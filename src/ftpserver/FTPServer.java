
package ftpserver;

class FTPServer {
    public static void main(String args[]) {
        
        try{
            ControlConnection controlConnection = new ControlConnection();
            controlConnection.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }  
        
    }
}