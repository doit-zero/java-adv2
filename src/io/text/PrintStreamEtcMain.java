package io.text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class PrintStreamEtcMain {
    public static void main(String[] args) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream("temp/print.txt");
        PrintStream printStream = new PrintStream(fos);
        while (true){
            Scanner scanner = new Scanner(System.in);
            String readData = scanner.nextLine();
            if(readData.equals("exit")){
                break;
            }
            printStream.println(readData);
        }
    }
}
