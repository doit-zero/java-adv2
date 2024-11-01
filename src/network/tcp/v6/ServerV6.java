package network.tcp.v6;

import network.tcp.v5.SessionV5;
import util.MyLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static util.MyLogger.log;

public class ServerV6 {
    private static final int PORT = 12345;
    public static void main(String[] args) throws IOException, InterruptedException {
        log("서버 시작");
        ServerSocket serverSocket = new ServerSocket(PORT);
        SessionManagerV6 sessionManager = new SessionManagerV6();
        log("서버 소켓 시작 - 리스닝 포트: " + PORT);

        // ShutdownHook 등록
        ShutdownHook shutdownHook = new ShutdownHook(sessionManager, serverSocket);
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook,"shutdown"));

        // serverSocket 종료시 예외가 발생하므로 try catch 안에서 로직 실행
        try {
            while (true){
                Socket socket = serverSocket.accept();
                log("소켓 연결: " + socket);
                SessionV5 session = new SessionV5(socket);
                Thread thread = new Thread(session);
                thread.start();
            }
        } catch (IOException e){
            log("서버 소켓 종료 : " + e);
        }

    }

    static class ShutdownHook implements Runnable{
        private final SessionManagerV6 sessionManager;
        private final ServerSocket serverSocket;

        public ShutdownHook(SessionManagerV6 sessionManager, ServerSocket serverSocket) {
            this.sessionManager = sessionManager;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            log("shutdownHook 실행");
            try {
                //1 세션 매니져에 있는 모든 세션들을 종료시키기
                sessionManager.closeAll();
                //2 serverSocket도 종료
                serverSocket.close();

                // 자원정리 대기
                Thread.sleep(1000);
            } catch (Exception e){
                log(e);
            }
        }
    }
}
