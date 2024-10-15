package chat;

import util.MyLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.DoubleSummaryStatistics;
import java.util.Scanner;

import static util.MyLogger.log;

public class Client {
    public static final int PORT = 12345;

    public static void main(String[] args) {
        log("join : 입장 / message : 메세지 전송 / change : 이름 변경 / users 채팅에 접속한 전체 사용자 목록 / exit 서버 접속 종료 ");
        String name = "";

        Scanner scanner = new Scanner(System.in);
        System.out.printf("명령어를 입력하세요: ");
        String received = scanner.nextLine();

        // 명령어가 'join'이 입력될 때까지 반복
        while (!received.equals("join")) {
            System.out.printf("채팅방에 입장하기 위해서는 join 입력하세요: ");
            received = scanner.nextLine();
        }

        // 이름이 입력될 때까지 반복
        while (name.isEmpty()) {
            System.out.printf("이름을 입력하세요: ");
            name = scanner.nextLine();

            if (name.isEmpty()) {
                System.out.println("이름은 반드시 입력하셔야 합니다.");
            }
        }

        try (Socket socket = new Socket("localhost", PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            log("소캣 연결 후 이름 전송 : " + socket);
            output.writeUTF(name);

            while (true) {
                System.out.print("명령어를 입력하세요: ");
                String command = scanner.nextLine();

                if (command.equals("message")) {
                    System.out.printf("메세지를 입력하세요: ");
                    String inputMessage = scanner.nextLine();
                    output.writeUTF("message " + inputMessage);

                } else if(command.equals("change")){
                    System.out.printf("바꿀 이름을 입력하세요: ");
                    String changedName = scanner.nextLine();
                    output.writeUTF("change" + changedName);
                } else if (command.equals("users")){
                    output.writeUTF("users");
                } else if (command.equals("exit")) {
                    break;
                }

                String receivedFrom = input.readUTF();
                log("서버로부터 받은 데이터: " + receivedFrom);

            }
        } catch (IOException e) {
            log(e);
        }
    }
}
