package com.fsmflying.common.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件同步读取类，可在多个线程中同步读取文本文件．
 * 
 * @author FangMing
 *
 */
public class FMSyncFileReader implements FMSyncReader {

	private String prefix = "temp";
	private String suffix = "tmp";
	private int nextFileIndex = 0;
	private String dirname = ".\\";

	private BufferedReader reader;

	public FMSyncFileReader(BufferedReader reader) {
		this.reader = reader;
	}

	public FMSyncFileReader(BufferedReader reader, String prefix, String suffix) {
		this.reader = reader;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public FMSyncFileReader(BufferedReader reader, String originalFileName) {
		this.reader = reader;
		if (originalFileName != null && !originalFileName.equals("")) {
			if (originalFileName.lastIndexOf("/") >= 0 || originalFileName.lastIndexOf("\\") >= 0) {
				originalFileName = originalFileName.replace('/', '\\');
				dirname = originalFileName.substring(0, originalFileName.lastIndexOf("\\") + 1);
				originalFileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);
				// System.out.println("dirName:" + dirname);
			}
			int lastIndex = originalFileName.lastIndexOf(".");
			if (lastIndex >= 0) {
				this.prefix = originalFileName.substring(0, lastIndex);
				this.suffix = originalFileName.substring(lastIndex + 1);
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

	/* (non-Javadoc)
	 * @see com.fsmflying.common.thread.FMSyncReader#readWithFileName(int)
	 */
	@Override
	public synchronized Map<String, List<String>> readWithFileName(int rowCount) throws IOException {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		int i = 0;
		List<String> list = new ArrayList<String>();
		String line = reader.readLine();
		while (i < rowCount && (!"".equals(line)) && (null != line)) {
			list.add(line);
			i++;
			line = reader.readLine();
		}
//		System.out.println("nextFileIndex:" + nextFileIndex);
		map.put("data", list);

		List<String> name = new ArrayList<String>();
		name.add(this.getNextFileName());
		map.put("name", name);
		
		return map;
	}

}
