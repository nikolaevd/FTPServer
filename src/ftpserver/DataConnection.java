
package ftpserver;

import java.net.*;
import java.io.*;

public class DataConnection extends Thread{
    
    final int LOCAL_PORT = 20;
    DataInputStream input;
    DataOutputStream output;
    
    DataConnection(int port) throws IOException{
        try(Socket s = new Socket("localhost", port, InetAddress.getLocalHost(), LOCAL_PORT)){
            input = new DataInputStream(s.getInputStream());
            output = new DataOutputStream(s.getOutputStream());
            
            System.out.println("Data Connection Has Started...");
            start();
        }
    }
    
    @Override
    public void run(){
        
    }
}
