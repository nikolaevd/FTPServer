
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ControlConnection {
     
    private File f;
    private String dataAddress;
    private int dataPort;
    private final int PORT = 21;
        
    ControlConnection() throws IOException {
        
        // создаем серверный сокет, для возможности программ устанавливать соединение для обмена данными по сети
        // данное сетевое соединение предназначено для управляющего канала (канал для обмена командами)
        try(ServerSocket s = new ServerSocket(PORT)){
            
            System.out.println("FTP Server Has Started on Port Number " + PORT + " ...");
            
            // предписываем сокеты ожидать подключение на указанном порту (21)
            try(Socket incoming = s.accept()){
                
                // inStream - используется для чтения данных из сокета
                InputStream inStream = incoming.getInputStream();
                // outStram - используется для отправки данных через сокет
                OutputStream outStream = incoming.getOutputStream();
                
                // данный объект нужен для отправки текстовых (String) команд
                PrintWriter out = new PrintWriter(outStream, true);
                
                out.println("FTP Client Connected ...");
                out.println("Enter BYE to exit");
                
                // объект типа Scanner нужен для чтения текса (данных типа String) из потока inStream
                try(Scanner in = new Scanner(inStream)){
                    
                    // в данном цикле мы считываем и выводим в консоль текстовые строки из входящего потока inStream
                    boolean done = false;
                    while(!done && in.hasNextLine()){
                        
                        String line = in.nextLine();
                        out.println(line);
                        
                        String command = parseCommand(line);
                        String argumnet = parseArgument(line);
                        
                        // в данном ветвлении распознаем какую команду мы получили
                        // и в зависимости от команды, выполняем определенные действия
                        if(command.trim().equals("BYE")){
                            done = true;
                            out.println("Connection closed");
                        }
                        else if(command.trim().equals("USER")){
                            if(!argumnet.trim().equals("anonymous")){
                                // неверное имя пользователя
                                out.println("530 Login incorrect");
                            }
                            else{
                                // авторизация успешно пройдена
                                out.println("230 User anonymous logged in");
                            }
                        }
                        else if(command.trim().equals("EPRT")){
                            out.println("200 EPRT command successful.");
                            // получаем данные из аргументов команды EPRT
                            String[] args = eprtHandler(argumnet);
                            dataAddress = args[2];
                            dataPort = Integer.parseInt(args[3]); 
                        }
                        else if(command.trim().equals("STOR")){
                            //  команда предписывает загрузить файл на сервер
                            out.println("150 Accepted data connection");
                            f = new File(argumnet);
                            
                            // проверяем, существует ли уже файл с таким именем
                            if(f.exists()){
                                out.println("File Already Exists");
                            }
                            // если нет, то начинаем загрузку
                            else{
                                out.println("SendFile");
                                // устанавливаем в отдельном потоке соединение для передачи данных
                                Runnable r = new DataConnection(dataAddress, dataPort);
                                Thread t = new Thread(r);
                                t.start();
                            }      
                        }
                        else{
                            // полученная команда нераспознана 
                            out.println("Unrecognized command");
                        }
                        
                    }
                }
            }
        }
    }
    
    // метод приниает на вход строку (команда + аргументы) и отделяют команду от остльного
    // команда помещается в переменную command
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
    
    // метод предназначен расщепляет аргументы команды eprt
    // записывает каждый в ячейку строкового массива
    private String[] eprtHandler(String str){
        String[] erpt = str.split("\\|");
        return erpt; 
    }
}
