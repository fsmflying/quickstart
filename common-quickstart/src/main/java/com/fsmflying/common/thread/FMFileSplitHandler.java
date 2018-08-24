package com.fsmflying.common.thread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 文件拆分处理类,从指定的文件中读取指定行数的数据，并写入到一个文件中
 *
 * @author FangMing
 */
public class FMFileSplitHandler implements Runnable {
    protected int handleRowCount = 1024;
    protected FMSyncFileReader reader;

    public FMFileSplitHandler(int handleRowCount, FMSyncFileReader reader) {
        if (handleRowCount > 0 && handleRowCount % 10 == 0)
            this.handleRowCount = handleRowCount;
        else
            this.handleRowCount = ((handleRowCount / 10) + 1) * 10;
        this.reader = reader;
    }

    @Override
    public void run() {
        if (this.reader != null) {
            try {
                List<String> list = this.reader.read(handleRowCount);
                while (list != null && list.size() != 0) {
                    // 注意，文件名称与数据读取是在两个不同的同步块中生成的，所以不同生成文件的数据,不一定与原始文件中数据顺序一致，
                    // 意思就是,part01,文件不一定是原始数据的第一段;
                    // 如果要生成与原始读取数据一致的文件名与原始文件的数据顺序一致，可将生成文件名和读取数据在同一个同步块中进行即可
                    String saveFileName = reader.getDirName() + reader.getNextFileName();
                    BufferedWriter writer01 = new BufferedWriter(//
                            new FileWriter(saveFileName));

                    for (String s : list) {
                        // System.out.println("Thread[" +
                        // Thread.currentThread().getName() + "]:" + s);
                        writer01.write(s + "\n");
                    }
                    writer01.flush();
                    writer01.close();
                    list = this.reader.read((int) this.handleRowCount);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
