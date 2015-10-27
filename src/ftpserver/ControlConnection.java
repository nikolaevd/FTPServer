
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ControlConnection {
     
    private File file;
    private String dataAddress;
    private int dataPort;
    private final int PORT = 21;
        
    ControlConnection() throws IOException{
        
        // создаем серверный сокет, позволяющий программам устанавливать с ним соединение для обмена данными по сети
        try(ServerSocket s = new ServerSocket(PORT)){
            
            System.out.println("FTP Server Has Started on Port Number " + PORT + " ...");
            
            // предписываем сокету ожидать подключение на указанном порту (21)
            try(Socket incoming = s.accept()){
                
                // потоки in и out используются для чтения и записи данных из сокета
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();
                
                // объект используется для отправки текстовых команд в исходящий поток outStream
                PrintWriter out = new PrintWriter(outStream, true);
                
                out.println("FTP Client Connected ...");
                out.println("Enter BYE to exit");
                
                // Scanner используется для чтения команд из входящего потока inStream
                try(Scanner in = new Scanner(inStream)){
                    
                    // в данном цикле мы считываем и выводим в консоль текстовые строки из входящего потока inStream
                    boolean done = false;
                    while(!done && in.hasNextLine()){
                        
                        String line = in.nextLine();
                        if(!line.equals("RETR HaxLogs.txt")){
                            out.println(line);
                        }
                        
                        String command = parseCommand(line);
                        String argumnet = parseArgument(line);
                        
                        // в зависимости от полученной команды, выполняем определенные действия
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
                                out.println("150 Accepted data connection");
                                file = new File(argumnet);
                                if(file.exists()){
                                    out.println("File Already Exists");
                                }
                                else{
                                    Runnable rStor = new DataConnection("send", dataAddress, dataPort, file);
                                    Thread tStor = new Thread(rStor);
                                    tStor.start();
                                }   break;
                            case "RETR":
                                out.println("150 Accepted data connection");
                                file = new File(argumnet);
                                file = file.getAbsoluteFile();
                                Runnable rRetr = new DataConnection("get", dataAddress, dataPort, file);
                                Thread tRetr = new Thread(rRetr);
                                tRetr.start();
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
    
    // метод приниает на вход строку (команда + аргументы) и отделяют команду от остального текста
    // текст команды присваевается переменной command
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
    
    // метод отделяет аргументы от команды
    // аргументы записываются в переменную argument
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
    
    // метод расщепляет аргументы команды eprt
    // и записывает каждый в ячейку строкового массива
    private String[] eprtHandler(String str){
        String[] erpt = str.split("\\|");
        return erpt; 
    }
}
