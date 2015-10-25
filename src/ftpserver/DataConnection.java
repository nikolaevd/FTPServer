
package ftpserver;

import java.net.*;
import java.io.*;

// класс реализует соединение для передачи данных
public class DataConnection implements Runnable{
    
    private final String typeOfOperation;
    private final String address;
    private final int port;
    private final File file;

    // конструктор класса принимает параметр "тип операции" (получить или передать файл),
    // IP-адрес, порт и имя файла
    DataConnection(String typeOfOperation, String address, int port, File file){
        
        this.typeOfOperation = typeOfOperation;
        this.address = address;
        this.port = port;
        this.file = file;
        
    }
    
    // в этом методе размещен код для нового потока
    @Override
    public void run(){
        
        if(typeOfOperation.equals("send")){
            recieveFile();
        }
        if(typeOfOperation.equals("get")){
            sendFile();
        }
        
    }
    
    // метод реализует загрузку файла с клиента на сервер
    private void recieveFile(){
        
        // создаем сокет на указанном адресе
        try(Socket s = new Socket(address, port)){
            // создаем поток для чтения данных из сокета
            DataInputStream input = new DataInputStream(s.getInputStream());            
            System.out.println("Data Connection Has Started ...");   
            
            // создаем поток для записи файлов на сервере
            try(FileOutputStream f = new FileOutputStream(file)){

                int tmp;     
            
                // пока в потоке есть байты, мы продолжаем их считывать
                // признаком конца файла является значение -1
                do{
                    tmp = input.readByte();
                    //System.out.println(tmp);
                    f.write(tmp);
                }
                while(tmp != -1);    
            }
            
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
    
    // метод реализует передачу данных с сервера на клиент
    private void sendFile(){
        
        // создаем сокет на уканазонном адресе
        try(Socket s = new Socket(address, port)){
            // создаем поток для записи данных в сокет (с сервера на клиент)
            DataOutputStream output = new DataOutputStream(s.getOutputStream());            
            System.out.println("Data Connection Has Started ...");
            
            // создаем поток для чтения данных из файла
            try(FileInputStream f = new FileInputStream(file)){
                
                int tmp;
                
                // читаем байты из файла и записываем их в исходяший поток сокета
                // т.е., передаем данные с сервера на клиент
                // признаком конца файла является -1
                do{
                    tmp = f.read();
                    System.out.println(tmp);
                    output.writeByte(tmp);
                }
                while(tmp != -1);      
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        catch(IOException ex){
            ex.printStackTrace();
        }
        
    }
   
}
