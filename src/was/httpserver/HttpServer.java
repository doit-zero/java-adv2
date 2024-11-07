package was.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;


/**
 * 스레드 풀에서 스레드를 가져와 클라이언트의 요청을 handler로 넘긴다.
 * */
public class HttpServer {

    private final int PORT;
    private final ExecutorService es = Executors.newFixedThreadPool(10);
    private final HttpSessionManager sessionManager;
    private final ServletManager servletManager;

    public HttpServer(int PORT, HttpSessionManager sessionManager, ServletManager servletManager) {
        this.PORT = PORT;
        this.sessionManager = sessionManager;
        this.servletManager = servletManager;
    }

    public void start(){
        try {
            log("서버 소켓 port : " + PORT);
            ServerSocket serverSocket = new ServerSocket(PORT);


            while (true){
                Socket socket = serverSocket.accept();
                es.submit(new HttpServerHandler(socket,sessionManager,servletManager));
            }
        } catch (IOException e){
            log(e);
        }
    }
}
