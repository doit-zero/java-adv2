package io.member.impl;

import io.member.Member;
import io.member.MemberRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.*;

public class FileMemberRepository implements MemberRepository {

    private static final String FILE_PATH = "temp/members-txt.dat";
    private static final String DELIMITER = ",";

    @Override
    public void add(Member member) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH,UTF_8,true))){
            bw.write(member.getId()+ DELIMITER + member.getName() + DELIMITER + member.getAge());
            bw.newLine();
            // bw.close 는 try가 끝나면 자동으로 close되기 때문에 쓰지 않음
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH,UTF_8))){
            String line;
            while ((line = br.readLine()) != null){
                String[] memberData = line.split(DELIMITER);
                Member findMember = new Member(memberData[0], memberData[1], Integer.valueOf(memberData[2]));
                members.add(findMember);
            }
            return members;
        } catch (FileNotFoundException e){
            return new ArrayList<>();
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
