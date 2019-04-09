package com.fsmflying.netty.bio;

import java.io.*;
import java.net.Socket;
@SuppressWarnings("all")
public class BioClient {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader readerForKey = null;
        BufferedWriter writerForNet = null;
        BufferedReader readerForNet=null;
        try {
            socket = new Socket("localhost", 8080);
            readerForKey = new BufferedReader(new InputStreamReader(System.in));
            writerForNet = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            readerForNet = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = readerForKey.readLine();
            writerForNet.write(line);
            writerForNet.newLine();
            writerForNet.flush();
            System.out.println(line);
            String response = readerForNet.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (writerForNet != null) {
                try {
                    writerForNet.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writerForNet != null) {
                try {
                    readerForKey.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
}
