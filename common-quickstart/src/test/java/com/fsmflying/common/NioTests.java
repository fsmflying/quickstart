package com.fsmflying.common;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTests {

    /**
     * 使用InputStream读取
     */
    public void startInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("d:\\private\\csdn.24000.txt");
            byte[] bytes = new byte[1024];
            int numForRead = inputStream.read(bytes);
            while (numForRead != -1) {
                for (byte b : bytes) {
                    System.out.print((char) b);
                }
                numForRead = inputStream.read(bytes);
            }

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用FileChannel读取
     */
    public void startFileChannel() {
        FileChannel fileChannel = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\private\\csdn.24000.txt", "rw");
            fileChannel = randomAccessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int numOfRead = fileChannel.read(byteBuffer);
            while (numOfRead != -1) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }
                byteBuffer.clear();
                numOfRead = fileChannel.read(byteBuffer);
            }
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileChannel != null && fileChannel.isOpen()) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
