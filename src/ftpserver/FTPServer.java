
package ftpserver;

class FTPServer {
    public static void main(String args[]) throws Exception {
        
        // создаем экземпляр класса, отвечающий за организацию управляющего соединения
        new ControlConnection();   
        
    }
}