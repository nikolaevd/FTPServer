
package ftpserver;

class FTPServer {
    public static void main(String args[]) throws Exception {
        
        // создаем объект класса, отвечающий за управляющее соединение
        new ControlConnection();   
        
    }
}