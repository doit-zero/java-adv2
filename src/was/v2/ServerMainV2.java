package was.v2;

import was.v1.Server;

import java.io.IOException;

public class ServerMainV2 {
    private static final int port = 12345;
    public static void main(String[] args) throws IOException {
        HttpServerV2 server = new HttpServerV2(port);
        server.start();
    }
}
