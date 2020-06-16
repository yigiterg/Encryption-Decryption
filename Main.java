package encryptdecrypt;

import java.io.*;
import java.util.Scanner;
public class Main {


    public static StringBuffer FileReader(String inputFile) throws FileNotFoundException {
        File file = new File(inputFile);
        Scanner scanner = new Scanner(file);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(scanner.nextLine());
        StringBuffer readData = new StringBuffer(stringBuffer);
        scanner.close();
        return readData;
    }
    public static void WriteToFile(StringBuffer password, String outputFile) throws IOException {
        File file = new File(outputFile);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(password.toString());
        fileWriter.close();
    }


    public static void main(String[] args) throws IOException {
        StringBuffer mode = new StringBuffer();
        StringBuffer alg = new StringBuffer();
        StringBuffer inputFile = new StringBuffer();
        StringBuffer outputFile = new StringBuffer();
        StringBuffer data = new StringBuffer();
        int key = 0;
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-mode")) {
                mode.append(args[i+1]);
            }
            if(args[i].equals("-alg")) {
                alg.append(args[i+1]);
            }
            if(args[i].equals("-key")) {
                key = Integer.parseInt(args[i+1]);
            }
            if(args[i].equals("-in")) {
                inputFile.append(args[i+1]);
            }
            if(args[i].equals("-out")) {
                outputFile.append(args[i+1]);
            }
            if(args[i].equals("-data")) {
                data.append(args[i+1]);
            }

        }
        if(inputFile.length() != 0 && data.length() == 0) {
            data = FileReader(inputFile.toString());
        }
        Context context = new Context(null);
        switch(alg.toString()) {
            case "unicode" : {
                EncryptionDecryption encryptionDecryption = new Unicode();
                context.setEncryptionDecryption(encryptionDecryption);
                break;
            }
            default : {
                EncryptionDecryption encryptionDecryption = new Shift();
                context.setEncryptionDecryption(encryptionDecryption);
                break;
            }
        }

        if(outputFile.length() != 0) {
            WriteToFile(context.executeStrategy(data,key,mode.toString()),outputFile.toString());
        } else {
            System.out.println(context.executeStrategy(data,key, mode.toString()));
        }
       /* StringBuffer amil = new StringBuffer();
        amil.append("Welcome to hell");
        Context context1 = new Context(new Unicode());
        System.out.println(context1.executeStrategy(amil,5,"enc"));*/
    }
}



 interface EncryptionDecryption {

    StringBuffer encryption(StringBuffer string, int key);
    StringBuffer decryption(StringBuffer string, int key);


}

 class Context {

    private EncryptionDecryption encryptionDecryption;


    public void setEncryptionDecryption(EncryptionDecryption encryptionDecryption) {
        this.encryptionDecryption = encryptionDecryption;
    }

    public  Context(EncryptionDecryption encryptionDecryption){

        this.encryptionDecryption = encryptionDecryption;

    }
    public StringBuffer executeStrategy(StringBuffer string, int key, String mode){
        if(mode.equals("dec")) {
            return encryptionDecryption.decryption(string, key);
        } else {
            return encryptionDecryption.encryption(string, key);
        }
    }

}
 class Unicode implements EncryptionDecryption {

    @Override
    public StringBuffer decryption(StringBuffer string, int key) {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < string.length(); i++) {
            stringBuffer.append((char)(string.charAt(i) - key));
        }
        return stringBuffer;
    }
    @Override
    public StringBuffer encryption(StringBuffer string, int key){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < string.length(); i++) {
            stringBuffer.append((char)(string.charAt(i) + key));
        }
        return stringBuffer;
    }

}




class Shift implements EncryptionDecryption {

    @Override
    public StringBuffer encryption(StringBuffer string, int key) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == ' ') {
                stringBuffer.append(' ');
                continue;
            }
            if(string.charAt(i) >= 97) {
                stringBuffer.append((char) ('a' + ((string.charAt(i)+key-'a')%26) ));
            } else {
                stringBuffer.append((char) ('A' + ((string.charAt(i)+key-'A')%26) ));
            }
        }
        return stringBuffer;

    }

    @Override
    public StringBuffer decryption(StringBuffer string, int key) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == ' ') {
                stringBuffer.append(' ');
                continue;
            }
            if(string.charAt(i) >= 97) {
                    stringBuffer.append((char) ('a' + ((string.charAt(i)-key-'a')+26)%26) );
                }
             else {
                stringBuffer.append((char) ('A' + ((string.charAt(i)-key-'A')+26)%26) );
            }
        }
        return stringBuffer;
    }
}


