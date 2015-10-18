
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Handling {
    
    final int CONTROL_CONNECTION_PORT = 21;
    final int DATA_CONNECTION_PORT = 20;
    
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
                        
                        String command = parseCommand(line);
                        String argumnet = parseArgument(line);
                        
                        if(command.trim().equals("BYE")){
                            done = true;
                            out.println("> Connection closed.");
                        }
                        else if(command.trim().equals("USER") && argumnet.trim().equals("anonymous")){
                            out.println("230 User anonymous logged in.");
                        }
                        else if(command.trim().equals("GET")){
                            out.println("> GET Command Received...");
                        }
                        else if(command.trim().equals("SEND")){
                            out.println("> SEND Command Received...");
                        }
                        else if(command.trim().equals("EPRT")){
                            out.println("> EPRT Command Received...");
                            String[] test = erptHandler(argumnet);
                        }
                        else if(command.trim().equals("PORT")){
                            out.println("> PORT Command Received...");
                        }
                        else{
                            out.println("> Unrecognized command.");
                        }
                        
                    }
                }
            }
        }
    }
    
    private String parseCommand(String str){
        String command = "";
        
        for(int i = 0; i < str.length(); i++){
            if(str.substring(i, i+1).equals(" ")){
                break;
            }
            command += str.substring(i, i+1);
        }
        return command;
    }
    
    private String parseArgument(String str){
        String argument = "";
        
        for(int i = 0; i < str.length(); i++){
            if(str.substring(i, i+1).equals(" ")){
                argument = str.substring(i+1);
                break;
            }        
        }
        return argument;
    }
    
    private String[] erptHandler(String str){
        String[] erpt = str.split("\\|");
        return erpt; 
    }
}
