package com.fsmflying.hbase01;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

public class HbaseHelper {

	static Admin admin = null;
	static Connection connection = null;
	static Configuration conf = null;

	public static void init() throws IOException {
		if (connection == null || admin == null) {
			conf = HBaseConfiguration.create();
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
		}
	}

	public static void close() throws IOException {
		if (connection != null)
			connection.close();
		if (admin != null)
			admin.close();
	}

	public static void createTable(String tableName, String[] colFamily, boolean autoClose) throws IOException {
		init();
		TableName tableNameObj = TableName.valueOf(tableName);
		if (admin.tableExists(tableNameObj)) {
			System.out.println("talbe[" + tableName + "] is exists!");
		} else {
			HTableDescriptor hTableDescriptor = new HTableDescriptor(tableNameObj);
			for (String str : colFamily) {
				HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(str);
				hTableDescriptor.addFamily(hColumnDescriptor);
			}
			admin.createTable(hTableDescriptor);
			System.out.println("create table[" + tableName + "] success");
		}
		if (autoClose)
			close();

	}

	public static HTableDescriptor[] listTables(boolean autoClose) throws IOException {
		init();
		HTableDescriptor[] hTableDescriptors = admin.listTables();
		if (autoClose)
			close();
		return hTableDescriptors;
	}

	public static void insertCell(String tableName, String rowKey, String colFamily, String qualifier, String value,
			boolean autoClose) throws IOException {
		init();
		Table table = connection.getTable(TableName.valueOf(tableName));
		Put put01 = new Put(rowKey.getBytes());
		put01.addColumn(colFamily.getBytes(), qualifier.getBytes(), value.getBytes());
		table.put(put01);
		table.close();
		if (autoClose)
			close();
	}

	public static void insertCell(Table table, String rowKey, String colFamily, String qualifier, String value,
			boolean autoClose) throws IOException {
		Put put01 = new Put(rowKey.getBytes());
		put01.addColumn(colFamily.getBytes(), qualifier.getBytes(), value.getBytes());
		table.put(put01);
		if (autoClose)
			table.close();
	}

	public static void insertRow(String tableName, String rowKey, String[] columns, String[] values,
			boolean autoClose) throws IOException {
		init();
		Table table = connection.getTable(TableName.valueOf(tableName));
		if (table != null) {
			for (int i = 0; i < columns.length; i++) {
				if (i < columns.length - 1)
					insertCell(table, rowKey, "", columns[i], values[i], false);
			}
			table.close();
		}
		if (autoClose)
			table.close();
	}
	
}
