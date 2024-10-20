package chat.client;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {

    private static final int PORT = 12345;
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", PORT);
        Client client = new Client(socket);
        client.start();
    }
}
