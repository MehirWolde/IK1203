import java.net.*;
import java.io.*;
import tcpclient.TCPClient;


public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException {
        try{
            ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]));
            while(true){
                Socket connectionSocket = socket.accept();
                MyRunnable run = new MyRunnable(connectionSocket);
                Thread now = new Thread(run);
                now.start();
            }
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }
    
}