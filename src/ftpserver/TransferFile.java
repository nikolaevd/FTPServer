
package ftpserver;

import java.net.*;
import java.io.*;

class TransferFile extends Thread {
    Socket incoming;

    DataInputStream input;
    DataOutputStream output;
    
    TransferFile(Socket soc) {
        try {
            incoming = soc;                        
            input = new DataInputStream(incoming.getInputStream());
            output = new DataOutputStream(incoming.getOutputStream());
            System.out.println("FTP Client Connected ...");
            start();          
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("Waiting for Command ...");
                String Command = input.readUTF();
                if(Command.compareTo("GET") == 0) {
                    System.out.println("\tGET Command Received ...");
                    sendFile();
                    continue;
                }
                else if(Command.compareTo("SEND") == 0) {
                    System.out.println("\tSEND Command Receiced ...");                
                    recieveFile();
                    continue;
                }
                else if(Command.compareTo("DISCONNECT") == 0) {
                    System.out.println("\tDisconnect Command Received ...");
                    System.exit(1);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    void sendFile() throws Exception {        
        String filename = input.readUTF();
        File f = new File(filename);
        if(!f.exists()) {
            output.writeUTF("File Not Found");
            return;
        }
        else {
            output.writeUTF("READY");
            FileInputStream fin = new FileInputStream(f);
            int ch;
            do {
                ch = fin.read();
                output.writeUTF(String.valueOf(ch));
            }
            while(ch != -1);    
            fin.close();    
            output.writeUTF("File Receive Successfully");                            
        }
    }
    
    void recieveFile() throws Exception {
        String filename = input.readUTF();
        if(filename.compareTo("File not found") == 0) {
            return;
        }
        File f = new File(filename);
        String option;
        
        if(f.exists()) {
            output.writeUTF("File Already Exists");
            option = input.readUTF();
        }
        else {
            output.writeUTF("SendFile");
            option="Y";
        }
            
        if(option.compareTo("Y") == 0) {
            FileOutputStream fout = new FileOutputStream(f);
            int ch;
            String temp;
            do {
                temp = input.readUTF();
                ch=Integer.parseInt(temp);
                if(ch != -1) {
                    fout.write(ch);                    
                }
            }
            while(ch != -1);
            fout.close();
            output.writeUTF("File Send Successfully");
        }
        else {
            return;
        }      
    }
    
}