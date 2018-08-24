package com.fsmflying.common.thread;

import java.io.IOException;

public class FMSyncFileSplitHandlerForFixedFiles extends FMSyncFileSplitHandler {

    public FMSyncFileSplitHandlerForFixedFiles(String originalFileName, int numberOfFiles) {
        super(originalFileName);
        this.numberOfFiles = numberOfFiles;

    }

    @Override
    public void calculateSplitProperties() {
        if (this.totalRowCount == 0) {
            try {
                this.calculateSplitCount();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.totalRowCount % this.numberOfFiles == 0)
            this.numOfRowsPerFile = (int) (this.totalRowCount / this.numberOfFiles);
        else
            this.numOfRowsPerFile = (int) (this.totalRowCount / this.numberOfFiles + 1);
    }

}
