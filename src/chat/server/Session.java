package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static util.MyLogger.log;
import static util.SocketCloseUtil.closeAll;


/**
* Session 객체의 책임과 SessionManager와의 협력
 * == 책임 ==
 * 1. 클라이언트와 socket으로 연결되고 stream으로 데이터를 주고 받는다.
 * 2. 서버 종료 또는 클라이언트와 소켓 연결 종료시 socket,input,output 자원을 정리한다.
 * == 협력 ==
 * 1. 클라이언트로부터 받은 데이터를 sessionManager 보내어 다른 세션들에게도 데이터를 전달하도록 한다.
 *
* */
public class Session implements Runnable {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private final SessionManager sessionManager;
    private boolean closed = false;
    private String userName = "";

    public Session(Socket socket, SessionManager sessionManager) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.sessionManager = sessionManager;
        sessionManager.add(this);
    }

    @Override
    public void run() {
        try {

            while (true) {
                String received = input.readUTF();
                if (received.contains("/join")) {
                    userName = received.replace("/join","").trim();
                    sessionManager.sendMessages(userName + "님이 입장하셨습니다.");
                } else if (received.contains("/message")) {
                    String message = received.replace("/message", "").trim();
                    sessionManager.sendMessages(message);
                } else if (received.contains("/exit")) {
                    break;
                }
            }

        } catch (
                IOException e) {
            log(e);
        } finally {
            sessionManager.remove(this);
            close();
        }

    }


    // 클라이언트와 연결된 소켓에 데이터 보낸다.
    public synchronized void sendMessage(String message) throws IOException {
        output.writeUTF(message);
    }

    public void close() {
        if (closed) {
            return;
        }
        closeAll(socket, input, output);
        closed = true;
        log("연결 종료: " + socket);
    }

    public String getUserName() {
        return userName;
    }
}
