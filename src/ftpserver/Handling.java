
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Handling {
    
    final int SERVER_PORT = 10021;
    
    public void acceptConnection() throws IOException {
    
        try(ServerSocket s = new ServerSocket(SERVER_PORT)){
            
            System.out.println("> FTP Server Started on Port Number " + SERVER_PORT);
            
            try(Socket incoming = s.accept()){
                
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
                
                PrintWriter out = new PrintWriter(outStream, true);
                
                out.println("> FTP Client Connected...");
                out.println("> Enter BYE to exit.");
                
                try(Scanner in = new Scanner(inStream)){
                    
                    boolean done = false;
                    while(!done && in.hasNextLine()){
                        String line = in.nextLine();
                        if(line.trim().equals("BYE")){
                            done = true;
                            out.println("> Connection closed.");
                        }
                        else if(line.trim().equals("GET")){
                            out.println("\t> GET Command Received ...");
                        }
                        else if(line.trim().equals("SEND")){
                            out.println("\t> SEND Command Received ...");
                        }
                        else if(line.trim().equals("PORT")){
                            out.println("\t> PORT Command Received ...");
                        }
                        else if(line.trim().equals("DISCONNECT")){
                            out.println("\t> DISCONNECT Command Received ...");
                        }
                    }
                }
            }
        }
    }
}
