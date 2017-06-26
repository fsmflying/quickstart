package com.fsmflying.common.thread;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FMSyncReader {

	/**
	 * 读取指定行数的数据(通过属性设置或构造传入)，并以包含"name","data"两个键值的Map&lt;String,List&lt;String&gt;&gt;对象返回;<br/>
	 * 其中,键为"name"的值列表的第一个元素为读取序列的名称;此列表也只有一个元素<br/>
	 * 键为"data"的值列表中存放的是读取的文本行，每个元素是一行;
	 * 
	 * @param rowCount
	 *            读取的数据行数
	 * @return 读取的结果数据
	 * @throws IOException
	 */
	Map<String, List<String>> readWithFileName(int rowCount) throws IOException;

	/**
	 * 读取默认行数的数据(通过属性设置或构造传入)，并以包含"name","data"两个键值的Map&lt;String,List&lt;String&gt;&gt;对象返回;<br/>
	 * 其中,键为"name"的值列表的第一个元素为读取序列的名称;此列表也只有一个元素<br/>
	 * 键为"data"的值列表中存放的是读取的文本行，每个元素是一行;
	 * 
	 * @return 读取的结果数据
	 * @throws IOException
	 */
	Map<String, List<String>> readWithFileName() throws IOException;

	/**
	 * 关闭读取
	 */
	void close() throws IOException;

}