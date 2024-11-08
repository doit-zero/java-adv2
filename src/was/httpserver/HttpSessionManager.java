package was.httpserver;

import util.MyLogger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static util.MyLogger.log;

public class HttpSessionManager {

    private final Map<String, HttpSession> sessions;
    private final long sessionTimeout;
    public HttpSessionManager() {
        sessions = new ConcurrentHashMap<>();
        sessionTimeout = 5 * 60 * 1000;
    }

    public HttpSession createSession() {
        String sessionId = UUID.randomUUID().toString();
        HttpSession session = new HttpSession(sessionId);

        sessions.put(sessionId, session);
        return session;
    }

    public HttpSession getSession(String sessionId) {
        // sessions에 session이 있는지 확인
        HttpSession session = sessions.get(sessionId);
        if (session == null) {
            log("세션 새로 새성2");
            return createSession(); // 세션이 없으면 새로 생성
        }

        // 만료 확인
        if (isExpired(session)) {
            invalidateSession(sessionId);
            return createSession(); // 만료된 세션이면 새로 생성
        }

        return session; // 기존 세션 반환

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
