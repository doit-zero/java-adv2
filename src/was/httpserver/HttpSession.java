package was.httpserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private String sessionId;
    private long creationTime;
    private Map<String, Object> data = new HashMap<>();

    public HttpSession(String sessionId) {
        this.sessionId = sessionId;
        this.creationTime = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "HttpSession{" +
                "sessionId='" + sessionId + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}
