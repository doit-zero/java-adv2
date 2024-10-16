package chat.client;


import util.MyLogger;

import java.io.DataInputStream;
import java.io.IOException;

import static util.MyLogger.log;

// 서버로부터 데이터를 받는 것을 담당
public class ReadHandler implements Runnable{

    private final Client client;

    public ReadHandler(DataInputStream input, Client client) {

        this.client = client;
    }

//    public ReadHandler(DataInputStream input) {
//        this.input = input;
//    }
    @Override
    public void run() {
//        try{
//            while (true){
//                String received = input.readUTF();
//                System.out.println(received);
//            }
//        } catch (IOException e) {
//            log(e);
//        }finally {
//            client.close();
//        }
    }
}
