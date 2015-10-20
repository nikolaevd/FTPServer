
package ftpserver;

import java.net.*;
import java.io.*;

// клас реализует соединение для передачи данных
public class DataConnection implements Runnable{
    
//    private final int LOCAL_PORT = 20;
//    private final InetAddress LOCAL_ADDRESS = InetAddress.getLocalHost();
    private DataInputStream input;
    private DataOutputStream output;
    private String address;
    private int port;

    DataConnection(String addres, int port){
        this.address = addres;
        this.port = port;
    }
    
    @Override
    public void run(){
        //System.setProperty("java.net.preferIPv4Stack" , "true");
        try(Socket s = new Socket(address, port)){
            input = new DataInputStream(s.getInputStream());
            output = new DataOutputStream(s.getOutputStream());
            
            System.out.println("> Data Connection Has Started ...");
            try{
                FileWriter file = new FileWriter("HaxLogs.txt");
                String str;
                do{
                    str = input.readUTF();
                    file.write(str);
                }
                while(str != null);    
                file.close();
            }
            catch(IOException ex){
            ex.printStackTrace();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
   
}
