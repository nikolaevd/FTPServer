
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
                    System.out.println(tmp);
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
        
        try(Socket s = new Socket(address, port)){
            DataOutputStream output = new DataOutputStream(s.getOutputStream());
            
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
           
            int tmp;
            
            do{
                tmp = bufferedReader.read();
                output.writeByte(tmp);
                System.out.println(tmp);
            }
            while(tmp != -1);
            
            System.out.println("File Has Sended");
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        
    }
   
}
