package was.httpserver;

import util.MyLogger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static util.MyLogger.log;

public class HttpSessionManager {

    private Map<String, HttpSession> sessions = new ConcurrentHashMap<>();
    private long sessionTimeout = 30 * 60 * 10;

    public HttpSession createSession() {
        String sessionId = UUID.randomUUID().toString();
        HttpSession session = new HttpSession(sessionId);

        log("createdSession : " + session);
        sessions.put(sessionId, session);
        return session;
    }

    public HttpSession getSession(String sessionId) {
        // sessions에 session이 있는지 확인
        if(!sessions.containsKey(sessionId)){
            return createSession();
        }

        // 만료 확인
        if(!isExpired(sessions.get(sessionId))){
            return sessions.get(sessionId);
        }

        invalidateSession(sessionId);
        return createSession();

    }

    public void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }
    private boolean isExpired(HttpSession session) {
        return System.currentTimeMillis() - session.getCreationTime() > sessionTimeout;
    }

    public Map<String, HttpSession> getSessions() {
        return sessions;
    }


}
