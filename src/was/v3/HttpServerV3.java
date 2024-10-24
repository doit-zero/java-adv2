package was.v3;

import was.v2.HttpServerHandlerV2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.MyLogger.log;

public class HttpServerV3 {

    private final int PORT;
    private final ExecutorService es = Executors.newFixedThreadPool(10);
    public HttpServerV3(int PORT) {
        this.PORT = PORT;
    }

    public void start(){
        try {
            log("서버 소켓 port : " + PORT);
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true){
                Socket socket = serverSocket.accept();
                es.submit(new HttpServerHandlerV3(socket));
            }
        } catch (IOException e){
            log(e);
        }
    }
}
