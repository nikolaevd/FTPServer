
package ftpserver;

import java.net.*;
import java.io.*;

// класс реализует соединение для передачи данных
public class DataConnection implements Runnable{
    
    private final String typeOfOperation;
    private final String address;
    private final int port;
    private final File file;

    // конструктор класса принимает параметр "тип операции" (операция на получение или передачу файла)
    // IP-адрес, порт и имя файла
    DataConnection(String typeOfOperation, String address, int port, File file){
        
        this.typeOfOperation = typeOfOperation;
        this.address = address;
        this.port = port;
        this.file = file;;
        
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
        try(Socket socket = new Socket(address, port)){
            // создаем поток для чтения данных из сокета
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());            
            
            // создаем поток для записи файлов на сервере
            try(FileOutputStream fileOutput = new FileOutputStream(file)){

                int tmp;     
            
                // пока в потоке есть байты, мы продолжаем их считывать
                // признаком конца файла является значение -1
                do{
                    tmp = dataInput.readByte();
                    fileOutput.write(tmp);
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
        
        try(Socket socket = new Socket(address, port)){
            // создаем поток для записи данных и подключаем к нему исходящий поток сокета
            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
            
            // читаем файл
            try(FileReader fileReader = new FileReader(file)){
                // чтобы не читать каждую строку из файла вручную, подключаем fileReader к потоку BufferedReader
                BufferedReader bufferedReader = new BufferedReader(fileReader);
           
            int tmp;
            
            // пока в потоке есть байты, мы продолжаем их считывать
            // признаком конца файла является значение -1
            do{
                tmp = bufferedReader.read();
                dataOutput.writeByte(tmp);
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
