package chat.client;


import util.MyLogger;

import java.io.DataInputStream;
import java.io.IOException;

import static util.MyLogger.log;

// 서버로부터 데이터를 받는 것을 담당
public class ReadHandler implements Runnable{

    private final Client client;

    public ReadHandler(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try{
            while (true){
                String received = client.getInput().readUTF();
                System.out.println(received);
            }
        } catch (IOException e) {
            log(e);
        }finally {
            client.close();
        }
    }
}
