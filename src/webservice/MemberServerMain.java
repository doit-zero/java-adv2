package webservice;

import io.member.MemberRepository;
import io.member.impl.FileMemberRepository;
import was.httpserver.servlet.annotation.AnnotationServletV2;

import java.util.List;

public class MemberServerMain {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        MemberRepository memberRepository = new FileMemberRepository();
        MemberController memberController = new MemberController(memberRepository);
        List<MemberController> memberController1 = List.of(memberController);

    }
}
