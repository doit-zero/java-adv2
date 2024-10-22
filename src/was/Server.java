package was;

import util.MyLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;

/** 서버의 책임
 * - 외부로부터 PORT를 받은 후 ServerSocket을 생성한다.
 * - ServerSocket으로 TCP 연결이 된 Socket을 생성한다.
 * - socket을 session에게 넘겨주고 Thread를 실행시킨다.
 * - 스레드풀을 만들어 동시에 동작하는 스레드 생성한다.
 * */
public class Server {
    private final int port;
    private final ServerSocket serverSocket;

    public Server(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public void start(){
        log("서버 시작 PORT : " + port);

        try {
            while (true){
                Socket socket = serverSocket.accept();
                Session session = new Session(socket);
                Thread thread = new Thread(session);
                thread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
