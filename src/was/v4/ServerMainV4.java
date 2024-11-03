package was.v4;

import was.v3.HttpServerV3;

import java.io.IOException;

public class ServerMainV4 {
    private static final int port = 12345;
    public static void main(String[] args) throws IOException {
        HttpServerV4 server = new HttpServerV4(port);
        server.start();
    }
}
