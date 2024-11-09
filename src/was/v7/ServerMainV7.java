package was.v7;

import was.httpserver.HttpServer;
import was.httpserver.HttpSessionManager;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServletV1;
import was.httpserver.servlet.reflection.ReflectionServlet;
import was.v5.servlet.HomeServlet;
import was.v6.SearchControllerV6;
import was.v6.SiteControllerV6;

import java.io.IOException;
import java.util.List;

public class ServerMainV7 {
    private static final int port = 12345;
    public static void main(String[] args) throws IOException {
        List<Object> controllers = List.of(new SearchControllerV7(),new SiteControllerV7());

        AnnotationServletV1 annotationServlet = new AnnotationServletV1(controllers);

        HttpSessionManager httpSessionManager = new HttpSessionManager();

        ServletManager servletManager = new ServletManager();

        // 리플렉션 서블릿을  기본 서블릿으로 등록하면 요청 경로와 같은 서블릿을 찾지 못하면 리플렉션 서블릿을 반환함
        servletManager.setDefaultServlet(annotationServlet);
        servletManager.add("/favicon",new DiscardServlet());
        HttpServer server = new HttpServer(port,httpSessionManager,servletManager);
        server.start();
    }
}
