
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

class TransferFile extends Thread {
    Socket incoming;

    InputStream input;
    OutputStream output;
    
    TransferFile(Socket soc) {
        try {
            incoming = soc;                        
            input = incoming.getInputStream();
            output = incoming.getOutputStream();
            System.out.println("FTP Client Connected ...");
            start();          
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    @Override
    public void run(){
        while(true){
            try{
                System.out.println("Waiting for command...");
                try(Scanner sc = new Scanner(input)){
                    PrintWriter out  = new PrintWriter(output, true);
                    out.println("Hello! Enter BYE to exit.");
                    
                    boolean done = false;
                    while(!done && sc.hasNextLine()){
                        String line = sc.nextLine();
                        out.println("Echo: " + line);
                        if(line.trim().equals("BYE")) done = true;
                    }
                }
            }
            catch(Exception ex){
                
            }
        }
    }
    
//    void SendFile() throws Exception {        
//        String filename = input.readUTF();
//        File f = new File(filename);
//        if(!f.exists()) {
//            output.writeUTF("File Not Found");
//            return;
//        }
//        else {
//            output.writeUTF("READY");
//            FileInputStream fin = new FileInputStream(f);
//            int ch;
//            do {
//                ch = fin.read();
//                output.writeUTF(String.valueOf(ch));
//            }
//            while(ch != -1);    
//            fin.close();    
//            output.writeUTF("File Receive Successfully");                            
//        }
//    }
//    
//    void ReceiveFile() throws Exception {
//        String filename = input.readUTF();
//        if(filename.compareTo("File not found") == 0) {
//            return;
//        }
//        File f = new File(filename);
//        String option;
//        
//        if(f.exists()) {
//            output.writeUTF("File Already Exists");
//            option = input.readUTF();
//        }
//        else {
//            output.writeUTF("SendFile");
//            option="Y";
//        }
//            
//        if(option.compareTo("Y") == 0) {
//            FileOutputStream fout = new FileOutputStream(f);
//            int ch;
//            String temp;
//            do {
//                temp = input.readUTF();
//                ch=Integer.parseInt(temp);
//                if(ch != -1) {
//                    fout.write(ch);                    
//                }
//            }
//            while(ch != -1);
//            fout.close();
//            output.writeUTF("File Send Successfully");
//        }
//        else {
//            return;
//        }      
//    }
//
//    @Override
//    public void run() {
//        while(true) {
//            try {
//                System.out.println("Waiting for Command ...");
//                String Command = input.toString();
//                System.out.println("JOPA");
//                if(Command.compareTo("GET") == 0) {
//                    System.out.println("\tGET Command Received ...");
//                    SendFile();
//                    continue;
//                }
//                else if(Command.compareTo("SEND") == 0) {
//                    System.out.println("\tSEND Command Receiced ...");                
//                    ReceiveFile();
//                    continue;
//                }
//                else if(Command.compareTo("DISCONNECT") == 0) {
//                    System.out.println("\tDisconnect Command Received ...");
//                    System.exit(1);
//                }
//            }
//            catch(Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
}