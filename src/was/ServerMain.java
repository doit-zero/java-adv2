package was;

import java.io.IOException;

public class ServerMain {
    private static final int port = 12345;
    public static void main(String[] args) throws IOException {
        Server server = new Server(port);
        server.start();
    }
}
