package com.github.drxaos.coins.spark.sessions;

import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.common.collect.Maps;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.session.AbstractSession;
import org.eclipse.jetty.server.session.AbstractSessionManager;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class DbSessionManager extends AbstractSessionManager {

    @Inject
    Db db;

    @Inject
    DateUtil dateUtil;

    @Inject
    ApplicationProps props;

    private final ConcurrentMap<String, Session> _sessions = Maps.newConcurrentMap();

    public static class Session extends AbstractSession {
        protected Session(AbstractSessionManager abstractSessionManager, long created, long accessed, String clusterId, Map<String, Object> attributes) {
            super(abstractSessionManager, created, accessed, clusterId);
            if (attributes != null) {
                super.addAttributes(attributes);
            }
        }

        boolean dirty = false;

        @Override
        public void setAttribute(String name, Object value) {
            super.setAttribute(name, value);
            dirty = true;
        }
    }

    protected void storeSession(Session session, boolean update) throws Exception {
        if (session == null)
            return;

        log.debug("store session: " + session.getClusterId());

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(b);
        HashMap<String, Object> toSave = new HashMap<>(Maps.filterKeys(session.getAttributeMap(), (k) -> k.startsWith("__")));
        stream.writeObject(toSave);
        stream.close();

        StoredSession s;
        List<StoredSession> sessionList = db.getDao(StoredSession.class).queryForEq("name", session.getClusterId());
        if (sessionList.size() > 0) {
            s = sessionList.get(0);
            if (!update) {
                s.created(dateUtil.now());
            }
        } else {
            s = new StoredSession().created(dateUtil.now());
        }

        s.name(session.getClusterId()).accessed(dateUtil.now()).data(b.toByteArray()).save();
    }

    protected void clearOldSessions() throws Exception {
        Integer keepMinutes = props.getInteger("jetty.session.keepMinutes", 60);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateUtil.now());
        cal.add(Calendar.MINUTE, -keepMinutes);
        DeleteBuilder<StoredSession, Long> deleteBuilder = db.getDao(StoredSession.class).deleteBuilder();
        deleteBuilder.where().lt("accessed", cal.getTime());
        deleteBuilder.delete();
    }

    protected Session loadSession(String name) throws Exception {
        if (name == null)
            return null;

        log.debug("load session: " + name);

        clearOldSessions();

        StoredSession s;
        List<StoredSession> sessionList = db.getDao(StoredSession.class).queryForEq("name", name);
        if (sessionList.size() > 0) {
            s = sessionList.get(0);
        } else {
            return null;
        }

        ByteArrayInputStream b = new ByteArrayInputStream(s.data());
        ObjectInputStream stream = new ObjectInputStream(b);
        HashMap<String, Object> map = (HashMap<String, Object>) stream.readObject();
        stream.close();

        Session session = new Session(this, s.created().getTime(), s.accessed().getTime(), name, map);

        s.accessed(dateUtil.now()).save();

        return session;
    }

    @Override
    protected void addSession(AbstractSession session) {
        if (session == null)
            return;

        log.debug("add session: " + session.getClusterId());

        synchronized (_sessions) {
            _sessions.put(session.getClusterId(), (Session) session);
        }

        try {
            synchronized (session) {
                session.willPassivate();
                storeSession((Session) session, false);
                session.didActivate();
            }
        } catch (Exception e) {
            log.warn("Unable to store new session id=" + session.getId(), e);
        }
    }

    @Override
    public AbstractSession getSession(String idInCluster) {
        if (idInCluster == null)
            return null;

        log.debug("get session: " + idInCluster);

        Session session;
        synchronized (_sessions) {
            session = _sessions.get(idInCluster);
        }
        try {
            if (session == null) {
                session = loadSession(idInCluster);
                if (session != null) {
                    _sessions.put(idInCluster, session);
                }
            } else {
                if (session.dirty) {
                    storeSession(session, true);
                    session.dirty = false;
                }
            }
            return session;
        } catch (Exception e) {
            log.warn("Unable to load session id=" + idInCluster, e);
        }

        return null;
    }

    @Override
    protected void invalidateSessions() throws Exception {
        log.debug("invalidate sessions");

        _sessions.clear();
        db.getDao(StoredSession.class).deleteBuilder().delete();
    }

    @Override
    protected AbstractSession newSession(HttpServletRequest request) {
        String sessionId = request.getRequestedSessionId();
        if (sessionId == null) {
            sessionId = _sessionIdManager.newSessionId(request, dateUtil.now().getTime());
        }
        log.debug("new session: " + sessionId);
        return new Session(this, dateUtil.now().getTime(), dateUtil.now().getTime(), sessionId, null);
    }

    @Override
    protected boolean removeSession(String idInCluster) {
        try {
            log.debug("remove session: " + idInCluster);
            DeleteBuilder<StoredSession, Long> deleteBuilder = db.getDao(StoredSession.class).deleteBuilder();
            deleteBuilder.where().eq("name", idInCluster);
            int n = deleteBuilder.delete();
            return n > 0;
        } catch (Exception e) {
            log.warn("Unable to delete session id=" + idInCluster, e);
        }
        return false;
    }

    @Override
    public void renewSessionId(String oldClusterId, String oldNodeId, String newClusterId, String newNodeId) {
        try {
            log.debug("update session: " + oldClusterId + " -> " + newClusterId);
            UpdateBuilder<StoredSession, Long> updateBuilder = db.getDao(StoredSession.class).updateBuilder();
            updateBuilder.where().eq("name", oldClusterId);
            updateBuilder.updateColumnValue("name", newClusterId);
            updateBuilder.update();
        } catch (Exception e) {
            log.warn("Unable to update session id=" + oldClusterId, e);
        }
    }
}
