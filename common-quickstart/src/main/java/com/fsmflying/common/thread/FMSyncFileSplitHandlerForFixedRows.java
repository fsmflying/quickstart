package com.fsmflying.common.thread;

import java.io.IOException;

public class FMSyncFileSplitHandlerForFixedRows extends FMSyncFileSplitHandler {

	public FMSyncFileSplitHandlerForFixedRows(String originalFileName, int numOfRowsPerFile) {
		super(originalFileName);
		this.numOfRowsPerFile = numOfRowsPerFile;

	}

	@Override
	public void calculateSplitProperties() {
		if (this.totalRowCount ==0 ) {
			try {
				this.calculateSplitCount();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.totalRowCount % this.numOfRowsPerFile == 0)
			this.numberOfFiles = (int) (this.totalRowCount / this.numOfRowsPerFile);
		else
			this.numberOfFiles = (int) (this.totalRowCount / this.numOfRowsPerFile + 1);
	}

}
