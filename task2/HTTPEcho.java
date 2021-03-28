import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class HTTPEcho {

    private static int BUFFERSIZE = 1024;
    public static void main( String[] args) throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0])); 
        byte[] fromClientBuffer= new byte[BUFFERSIZE];

        try{
            while(true) { 
                Socket connectionSocket = welcomeSocket.accept(); 
                connectionSocket.setSoTimeout(5000); 
                StringBuilder s = new StringBuilder("HTTP/1.1 200 OK \r\n\r\n");
                int fromClientLength;
                while((fromClientLength = connectionSocket.getInputStream().read(fromClientBuffer)) != -1){
                    s.append(new String(fromClientBuffer, 0,fromClientLength));
                    if(s.toString().contains("\r\n")){ break; }
                }
                byte[] toClientBuffer = s.toString().getBytes();
                connectionSocket.getOutputStream().write(toClientBuffer); 
                connectionSocket.close();
            }
        }
        catch(SocketTimeoutException e){}
    }


}
