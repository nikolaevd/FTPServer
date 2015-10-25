
package ftpserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ControlConnection {
     
    private File file;
    private String dataAddress;
    private int dataPort;
    private final int PORT = 21;
        
    ControlConnection() throws IOException {
        
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
                        out.println(line);
                        
                        String command = parseCommand(line);
                        String argumnet = parseArgument(line);
                        
                        // распознаем какую команду мы получили
                        // в зависимости от команды, выполняем определенные действия
                        switch (command.trim()) {
                            case "BYE":
                                done = true;
                                out.println("Connection closed");
                                break;
                            case "USER":
                                if(!argumnet.trim().equals("anonymous")){
                                    // неверное имя пользователя
                                    out.println("530 Login incorrect");
                                }
                                else{
                                    // авторизация успешно пройдена
                                    out.println("230 User anonymous logged in");
                                }   break;
                            case "EPRT":
                                out.println("200 EPRT command successful");
                                // получаем данные из аргументов команды EPRT
                                String[] args = eprtHandler(argumnet);
                                dataAddress = args[2];
                                dataPort = Integer.parseInt(args[3]);
                                break;
                            case "STOR":
                                //  команда предписывает загрузить файл на сервер
                                out.println("150 Accepted data connection");
                                file = new File(argumnet);
                                // проверяем, существует ли уже файл с таким именем
                                if(file.exists()){
                                    out.println("File Already Exists");
                                }
                                // если нет, то начинаем загрузку
                                else{
                                    out.println("Send File");
                                    // устанавливаем в отдельном потоке соединение для передачи данных
                                    Runnable r = new DataConnection("send", dataAddress, dataPort, file);
                                    Thread t = new Thread(r);
                                    t.start();
                                } break;
                            case "RETR":
                                // команда предписывает скачать файл с сервера
                                out.println("150 OK to get data");
                                file = new File(argumnet);
                                out.println("Get File");
                                Runnable r = new DataConnection("get", dataAddress, dataPort, file);
                                Thread t = new Thread(r);
                                t.start();
                                break;
                            default:
                                // полученная команда нераспознана 
                            out.println("Unrecognized command");
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
