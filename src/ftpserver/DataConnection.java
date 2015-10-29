
package ftpserver;

import java.net.*;
import java.io.*;

public class DataConnection implements Runnable{
    
    private final String typeOfOperation;
    private final String address;
    private final int port;
    private final File file;

    DataConnection(String typeOfOperation, String address, int port, File file){
        
        this.typeOfOperation = typeOfOperation;
        this.address = address;
        this.port = port;
        this.file = file;;
        
    }
    
    @Override
    public void run(){
        
        if(typeOfOperation.equals("send")){
            recieveFile();
        }
        if(typeOfOperation.equals("get")){
            sendFile();
        }
        
    }
    
    private void recieveFile(){
        
        try(Socket socket = new Socket(address, port)){
            
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());
            BufferedInputStream bufferedInput = new BufferedInputStream(dataInput);
            
            try(FileOutputStream fileOutput = new FileOutputStream(file)){

                int tmp;     
                
                do{
                    tmp = bufferedInput.read();
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
    
    private void sendFile(){
        
        try(Socket socket = new Socket(address, port)){
            
            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
            
            try(FileReader fileReader = new FileReader(file)){

                BufferedReader bufferedReader = new BufferedReader(fileReader);
           
            int tmp;

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
