
package ftpserver;

import java.net.*;
import java.io.*;

public class DataConnection {
    
    final int PORT = 20;
    
    public void createDataChannel() throws IOException {
        
        try(ServerSocket s = new ServerSocket(PORT)){
            
            System.out.println("Data Connection Has Started on Port Number " + PORT);
            
            try(Socket incoming = s.accept()){
                DataInputStream inStream = new DataInputStream(incoming.getInputStream());
                DataOutputStream outStream = new DataOutputStream(incoming.getOutputStream());
                
            }
        }
    }
    
}
