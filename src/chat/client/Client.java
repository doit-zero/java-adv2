package chat.client;

import util.MyLogger;
import util.SocketCloseUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.DoubleSummaryStatistics;
import java.util.Scanner;

import static util.MyLogger.log;

public class Client {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;

    private boolean closed = false;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    public void close(){
        if(closed){
            return;
        }
        closed = true;
        SocketCloseUtil.closeAll(socket,input,output);

    }

    public DataOutputStream getOutput() {
        return output;
    }
}
