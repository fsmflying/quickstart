package com.fsmflying.common.thread;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程文本文件分割器
 *
 * @author FangMing
 */
public abstract class FMSyncFileSplitHandler {

    protected String originalFileName;
    int numOfRowsPerFile;
    protected long totalRowCount = 0;
    protected String prefix = "temp";
    protected String suffix = "tmp";
    // private int nextFileIndex = 0;
    protected String dirname = ".\\";
    protected int numberOfFiles = 1;

    public FMSyncFileSplitHandler(String originalFileName) {
        super();
        File originalFile = new File(originalFileName);
        if (!originalFile.isFile())
            throw new IllegalArgumentException("The '[" + originalFileName + "]' does not exists !");

        this.originalFileName = originalFileName;

        if (originalFileName != null && !originalFileName.equals("")) {
            if (originalFileName.lastIndexOf("/") >= 0 || originalFileName.lastIndexOf("\\") >= 0) {
                originalFileName = originalFileName.replace('/', '\\');
                this.dirname = originalFileName.substring(0, originalFileName.lastIndexOf("\\") + 1);
                originalFileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);
            }
            int lastIndex = originalFileName.lastIndexOf(".");
            if (lastIndex >= 0) {
                this.prefix = originalFileName.substring(0, lastIndex);
                this.suffix = originalFileName.substring(lastIndex + 1);
            }
        }
    }

    public void split() throws InterruptedException, IOException {
        this.calculateSplitCount();
        if (this.totalRowCount == 0)
            return;
        this.calculateSplitProperties();
        Reader reader01 = new InputStreamReader(new FileInputStream(this.originalFileName));
        BufferedReader reader03 = new BufferedReader(reader01);
        FMSyncFileReader reader04 = new FMSyncFileReader(reader03, this.originalFileName);

        ExecutorService es01 = Executors.newCachedThreadPool();
        System.out.println("numberOfFiles:" + numberOfFiles);
        for (int i = 0; i < numberOfFiles && i < 100; i++) {
            System.out.println("read lines count:" + numOfRowsPerFile);
            // es01.submit(new FileSplitHandler((int) numOfRowsPerFile,
            // reader04));//按文件名排序生成的文件顺序不一定与原始文件中一致
            es01.submit(new FMFileSplitOrderHandler((int) numOfRowsPerFile, reader04));
        }
        es01.shutdown();
        while (true) {
            System.out.println("=====check======");
            // if (es01.isShutdown()) {
            // System.out.println("===============isShutdown===========================");
            // break;
            // }
            if (es01.isTerminated()) {
                System.out.println("===============finished===========================");
                if (reader01 != null)
                    try {
                        reader01.close();
                        reader03.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
            }
            Thread.sleep(100);
        }
    }

    public void calculateSplitCount() throws IOException {
        BufferedInputStream bis01 = new BufferedInputStream(new FileInputStream(this.originalFileName));
        long rowCount = 0;
        byte[] c = new byte[1024 * 1024];
        int readChars = 0;
        // long start = System.currentTimeMillis();
        while ((readChars = bis01.read(c)) != -1) {
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n') {
                    ++rowCount;
                }
            }
        }
        bis01.close();
        // long end = System.currentTimeMillis();
        // System.out.println("文件总行数：" + rowCount);
        // System.out.println("总花费时间(毫秒)：" + (end - start));
        this.totalRowCount = rowCount;

        // return totalRowCount;
    }

    public abstract void calculateSplitProperties();

}