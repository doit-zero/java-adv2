package was.v4;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpServerHandlerV4 implements Runnable {
    private final Socket socket;

    public HttpServerHandlerV4(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (IOException e) {
            log(e);
        }
    }

    public void process() throws IOException {
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), false, UTF_8)) {

            // 매 요청마다 httpRequest & httpResponse 객체가 생성되어야함

            HttpRequest request = new HttpRequest(reader);
            HttpResponse response = new HttpResponse(writer);
            
            log("HTTP 요청 정보 출력");

            if (request.getPath().equals("/site1")) {
                site1(response);
            } else if (request.getPath().equals("/site2")) {
                site2(response);
            } else if (request.getPath().equals("/searc")) {
                search(request, response);
            } else if (request.getPath().equals("/")) {
                home(response);
            } else {
                notFound(response);
            }
            log("HTTP 응답 전달 완료");
        }
    }

    private String requestToString(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private static void home(HttpResponse response) {
        response.writeBody("<h1>home</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li><a href='/site1'>site1</a></li>");
        response.writeBody("<li><a href='/site2'>site2</a></li>");
        response.writeBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writeBody("</ul>");
    }

    private static void site1(HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
//        writer.println("HTTP/1.1 200 OK");
//        writer.println("Content-Type: text/html; charset=UTF-8");
//        writer.println();
//        writer.println("<h1>site1</h1>");
//        writer.flush();
    }

    private static void site2(HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
    private static void notFound(HttpResponse response) {
        response.setStatus(404);
        response.writeBody("<h1>404 페이지를 찾을 수 없습니다.</h1>");
    }

    private static void search(HttpRequest request,HttpResponse response) {
        String query = request.getParameter("q");
        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }
}


