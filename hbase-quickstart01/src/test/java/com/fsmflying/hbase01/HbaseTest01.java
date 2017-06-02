package com.fsmflying.hbase01;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HbaseTest01 {

	Admin admin = null;
	Connection connection = null;
	Configuration conf = null;

	@Before
	public void before() {
		conf = HBaseConfiguration.create();
//		conf.set("hbase.rootdir", "hdfs://master.hadoop:9000/hbase");
//		conf.set("hbase.zookeeper.quorum", "master.hadoop");
		//conf.set("hbase.zookeeper.property.clientPort", "2181");
		try {
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() throws IOException {
		if (admin != null)
			admin.close();
		if (connection != null)
			connection.close();
	}

	@Test
	public void test01_createTables() throws IOException {
//		org.apache.hadoop.hdfs.DistributedFileSystem
//		org.apache.hadoop.hdfs.DistributedFileSystem
		String  myTableName = "users";
		String[] colFamily = new String[]{"username","password","sex","birthdate","memo","others"};
		TableName tableName = TableName.valueOf(myTableName);
		 
        if(admin.tableExists(tableName)){
            System.out.println("talbe is exists!");
        }else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for(String str:colFamily){
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(str);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
            System.out.println("create table success");
        }

	}
	
	@Test
	public void test02_deleteTables() throws IOException {
		
		String  myTableName = "users";
		TableName tableName = TableName.valueOf(myTableName);
        if(admin.tableExists(tableName)){
        	admin.disableTable(tableName);
        	admin.deleteTable(tableName);
            System.out.println("talbe was deleted successfully!");
        }
	}
	
	@Test
	public void test03_listTables() throws IOException {
		
		HTableDescriptor hTableDescriptors[] = admin.listTables();
        for(HTableDescriptor hTableDescriptor :hTableDescriptors){
            System.out.println(hTableDescriptor.getNameAsString());
        }
	}
	
	public void insertCell(String tableName,String rowKey,String colFamily,String qualifier,String value) throws IOException
	{
		Table table = connection.getTable(TableName.valueOf(tableName));
		Put put01 = new Put(rowKey.getBytes());
		put01.addColumn(colFamily.getBytes(), qualifier.getBytes(), value.getBytes());
		table.put(put01);
		table.close();
	}
	
	public void insertCell(Table table,String rowKey,String colFamily,String qualifier,String value) throws IOException
	{
		Put put01 = new Put(rowKey.getBytes());
		put01.addColumn(colFamily.getBytes(), qualifier.getBytes(), value.getBytes());
		table.put(put01);
	}
	
	public void insertRow(String rowKey,String username,String password,String sex,String birthdate,String memo,String others) throws IOException
	{
		Table table = connection.getTable(TableName.valueOf("users"));
		insertCell(table,rowKey,"username","",username);
		insertCell(table,rowKey,"password","",password);
		insertCell(table,rowKey,"sex","",sex);
		insertCell(table,rowKey,"birthdate","",birthdate);
		insertCell(table,rowKey,"memo","",memo);
		insertCell(table,rowKey,"others","",others);
		table.close();
	}
	
	@Test
	public void test04_insertRows() throws IOException {
//		String  myTableName = "users";
//		Table table = connection.getTable(TableName.valueOf(myTableName));
//		Put put01 = new Put("10001".getBytes());
//		put01.addColumn("username".getBytes(), "kkkkkk".getBytes(), "fangming".getBytes());
//		table.put(put01);
		this.insertRow("10001", "fangming", "123456", "male", "1984-09-24", "test", "");
		this.insertRow("10002", "wangxiaojuan", "123456", "female", "1986-05-13", "test", "");
		this.insertRow("10003", "yanxia", "123456", "female", "1985-09-18", "test", "");
		this.insertRow("10003", "yanxia", "123456", "female", "1985-09-18", "test", "");
//		table.close();
	}
	
	@Test
	public void test05_deleteRows() throws IOException {
		String  myTableName = "users";
		Table table = connection.getTable(TableName.valueOf(myTableName));
        Delete delete = new Delete("10001".getBytes());
        //删除指定列族的所有数据
        //delete.addFamily(colFamily.getBytes());
        //删除指定列的数据
        //delete.addColumn(colFamily.getBytes(), col.getBytes());
 
        table.delete(delete);
        table.close();
	}
	
	 /**
     * 格式化输出
     * @param result
     */
    public void showCell(Result result){
        Cell[] cells = result.rawCells();
        for(Cell cell:cells){
            System.out.println("RowName:"+new String(CellUtil.cloneRow(cell))+" ");
            System.out.println("Timetamp:"+cell.getTimestamp()+" ");
            System.out.println("column Family:"+new String(CellUtil.cloneFamily(cell))+" ");
            System.out.println("row Name:"+new String(CellUtil.cloneQualifier(cell))+" ");
            System.out.println("value:"+new String(CellUtil.cloneValue(cell))+" ");
        }
    }
	
	@Test
	public void test06_getData() throws IOException {
		String  myTableName = "users";
		Table table = connection.getTable(TableName.valueOf(myTableName));
        Get get = new Get("10001".getBytes());
        get.addColumn("username".getBytes(),"kkkkkk".getBytes());
        Result result = table.get(get);
        showCell(result);
        table.close();
	}
}
