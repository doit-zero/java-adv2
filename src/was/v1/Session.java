//package was.v1;
//
//import util.MyLogger;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//import static util.MyLogger.log;
//
///**
// * - 응답값으로 <h1>Hello World</h1> 를 보낸다
// * */
//public class Session implements Runnable {
//
//    private final Socket socket;
//    public Session(Socket socket) throws IOException {
//        this.socket = socket;
//        // 종료후 자원 정리를 위해 생성자 주입 방식을 하지 않음
//        //this.writer = new PrintWriter(socket.getOutputStream(),false, UTF_8);
//    }UTF_8
//
//    @Override
//    public void run() {
//        try(socket;
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),UTF_8));
//            PrintWriter writer = new PrintWriter(socket.getOutputStream(),false, UTF_8)) {
//
//            requestData(reader);
//            responseToClient(writer);
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static void requestData(BufferedReader reader) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            if (line.isEmpty()) {
//                break;
//            }
//            sb.append(line).append("\n");
//        }
//        log("서버가 받은 데이터");
//        System.out.println(sb);
//    }
//
//    private void responseToClient(PrintWriter writer) {
//
//        String body = "<h1>Hello World</h1>";
//        int length = body.getBytes(UTF_8).length;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("HTTP/1.1 200 OK\r\n");
//        sb.append("Content-Type: text/html\r\n");
//        sb.append("Content-Length: ").append(length).append("\r\n");
//        sb.append("\r\n"); // header, body 구분 라인
//        sb.append(body); log("HTTP 응답 정보 출력");
//        System.out.println(sb);
//        writer.println(sb);
//        writer.flush();
//    }
//}
