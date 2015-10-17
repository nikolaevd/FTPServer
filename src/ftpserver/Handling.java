
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Handling {
    
    final int CONTROL_CONNECTION_PORT = 21;
    
    public void createServer() throws IOException {
    
        try(ServerSocket s = new ServerSocket(CONTROL_CONNECTION_PORT)){
            
            System.out.println("> FTP Server Started on Port Number " + CONTROL_CONNECTION_PORT);
            
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
                        out.println("> " + line);
                        if(line.trim().equals("BYE")){
                            done = true;
                            out.println("> Connection closed.");
                        }
                        else if(line.trim().equals("USER anonymous")){
                            out.println("230 User anonymous logged in.");
                        }
                        else if(line.trim().equals("GET")){
                            out.println("> GET Command Received...");
                        }
                        else if(line.trim().equals("SEND")){
                            out.println("> SEND Command Received...");
                        }
                        else if(line.trim().equals("PORT")){
                            out.println("> PORT Command Received...");
                        }
                        else{
                            out.println("Unrecognized command.");
                        }
                        
                    }
                }
            }
        }
    }
}
