package chat.client;

import util.MyLogger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

// 서버에 데이터 쓰는 담당
public class WriteHandler implements Runnable{
    private final Client client;

    public WriteHandler(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            String name = "";
            do{
                System.out.print("이름을 입력하세요: ");
                name = scanner.nextLine();
            }while (name.isEmpty());

            // client 객체에게 서버 소켓과 연결된 DataOutStream을 요청한다.
            DataOutputStream output = client.getOutput();
            output.writeUTF(name);

            while (true){
                System.out.print("입력하세요: ");
                String received = scanner.nextLine();

                if(received.equals("exit")){
                    client.close();
                    break;
                }

            }


        } catch (IOException e){
            MyLogger.log(e);
        } finally {
            client.close();
        }
    }
}
