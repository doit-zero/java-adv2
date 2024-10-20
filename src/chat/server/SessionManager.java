package chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private List<Session> sessions = new ArrayList<>();

    public synchronized void add(Session session) {
        sessions.add(session);
    }

    public synchronized void remove(Session session){
        sessions.remove(session);
    }

    public synchronized void closeAll() {
        for (Session session : sessions) {
            session.close();
        }
        sessions.clear();
    }

    public synchronized void sendMessages(String message) throws IOException {
        for (Session session : sessions){
            session.sendMessage(message);
        }
    }

    public synchronized List<String> findAll() {
        List<String> userNameList = new ArrayList<>();
        for (Session session : sessions) {
            userNameList.add(session.getUserName());
        }

        return userNameList;
    }
}
