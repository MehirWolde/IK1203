/*
Author: Mehir Wolde
Date generated: 14-02-2021
Date edited: 15-02-2021
README: Program which creates a TCP clientSocket which is used to communicate between a server and client where you are 
given the option to send a message to the server or not.
*/

package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient {
    
    static int BUFFERSIZE = 1024;

    public static String askServer(String hostname, int port, String ToServer) throws IOException {
        
        if(ToServer == null)  return askServer(hostname, port); 
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(5000);
        InputStream inStream = clientSocket.getInputStream();
        byte[] fromClientBuffer = (ToServer + "\r\n").getBytes(StandardCharsets.UTF_8);
        byte[] fromServerBuffer = new byte[BUFFERSIZE];

        clientSocket.getOutputStream().write(fromClientBuffer);

        int fromServerLength;
        StringBuilder encodedBytes = new StringBuilder();
        try{
            while((fromServerLength = inStream.read(fromServerBuffer)) != -1){
            encodedBytes.append(new String(fromServerBuffer, 0, fromServerLength, "UTF-8"));
            }
        }
        catch(java.net.SocketTimeoutException e){}
        clientSocket.close();
        return encodedBytes.toString();
    }


    public static String askServer(String hostname, int port) throws IOException {
        Socket clientSocket = new Socket(hostname, port);
        InputStream inStream = clientSocket.getInputStream();
        clientSocket.setSoTimeout(5000);
        byte[] fromServerBuffer= new byte[BUFFERSIZE];
        int fromServerLength;
        StringBuilder encodedBytes = new StringBuilder();
        try{
            while((fromServerLength = inStream.read(fromServerBuffer)) != -1 && fromServerLength < 256){
            encodedBytes.append(new String(fromServerBuffer, 0, fromServerLength, "UTF-8"));
            }
        }
        catch(java.net.SocketTimeoutException e){}
        clientSocket.close();
        return encodedBytes.toString();

    }
}

