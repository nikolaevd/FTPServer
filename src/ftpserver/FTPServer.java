
package ftpserver;

import java.io.IOException;
import java.net.*;

class FTPServer {
    public static void main(String args[]) throws Exception {
        
        ControlConnection controlCon = new ControlConnection();
        
//        ServerSocket soc = new ServerSocket(21);
//        System.out.println("FTP Server Started on Port Number 21");
//        
//        while(true) {
//            System.out.println("Waiting for Connection ...");
//            TransferFile t = new TransferFile(soc.accept());   
//        }
    }
}