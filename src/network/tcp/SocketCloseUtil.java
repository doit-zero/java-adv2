package network.tcp;

import util.MyLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static util.MyLogger.log;

public abstract class SocketCloseUtil {
    public static void closeAll(Socket socket, InputStream input, OutputStream output){
        close(output);
        close(input);
        close(socket);
    }

    private static void close(OutputStream output) {
        if(output != null){
            try {
                output.close();
            } catch (IOException e) {
                log(e);
            }
        }
    }

    private static void close(InputStream input) {
        if(input != null){
            try {
                input.close();
            } catch (IOException e) {
                log(e);
            }
        }
    }

    private static void close(Socket socket) {
        if(socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                log(e);
            }
        }
    }


}
