package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static util.MyLogger.log;
import static util.SocketCloseUtil.closeAll;

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
                log("클라이언트로부터 받은 데이터: " + received);

                if (received.startsWith("/join")) {
                    userName = received;
                    log("초기 이름 저장 세션 이름 " + userName + " 이 저장됨 ");
                } else if (received.contains("message")) {
                    System.out.println("서버에 메시지 전달받음");
                    sessionManager.sendMessages(received.replace("message", "").trim());
                } else if (received.contains("change")) {
                    userName = received.replace("change","").trim();
                } else if (received.contains("users")) {
                    findAll();
                } else if (received.equals("exit")) {
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


    // 세션에 데이터 보내기
    public synchronized void sendMessage(String message) throws IOException {
        log("각 세션들에게 메시지가 전달됨");
        output.writeUTF(userName + " " + message);
    }

    private void findAll() throws IOException {
        List<String> userNameList = sessionManager.findAll();
        output.writeUTF(String.valueOf(userNameList));
    }

    //세션 종료시,서버 종료시 동시에 호출될 수 있다.
    public synchronized void close() {
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
