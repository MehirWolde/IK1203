package task3;

import java.net.*;
import java.io.*;
import tcpclient.TCPClient;


public class HTTPAsk {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]));
        byte[] fromClientBuffer = new byte[512];
        String hostname = null;
        int port = 0;
        String toServer = null;
        String h200 = "HTTP/1.1 200 OK \r\n\r\n";
        String h404 = "HTTP/1.1 404 Not Found";
        String h400 = "HTTP/1.1 400 Bad Request";
        try{
            while(true){
                Socket connectionSocket = socket.accept();
                connectionSocket.setSoTimeout(5000);
                InputStream in = connectionSocket.getInputStream();
                OutputStream out = connectionSocket.getOutputStream();
                StringBuilder s = new StringBuilder();
                int fromClientLength;
                while((fromClientLength = in.read(fromClientBuffer)) != -1){
                    s.append(new String(fromClientBuffer, 0, fromClientLength));
                    if(s.toString().contains("\r\n")){ 
                        break; 
                    }
                }
                

                String[] sp = s.toString().split("[ ?&=]");
                
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
                
                int i;
                for(i = 0; i < sp.length; i++)
                {
                    if(sp[i].equals("HTTP/1.1"))
                    {
                        System.out.println(i);
                        break;
                    }
                }
                

                if(sp[0].equals("GET") && sp[1].equals("/ask")){
                    try{
                        m = h200 + TCPClient.askServer(hostname, port, toServer);
                        out.write(m.getBytes());  
                        connectionSocket.close();
                    }
                    catch(Exception e){
                        out.write(h404.getBytes());
                        connectionSocket.close();
                    }
                }
                else{
                    out.write(h400.getBytes());
                }
                connectionSocket.close();
            }
        }
        catch(Exception e)
        {
            
        }
    }
    
}