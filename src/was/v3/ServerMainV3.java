package was.v3;

import was.v2.HttpServerV2;

import java.io.IOException;

public class ServerMainV3 {
    private static final int port = 12345;
    public static void main(String[] args) throws IOException {
        HttpServerV3 server = new HttpServerV3(port);
        server.start();
    }
}
