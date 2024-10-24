package was.v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpServerHandlerV2 implements Runnable{

    private final Socket socket;

    public HttpServerHandlerV2(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        process();
    }

    public void process(){
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(),false, UTF_8)){

            // client가 보낸 요청 데이터 파싱
            String requestString = requestToString(reader);
            log("HTTP 요청 정보 출력");
            System.out.println(requestString);

            log("요청 처리 시간 3초");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

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
