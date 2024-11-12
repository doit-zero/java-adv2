package webservice;

import io.member.MemberRepository;
import io.member.impl.FileMemberRepository;
import was.httpserver.HttpServer;
import was.httpserver.HttpSessionManager;
import was.httpserver.ServletManager;
import was.httpserver.servlet.annotation.AnnotationServletV2;
import was.httpserver.servlet.annotation.AnnotationServletV3;

import java.util.List;

public class MemberServerMain {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        MemberRepository memberRepository = new FileMemberRepository();
        MemberController memberController = new MemberController(memberRepository);
        List<Object> memberControllers = List.of(memberController);

        ServletManager servletManager = new ServletManager();
        AnnotationServletV3 annotationServletV3 = new AnnotationServletV3(memberControllers);
        servletManager.setDefaultServlet(annotationServletV3);
        HttpSessionManager httpSessionManager = new HttpSessionManager();

        HttpServer httpServer = new HttpServer(PORT,httpSessionManager,servletManager);

        httpServer.start();
    }
}
