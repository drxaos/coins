package com.github.drxaos.coins.javafx;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Register a protocol handler for URLs like this: <code>webapp:///pics/sland.gif</code><br>
 */
public class WebAppURLConnection extends URLConnection {

    Object target;
    private byte[] data;

    protected WebAppURLConnection(URL url, Object target) {
        super(url);
        this.target = target;
    }

    @Override
    public void connect() throws IOException {
        if (connected) {
            return;
        }
        loadData();
        connected = true;
    }

    public InputStream getInputStream() throws IOException {
        connect();
        return new ByteArrayInputStream(data);
    }

    static public boolean isDirectory(URL url) throws IOException {
        String protocol = url.getProtocol();
        if (protocol.equals("file")) {
            return new File(url.getFile()).isDirectory();
        }
        if (protocol.equals("jar")) {
            String file = url.getFile();
            int bangIndex = file.indexOf('!');
            String jarPath = file.substring(bangIndex + 2);
            file = new URL(file.substring(0, bangIndex)).getFile();
            ZipFile zip = new ZipFile(file);
            ZipEntry entry = zip.getEntry(jarPath);
            boolean isDirectory = entry.isDirectory();
            if (!isDirectory) {
                InputStream input = zip.getInputStream(entry);
                isDirectory = input == null;
                if (input != null) input.close();
            }
            return isDirectory;
        }
        throw new RuntimeException("Invalid protocol: " + protocol);
    }

    public static final String[] BASE = {"/static", "/META-INF/resources"};

    private void loadData() throws IOException {
        if (data != null) {
            return;
        }
        URL url = getURL();
        String filePath = url.toExternalForm();
        filePath = filePath.startsWith("webapp://") ? filePath.substring("webapp://".length()) : filePath.substring("webapp:".length()); // attention: triple '/' is reduced to a single '/'
        data = new byte[0];
        for (String base : BASE) {
            String fp = base + filePath;
            try {
                URL resource = target.getClass().getResource(fp);
                if (resource == null || isDirectory(resource)) {
                    fp += "/index.html";
                    resource = target.getClass().getResource(fp);
                }
                if (resource != null) {
                    System.out.println("Loading: " + resource);
                    data = IOUtils.toByteArray(resource.openConnection());
                    return;
                }
            } catch (IOException e) {
                // next
            }
        }
        System.out.println("Not found: " + filePath);
    }

    public OutputStream getOutputStream() throws IOException {
        return new ByteArrayOutputStream();
    }

    public java.security.Permission getPermission() throws IOException {
        return null;
    }

}