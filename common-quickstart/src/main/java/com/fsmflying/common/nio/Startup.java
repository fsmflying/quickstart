package com.fsmflying.common.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;

import org.junit.Test;

public class Startup {
    public static void main(String[] args) throws IOException {

        test_01_readFile();
    }

    public static void test_01_readFile() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("d:\\private\\csdn.10.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            // System.out.println("{mark:" + buf.mark() + ",position:" +
            // buf.position() + ",limit:" + buf.limit()
            // + ",capacity:" + buf.capacity() + "}");
            System.out.println("Read " + bytesRead);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }

    /**
     * 通过Buffer复制文本文件内容
     *
     * @throws IOException
     */
    @Test
    public void test_00() throws IOException {
        //mode must be one of "r", "rw", "rws", or "rwd"
        RandomAccessFile aFile = new RandomAccessFile("d:\\private\\csdn.10.txt", "r");
        RandomAccessFile bFile = new RandomAccessFile("d:\\private\\csdn.10b.txt", "rw");

        FileChannel ach = aFile.getChannel();
        FileChannel bch = bFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);
//		ByteBuffer buf2 = ByteBuffer.allocate(48);
        int bytesRead = ach.read(buf), bytesWrite = 0;
        char ch;
        while (bytesRead > 0) {
//			System.out.println("{mark:" + buf.mark() + ",position:" + buf.position() + ",limit:" + buf.limit()
//					+ ",capacity:" + buf.capacity() + "}");
            System.out.println("Read " + bytesRead);

            buf.flip();
            buf.mark();

            while (buf.hasRemaining()) {
                ch = (char) buf.get();
                System.out.println(ch);
                //bFile.write(ch);
            }

            buf.reset();
            bytesWrite = bch.write(buf);

            buf.clear();
            bytesRead = ach.read(buf);
        }
        // ach.close();
        // bch.close();
        aFile.close();
        bFile.close();

    }

    @Test
    public void test_03_ScatterAndGatter() {

    }

    @Test
    public void test_04_Selector() throws IOException {
        Selector selector = Selector.open();

    }

}
