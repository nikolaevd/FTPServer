
package ftpserver;

import java.net.*;
import java.io.*;

// класс реализует соединение для передачи данных
public class DataConnection implements Runnable{
    
    private final String typeOfOperation;
    private final String address;
    private final int port;
    private final File file;

    DataConnection(String typeOfOperation, String address, int port, File file){
        this.typeOfOperation = typeOfOperation;
        this.address = address;
        this.port = port;
        this.file = file;
    }
    
    @Override
    public void run(){
        
        if(typeOfOperation.equals("send")){
            recieveFile();
        }        
    }
    
    private void recieveFile(){
        
        try(Socket s = new Socket(address, port)){
            DataInputStream input = new DataInputStream(s.getInputStream());            
            System.out.println("Data Connection Has Started ...");   
            
            try(FileWriter f = new FileWriter(file)){

                byte tmp;     
            
                do{
                    tmp = input.readByte();
                    //System.out.println(tmp);
                    f.write(tmp);
                }
                while(tmp != 0);    
            }
            
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
    
    private void sendFile(){
        
    }
   
}
