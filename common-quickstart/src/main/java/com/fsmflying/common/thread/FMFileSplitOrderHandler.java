package com.fsmflying.common.thread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FMFileSplitOrderHandler extends FMFileSplitHandler {

    public FMFileSplitOrderHandler(int handleRowCount, FMSyncFileReader reader) {
        super(handleRowCount, reader);
    }

    @Override
    public void run() {

        if (this.reader != null) {
            try {
                Map<String, List<String>> map = this.reader.readWithFileName(handleRowCount);
                while (map != null && map.containsKey("name") && map.containsKey("data") && map.get("data").size() > 0) {

                    List<String> list = map.get("data");
                    // 注意，文件名称与数据读取是在同一个同步块中生成的，所以生成文件的数据,应该与原始文件中数据顺序一致，
                    String saveFileName = reader.getDirName() + map.get("name").get(0);
                    BufferedWriter writer01 = new BufferedWriter(//
                            new FileWriter(saveFileName));
                    for (String s : list) {
                        writer01.write(s + "\n");
                    }
                    writer01.flush();
                    writer01.close();
                    map = this.reader.readWithFileName(handleRowCount);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
