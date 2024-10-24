package was.v1;

import util.MyLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.*;
import static util.MyLogger.log;

/** 서버의 책임
 * - 외부로부터 PORT를 받은 후 ServerSocket을 생성한다.
 * - ServerSocket으로 TCP 연결이 된 Socket을 생성한다.
 * - socket을 session에게 넘겨주고 Thread를 실행시킨다.
 * - 스레드풀을 만들어 동시에 동작하는 스레드 생성한다.
 * */
public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        log("서버 시작 PORT : " + port);
        // 시작 후에 port를 이용하여 serverSocket을 만든다.
        ServerSocket serverSocket = new ServerSocket(port);

        // 매 요청때마다 서버에서 socket을 생성해야 하므로 반복문에 넣는다. -> 요청마다 새로운 소켓을 생성함
        while (true){
            Socket socket = serverSocket.accept();
            // 클라이언트 매 요청 시 서버에서 새로운 소켓을 통해 데이터를 주고 받는다.
            process(socket);
        }
    }

    /**
     * BufferedReader,InputStreamReader의를 쓰는 이유
     * - 클라이언트가 보낸 http 요청을 받을 수 있게 하기 위함
     * - http요청은 http 형식에 맞춰서 데이터가 전달되기 때문에, 형식에 맞춰서 데이터를 파싱해야함
     * -- socket으로부터 받은 byte[] 데이터를 InputStreamReader의 내부 문자 집합을 통해 문자 데이터로 변환
     * -- BufferedReader는 InputStreamReader을 통해 전환된 문자 데이터를 라인 단위로 읽을 수 있게 해줌
     *
     * PrintWriter
     * - 문자 데이터를 byte[]로 변환 후 socket을 통해 전송
     *
     * */
    public void process(Socket socket){
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(),false, UTF_8)){

            // client가 보낸 요청 데이터 파싱
            String requestString = requestToString(reader);
            log("HTTP 요청 정보 출력");
            System.out.println(requestString);

            if (requestString.contains("/favicon.ico")){
                log("favicon 요청");
                return;
            }

            // client 에게 보낼 데이터 생성 후 데이터 전송
            responseToClient(writer);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String requestToString(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            if(line.isEmpty()){
                break;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
    private void responseToClient(PrintWriter writer) {

        String body = "<h1>Hello World</h1>";
        int length = body.getBytes(UTF_8).length;

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Content-Length: ").append(length).append("\r\n");
        sb.append("\r\n"); // header, body 구분 라인

        sb.append(body);
        log("HTTP 응답 정보 출력");
        System.out.println(sb);

        writer.println(sb);
        writer.flush();
    }
}
