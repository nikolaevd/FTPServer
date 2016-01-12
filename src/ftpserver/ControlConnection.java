
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ControlConnection {
     
    private File file;
    private String dataAddress;
    private int dataPort;
    private final int PORT = 21;
        
    public void start() throws IOException{
        
        try(ServerSocket s = new ServerSocket(PORT)){
            
            System.out.println("FTP Server Has Started on Port Number " + PORT + " ...");
            
            try(Socket incoming = s.accept()){
                
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
                
                PrintWriter out = new PrintWriter(outStream, true);
                
                out.println("FTP Client Connected ...");
                out.println("Enter BYE to exit");
                
                try(Scanner in = new Scanner(inStream)){
                    
                    boolean done = false;
                    while(!done && in.hasNextLine()){
                        
                        String line = in.nextLine();
                        
                        String command = parseCommand(line);
                        String argumnet = parseArgument(line);
                        
                        switch (command.trim()) {
                            case "BYE":
                                done = true;
                                out.println("426 Connection closed");
                                break;
                            case "USER":
                                if(!argumnet.trim().equals("anonymous")){
                                    out.println("530 Login incorrect");
                                }
                                else{
                                    out.println("230 User anonymous logged in");
                                }   break;
                            case "EPRT":
                                String[] args = eprtHandler(argumnet);
                                dataAddress = args[2];
                                dataPort = Integer.parseInt(args[3]);
                                out.println("200 EPRT command successful");
                                break;
                            case "STOR":
                                file = new File(argumnet);
                                if(file.exists()){
                                    out.println("500 File already exists");
                                }
                                else{
                                    out.println("150 Accepted data connection");
                                    Runnable rStor = new DataConnection("send", dataAddress, dataPort, file);
                                    Thread tStor = new Thread(rStor);
                                    tStor.start();
                                    out.println("226 Successfully transferred");
                                }   break;
                            case "RETR":
                                file = new File(argumnet);
                                if(file.exists()){
                                    out.println("150 Accepted data connection");
                                    Runnable rRetr = new DataConnection("get", dataAddress, dataPort, file);
                                    Thread tRetr = new Thread(rRetr);
                                    tRetr.start();
                                    out.println("226 Successfully transferred");
                                }
                                else{
                                    out.println("550 File Not Found");
                                }
                                break;
                            default:
                                out.println("500 Unrecognized command");
                                break;
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
    
    private String[] eprtHandler(String str){
        String[] erpt = str.split("\\|");
        return erpt; 
    }
}
