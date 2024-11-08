package was.v4;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpSession;
import was.httpserver.HttpSessionManager;
//import was.httpserver.HttpSession;
//import was.httpserver.HttpSessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.Socket;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpServerHandlerV4 implements Runnable {
    private final Socket socket;

    private HttpSessionManager sessionManager;

//    public HttpServerHandlerV4(Socket socket){
//        this.socket = socket;
//    }

    public HttpServerHandlerV4(Socket socket,HttpSessionManager sessionManager) {
        this.socket = socket;
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (IOException e) {
            log("스레드에서 예외 상황 발생 : " + e);
        }
    }

    public void process() throws IOException {
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), false, UTF_8)) {

            // 매 요청마다 httpRequest & httpResponse 객체가 생성되어야함
            HttpRequest request = new HttpRequest(reader);
            HttpResponse response = new HttpResponse(writer);

            if (request.getPath().equals("/favicon.ico")) {
                log("favicon 요청");
                return;
            }

            createCookie(request, response);

            if (request.getPath().equals("/site1")) {
                site1(response);
            } else if (request.getPath().equals("/site2")) {
                site2(response);
            } else if (request.getPath().equals("/search")) {
                search(request, response);
            } else if (request.getPath().equals("/")) {
                home(response);
            } else {
                notFound(response);
            }

            response.flush();
            log("HTTP 응답 전달 완료");
        }
    }

    private void createCookie(HttpRequest request, HttpResponse response) {
        HttpSession session = null;
        if (request.getHeaders().containsKey("Cookie")){
            String[] splitedCookies = request.getHeader("Cookie").split(" ");
            String sessionId = splitedCookies[0];
            String[] split = sessionId.split("=");
            String realSessionId = split[1].replace("\"", "").replace(";", "");
            System.out.println("realSessionId " + realSessionId);

            session = sessionManager.getSession(realSessionId);
            HttpCookie cookie = new HttpCookie("SESSIONID", session.getSessionId());
            cookie.setHttpOnly(true);
            response.setCookie(cookie);
            return;
        }


        HttpCookie cookie = new HttpCookie("SESSIONID", session.getSessionId());
        cookie.setHttpOnly(true);
        response.setCookie(cookie);
    }


    private void home(HttpResponse response) {
        response.writeBody("<h1>home</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li><a href='/site1'>site1</a></li>");
        response.writeBody("<li><a href='/site2'>site2</a></li>");
        response.writeBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writeBody("</ul>");
    }

    private static void site1(HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
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


