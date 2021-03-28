import java.net.*;
import java.io.*;
import tcpclient.TCPClient;
import java.lang.Runnable;

public class MyRunnable implements Runnable {
    Socket userSocket;

    public MyRunnable(Socket s){
        userSocket = s;
    }

    public void run(){
        byte[] fromClientBuffer = new byte[512];
        String hostname = null;
        int port = 0;
        String toServer = null;
        String h200 = "HTTP/1.1 200 OK \r\n\r\n";
        String h404 = "HTTP/1.1 404 Not Found";
        String h400 = "HTTP/1.1 400 Bad Request";
        try{
                userSocket.setSoTimeout(10000);
                StringBuilder s = new StringBuilder();
                int fromClientLength;
                while((fromClientLength = userSocket.getInputStream().read(fromClientBuffer)) != -1){
                    s.append(new String(fromClientBuffer, 0, fromClientLength));
                    if(s.toString().contains("\r\n")){ 
                        break; 
                    }
                }

                String[] sp = s.toString().split("[ ?&=]", 30);

                for(int i = 0; i < sp.length; i++){
                    if(sp[i].equals("hostname")){
                        hostname = sp[i+1];
                    }
                    if(sp[i].equals("port")){
                        port = Integer.parseInt(sp[i+1]);
                    }
                    if(sp[i].equals("string")){
                        toServer = sp[i+1];
                    }
                }

                String m = null;
                
                if(sp[0].equals("GET") && sp[1].equals("/ask") ){
                    try{
                        m = h200 + TCPClient.askServer(hostname, port, toServer);
                        userSocket.getOutputStream().write(m.getBytes());  
                        userSocket.close();
                    }
                    catch(Exception e){
                        userSocket.getOutputStream().write(h404.getBytes());
                        userSocket.close();
                    }
                }
                else{
                    userSocket.getOutputStream().write(h400.getBytes());
                }
                userSocket.close();
        }
        catch(Exception e)
        {
            
        }

    }
}