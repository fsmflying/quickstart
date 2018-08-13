package com.fsmflying.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Hello world!
 *
 */
public class App {
	/**
	 * create table csdn_users(id int auto_increment primary key,username
	 * varchar(64) not null,password varchar(64) not null,email varchar(128));
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// System.out.println("Hello World!");
		// create table csdn_users(id int auto_increment primary key,username
		// varchar(128) not null,password varchar(128) not null,email
		// varchar(128));
		//HashMap
		int i = 0;
		String line = null;
		int numPerWrite = 100000;
		// BufferedReader reader = new BufferedReader(new
		// FileReader("d:\\private\\csdn.txt"));
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream("d:\\private\\csdn.txt"), "GBK"));
		List<CsdnUser> arrUsers = new ArrayList<CsdnUser>();
		String[] fields = null;// new String[3];
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/csdn?useUnicode=true&amp;characterEncoding=utf-8&useSSL=false";
		Connection conn = null;
		PreparedStatement pstmt = null;
		int commitIndex = 0;
		try {
			conn = DriverManager.getConnection(url, "root", "fangming");
			pstmt = conn.prepareStatement("insert into csdn_users(username,password,email) values(?,?,?)");
			conn.setAutoCommit(false);
			while (i < numPerWrite && (line = reader.readLine()) != null) {
				fields = line.split(" ");
				arrUsers.add(new CsdnUser(fields[0], fields[2], fields[4]));
				i++;
				if (i >= numPerWrite) {
					for (CsdnUser u : arrUsers) {
						// System.out.println(u);
						pstmt.setString(1, u.getUsername());
						pstmt.setString(2, u.getPassword());
						pstmt.setString(3, u.getEmail());
						pstmt.execute();
					}
					conn.commit();
					System.out.println("100000*" + ++commitIndex);
					arrUsers.clear();
					i = 0;
				}

			}
			// 最后一页的数据
			if (arrUsers.size() > 0) {
				for (CsdnUser u : arrUsers) {
					pstmt.setString(1, u.getUsername());
					pstmt.setString(2, u.getPassword());
					pstmt.setString(3, u.getEmail());
					pstmt.execute();
				}
				conn.commit();
				arrUsers.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			reader.close();
		}

	}

	public void importToDb() throws IOException {
		String line = null;
		int numPerRead = 100000;
		int i = 0;
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream("d:\\private\\csdn.txt"), "GBK"));
		ExecutorService es01 = Executors.newFixedThreadPool(4);
		while ((line = reader.readLine()) != null) {

			if (i % numPerRead == 0) {

			}
		}

	}

	static class DbWriter implements Runnable {
		private List<CsdnUser> userList;
		

		public DbWriter(List<CsdnUser> userList) {
			this.userList = userList;
		}

		public DbWriter(int numOfRead) {
			this.userList = userList;
		}

		@Override
		public void run() {

			String url = "jdbc:mysql://localhost:3306/csdn?useUnicode=true&amp;characterEncoding=utf-8&useSSL=false";
			Connection conn = null;
			PreparedStatement pstmt = null;
			// int commitIndex = 0;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url, "root", "fangming");
				pstmt = conn.prepareStatement("insert into csdn_users(username,password,email) values(?,?,?)");
				conn.setAutoCommit(false);
				if (userList.size() > 0) {
					for (CsdnUser u : userList) {
						pstmt.setString(1, u.getUsername());
						pstmt.setString(2, u.getPassword());
						pstmt.setString(3, u.getEmail());
						pstmt.execute();
					}
					conn.commit();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.commit();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}

	}

}
