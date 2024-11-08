package was.v4;

//import was.httpserver.HttpSessionManager;
import was.httpserver.HttpSessionManager;
import was.v3.HttpServerHandlerV3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;

public class HttpServerV4 {

    private final int PORT;
    private final ExecutorService es = Executors.newFixedThreadPool(10);
    public HttpServerV4(int PORT) {
        this.PORT = PORT;
    }

    public void start() throws IOException {
            log("서버 소켓 port : " + PORT);
            ServerSocket serverSocket = new ServerSocket(PORT);
            HttpSessionManager sessionManager = new HttpSessionManager();
            while (true){
                Socket socket = serverSocket.accept();
                es.submit(new HttpServerHandlerV4(socket,sessionManager));
                //es.submit(new HttpServerHandlerV4(socket));
            }
    }
}
