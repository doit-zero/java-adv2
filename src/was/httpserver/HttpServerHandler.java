package was.httpserver;

//import was.httpserver.HttpSession;
//import was.httpserver.HttpSessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpServerHandler implements Runnable {
    private final Socket socket;
    private final HttpSessionManager sessionManager;

    private final ServletManager servletManager;
    public HttpServerHandler(Socket socket,HttpSessionManager sessionManager,ServletManager servletManager) {
        this.socket = socket;
        this.sessionManager = sessionManager;
        this.servletManager = servletManager;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (IOException e) {
            log(e);
            e.printStackTrace();
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

            // 세션 확인 후 세션 생성
            HttpSession session = getHttpSession(request);
            // 쿠키에 세션Id 생성
            setSessionInCookie(response, session);

            servletManager.execute(request,response);
            response.flush();
            log("HTTP 응답 전달 완료");
        }
    }

    private static void setSessionInCookie(HttpResponse response, HttpSession session) {
        HttpCookie cookie = new HttpCookie("SESSIONID", session.getSessionId());
        cookie.setHttpOnly(true);
        response.setCookie(cookie);
    }

    private HttpSession getHttpSession(HttpRequest request) {
        // 매 요청마다 httpSession 확인
        String[] splitedCookies = request.getHeader("Cookie").split(" ");
        String sessionId = splitedCookies[0];
        String[] split = sessionId.split("=");
        String realSessionId = split[1].replace("\"", "").replace(";", "");

        HttpSession session = sessionManager.getSession(realSessionId);
        return session;
    }

}


