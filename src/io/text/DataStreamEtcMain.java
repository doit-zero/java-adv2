package io.text;

import java.io.*;

import static io.text.TextConst.FILE_NAME;

public class DataStreamEtcMain {
    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream(FILE_NAME);
        DataOutputStream dos = new DataOutputStream(fos);

        dos.writeUTF("회원A");
        dos.writeInt(20);
        dos.close();

        FileInputStream fis = new FileInputStream(FILE_NAME);
        DataInputStream dis = new DataInputStream(fis);
        System.out.println(dis.readUTF());
        System.out.println(dis.readInt());
        dis.close();
    }
}
