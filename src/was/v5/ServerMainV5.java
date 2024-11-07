package was.v5;

import was.httpserver.HttpServer;
import was.httpserver.HttpServerHandler;
import was.httpserver.HttpSessionManager;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.v4.HttpServerV4;
import was.v5.servlet.HomeServlet;
import was.v5.servlet.SearchServlet;
import was.v5.servlet.Site1Servlet;
import was.v5.servlet.Site2Servlet;

import java.io.IOException;

public class ServerMainV5 {
    private static final int port = 12345;
    public static void main(String[] args) throws IOException {
        HttpSessionManager httpSessionManager = new HttpSessionManager();
        ServletManager servletManager = new ServletManager();
        servletManager.add("/",new HomeServlet());
        servletManager.add("/site1",new Site1Servlet());
        servletManager.add("/site2",new Site2Servlet());
        servletManager.add("/search",new SearchServlet());
        servletManager.add("/favicon",new DiscardServlet());

        HttpServer server = new HttpServer(port,httpSessionManager,servletManager);
        server.start();
    }
}
