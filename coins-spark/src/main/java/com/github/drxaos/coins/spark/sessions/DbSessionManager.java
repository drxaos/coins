package com.github.drxaos.coins.spark.sessions;

import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.database.Db;
import com.github.drxaos.coins.application.factory.Inject;
import com.github.drxaos.coins.utils.DateUtil;
import com.google.common.collect.Maps;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.server.session.AbstractSession;
import org.eclipse.jetty.server.session.AbstractSessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static java.lang.Math.round;

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

        @Override
        public boolean access(long time) {
            return super.access(time);
        }

        @Override
        public void cookieSet() {
            super.cookieSet();
        }
    }

    protected boolean storeSession(Session session, boolean update) throws Exception {
        if (session == null)
            return false;

        log.debug("store session: " + session.getClusterId());

        com.github.drxaos.coins.domain.Session s;
        List<com.github.drxaos.coins.domain.Session> sessionList = db.getDao(com.github.drxaos.coins.domain.Session.class).queryForEq("name", session.getClusterId());
        if (sessionList.size() > 0) {
            s = sessionList.get(0);
            if (!update) {
                s.created(dateUtil.now());
            }
        } else if (!update) {
            s = new com.github.drxaos.coins.domain.Session().created(dateUtil.now());
        } else {
            return false;
        }

        s.name(session.getClusterId())
                .accessed(dateUtil.now())
                .dataMap(session.getAttributeMap())
                .userId((Long) session.getAttribute("__userId"))
                .label((String) session.getAttribute("__sessionLabel"))
                .save();
        return true;
    }

    protected void clearOldSessions() throws Exception {
        Integer keepMinutes = props.getInteger("jetty.session.keepMinutes", 60);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dateUtil.now());
        cal.add(Calendar.MINUTE, -keepMinutes);
        DeleteBuilder<com.github.drxaos.coins.domain.Session, Long> deleteBuilder = db.getDao(com.github.drxaos.coins.domain.Session.class).deleteBuilder();
        deleteBuilder.where().lt("accessed", cal.getTime());
        deleteBuilder.delete();
    }

    @Override
    public HttpCookie access(HttpSession session, boolean secure) {
        long now = dateUtil.now().getTime();

        Session s = (Session) ((SessionIf) session).getSession();

        if (s.access(now)) {
            // Do we need to refresh the cookie?
            if (isUsingCookies() &&
                    (s.isIdChanged() ||
                            (getSessionCookieConfig().getMaxAge() > 0 && getRefreshCookieAge() > 0 && ((now - s.getCookieSetTime()) / 1000 > getRefreshCookieAge()))
                    )
                    ) {
                HttpCookie cookie = getSessionCookie(session, _context == null ? "/" : (_context.getContextPath()), secure);
                s.cookieSet();
                s.setIdChanged(false);
                return cookie;
            }
        }
        return null;
    }

    @Override
    public void removeSession(AbstractSession session, boolean invalidate) {
        // Remove session from context and global maps
        boolean removed = removeSession(session.getClusterId());

        if (removed) {
            _sessionsStats.decrement();
            _sessionTimeStats.set(round((dateUtil.now().getTime() - session.getCreationTime()) / 1000.0));

            // Remove session from all context and global id maps
            _sessionIdManager.removeSession(session);
            if (invalidate)
                _sessionIdManager.invalidateAll(session.getClusterId());

            if (invalidate && _sessionListeners != null) {
                HttpSessionEvent event = new HttpSessionEvent(session);
                for (HttpSessionListener listener : _sessionListeners)
                    listener.sessionDestroyed(event);
            }
        }
    }

    protected Session loadSession(String name) throws Exception {
        if (name == null)
            return null;

        log.debug("load session: " + name);

        clearOldSessions();

        com.github.drxaos.coins.domain.Session s;
        List<com.github.drxaos.coins.domain.Session> sessionList = db.getDao(com.github.drxaos.coins.domain.Session.class).queryForEq("name", name);
        if (sessionList.size() > 0) {
            s = sessionList.get(0);
        } else {
            return null;
        }

        Session session = new Session(this, s.created().getTime(), s.accessed().getTime(), name, s.dataMap());

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
                    boolean success = storeSession(session, true);
                    if (!success) {
                        synchronized (_sessions) {
                            _sessions.remove(idInCluster);
                        }
                        return null;
                    }
                    session.dirty = false;
                } else if (session.getLastAccessedTime() + 1000 < dateUtil.now().getTime()) {

                    UpdateBuilder<com.github.drxaos.coins.domain.Session, Long> builder =
                            db.getDao(com.github.drxaos.coins.domain.Session.class)
                                    .updateBuilder();
                    builder.updateColumnValue("accessed", dateUtil.now());
                    builder.where().eq("name", session.getClusterId());
                    int updated = builder.update();

                    if (updated == 0) {
                        // no session
                        synchronized (_sessions) {
                            _sessions.remove(idInCluster);
                        }
                        return null;
                    }
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
        db.getDao(com.github.drxaos.coins.domain.Session.class).deleteBuilder().delete();
    }

    public void invalidateSessions(Long userId, String keepSession) throws Exception {
        log.debug("invalidate sessions");

        DeleteBuilder<com.github.drxaos.coins.domain.Session, Long> builder =
                db.getDao(com.github.drxaos.coins.domain.Session.class).deleteBuilder();
        builder.where().eq("userId", userId).and().not().eq("name", keepSession);
        builder.delete();
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
            DeleteBuilder<com.github.drxaos.coins.domain.Session, Long> deleteBuilder = db.getDao(com.github.drxaos.coins.domain.Session.class).deleteBuilder();
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
            UpdateBuilder<com.github.drxaos.coins.domain.Session, Long> updateBuilder = db.getDao(com.github.drxaos.coins.domain.Session.class).updateBuilder();
            updateBuilder.where().eq("name", oldClusterId);
            updateBuilder.updateColumnValue("name", newClusterId);
            updateBuilder.update();
        } catch (Exception e) {
            log.warn("Unable to update session id=" + oldClusterId, e);
        }
    }
}
