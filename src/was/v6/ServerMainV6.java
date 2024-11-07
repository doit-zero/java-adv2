package was.v6;

import was.httpserver.HttpServer;
import was.httpserver.HttpSessionManager;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.reflection.ReflectionServlet;
import was.v5.servlet.HomeServlet;
import was.v5.servlet.SearchServlet;
import was.v5.servlet.Site1Servlet;
import was.v5.servlet.Site2Servlet;

import java.io.IOException;
import java.util.List;

public class ServerMainV6 {
    private static final int port = 12345;
    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SearchControllerV6(),new SiteControllerV6());

        ReflectionServlet reflectionServlet = new ReflectionServlet(controllers);

        HttpSessionManager httpSessionManager = new HttpSessionManager();

        ServletManager servletManager = new ServletManager();

        // 리플렉션 서블릿을  기본 서블릿으로 등록하면 요청 경로와 같은 서블릿을 찾지 못하면 리플렉션 서블릿을 반환함
        servletManager.setDefaultServlet(reflectionServlet);
        servletManager.add("/",new HomeServlet());
        servletManager.add("/favicon",new DiscardServlet());
        HttpServer server = new HttpServer(port,httpSessionManager,servletManager);
        server.start();
    }
}
