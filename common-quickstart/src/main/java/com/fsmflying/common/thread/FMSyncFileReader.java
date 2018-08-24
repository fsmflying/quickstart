package com.fsmflying.common.thread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件同步读取类，可在多个线程中同步读取文本文件．
 *
 * @author FangMing
 */
public class FMSyncFileReader implements FMSyncReader {

    private String prefix = "temp";
    private String suffix = "tmp";
    private int nextFileIndex = 0;
    private String dirname = ".\\";
    private int numOfRowsPerRead = 100000;
    private static final int NUM_OF_ROWS_PER_READ = 100000;

    private BufferedReader reader;

    public FMSyncFileReader(BufferedReader reader) {
        this.reader = reader;
    }

    public FMSyncFileReader(BufferedReader reader, String prefix, String suffix) {
        this(reader);
        this.prefix = prefix;
        this.suffix = suffix;

    }

    public FMSyncFileReader(BufferedReader reader, String prefix, String suffix, int numOfRowsPerRead) {
        this(reader);
        this.prefix = prefix;
        this.suffix = suffix;
        this.numOfRowsPerRead = numOfRowsPerRead;
    }

    public FMSyncFileReader(String fullFileName) throws FileNotFoundException {
        this(fullFileName, NUM_OF_ROWS_PER_READ);
    }

    public FMSyncFileReader(String fullFileName, String prefix, String suffix) throws FileNotFoundException {
        this(fullFileName, prefix, suffix, NUM_OF_ROWS_PER_READ);
    }

    public FMSyncFileReader(String fullFileName, String prefix, String suffix, int numOfRowsPerRead)
            throws FileNotFoundException {
        this(new BufferedReader(new FileReader(fullFileName)));
        this.prefix = prefix;
        this.suffix = suffix;
        this.numOfRowsPerRead = numOfRowsPerRead;
    }

    public FMSyncFileReader(InputStream input, String prefix, String suffix, int numOfRowsPerRead)
            throws FileNotFoundException {
        this(new BufferedReader(new InputStreamReader(input)));
        this.prefix = prefix;
        this.suffix = suffix;
        this.numOfRowsPerRead = numOfRowsPerRead;
    }

    public FMSyncFileReader(String fullFileName, int numOfRowsPerRead) throws FileNotFoundException {
        this(new BufferedReader(new FileReader(fullFileName)));
//		this(new BufferedReader(new InputStreamReader(new FileInputStream(fullFileName),Charset.forName("UTF-8"))));
        this.numOfRowsPerRead = numOfRowsPerRead;
        if (fullFileName != null && !fullFileName.equals("")) {
            if (fullFileName.lastIndexOf("/") >= 0 || fullFileName.lastIndexOf("\\") >= 0) {
                fullFileName = fullFileName.replace('/', '\\');
                dirname = fullFileName.substring(0, fullFileName.lastIndexOf("\\") + 1);
                fullFileName = fullFileName.substring(fullFileName.lastIndexOf("\\") + 1);
            }
            int lastIndex = fullFileName.lastIndexOf(".");
            if (lastIndex >= 0) {
                this.prefix = fullFileName.substring(0, lastIndex);
                this.suffix = fullFileName.substring(lastIndex + 1);
            }
        }
    }

    public FMSyncFileReader(String fullFileName, String charset, int numOfRowsPerRead) throws FileNotFoundException {
//		this(new BufferedReader(new FileReader(fullFileName)));
        this(new BufferedReader(new InputStreamReader(new FileInputStream(fullFileName), Charset.forName(charset))));
        this.numOfRowsPerRead = numOfRowsPerRead;
        if (fullFileName != null && !fullFileName.equals("")) {
            if (fullFileName.lastIndexOf("/") >= 0 || fullFileName.lastIndexOf("\\") >= 0) {
                fullFileName = fullFileName.replace('/', '\\');
                dirname = fullFileName.substring(0, fullFileName.lastIndexOf("\\") + 1);
                fullFileName = fullFileName.substring(fullFileName.lastIndexOf("\\") + 1);
            }
            int lastIndex = fullFileName.lastIndexOf(".");
            if (lastIndex >= 0) {
                this.prefix = fullFileName.substring(0, lastIndex);
                this.suffix = fullFileName.substring(lastIndex + 1);
            }
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public FMSyncFileReader(BufferedReader reader, String fullFileName) {
        this.reader = reader;
        if (fullFileName != null && !fullFileName.equals("")) {
            if (fullFileName.lastIndexOf("/") >= 0 || fullFileName.lastIndexOf("\\") >= 0) {
                fullFileName = fullFileName.replace('/', '\\');
                dirname = fullFileName.substring(0, fullFileName.lastIndexOf("\\") + 1);
                fullFileName = fullFileName.substring(fullFileName.lastIndexOf("\\") + 1);
                // System.out.println("dirName:" + dirname);
            }
            int lastIndex = fullFileName.lastIndexOf(".");
            if (lastIndex >= 0) {
                this.prefix = fullFileName.substring(0, lastIndex);
                this.suffix = fullFileName.substring(lastIndex + 1);
                // System.out.println("originalFileName:" +
                // originalFileName);
                // System.out.println("prefix:" + this.prefix);
                // System.out.println("suffix:" + this.suffix);
            }
        }
    }

    public synchronized String getNextFileName() {
        ++nextFileIndex;
        String partName = ((nextFileIndex) < 10 ? ("0" + nextFileIndex) : ("" + nextFileIndex));
        return (prefix + ".part" + partName + "." + suffix);
    }

    public String getDirName() {
        return this.dirname;
    }

    public synchronized List<String> read(int rowCount) throws IOException {
        int i = 0;
        List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (i < rowCount && (!"".equals(line)) && (null != line)) {
            list.add(line);
            i++;
            line = reader.readLine();
        }
        return list;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fsmflying.common.thread.FMSyncReader#readWithFileName(int)
     */
    @Override
    public synchronized Map<String, List<String>> readWithFileName(int rowCount) throws IOException {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        int i = 0;
        List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (i < rowCount && (!"".equals(line)) && (!"\n".equals(line)) && (null != line)) {
            list.add(line);
            i++;
            line = reader.readLine();
        }
        // System.out.println("nextFileIndex:" + nextFileIndex);
        map.put("data", list);

        List<String> name = new ArrayList<String>();
        name.add(this.getNextFileName());
        map.put("name", name);

        return map;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fsmflying.common.thread.FMSyncReader#readWithFileName(int)
     */
    @Override
    public synchronized Map<String, List<String>> readWithFileName() throws IOException {
        return readWithFileName(this.numOfRowsPerRead);
    }

    @Override
    public void close() throws IOException {
        if (this.reader != null)
            this.reader.close();
    }

}
