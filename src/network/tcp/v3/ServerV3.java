package network.tcp.v3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class ServerV3 {
    private static final int PORT = 12345;
    public static void main(String[] args) throws IOException {
        log("서버 시작");
        ServerSocket serverSocket = new ServerSocket(PORT);
        log("서버 소켓 시작 - 리스닝 포트: " + PORT);

        while (true){
            Socket socket = serverSocket.accept();
            log("소켓 연결: " + socket);

            DataInputStream input = new DataInputStream(socket.getInputStream());
            String received = input.readUTF();

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(received + " 서버로 데이터 받음");
//            SessionV3 session = new SessionV3(socket);
//            Thread thread = new Thread(session);
//            thread.start();
        }

    }
}
