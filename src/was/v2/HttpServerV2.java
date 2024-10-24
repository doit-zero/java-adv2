package was.v2;

import util.MyLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;

public class HttpServerV2 {

    private final int PORT;
    private final ExecutorService es = Executors.newFixedThreadPool(10);
    public HttpServerV2(int PORT) {
        this.PORT = PORT;
    }

    public void start(){
        try {
            log("서버 소켓 port : " + PORT);
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true){
                Socket socket = serverSocket.accept();
                es.submit(new HttpServerHandlerV2(socket));
            }
        } catch (IOException e){
            log(e);
        }
    }
}
