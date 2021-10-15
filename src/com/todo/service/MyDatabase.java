package com.todo.service;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDatabase implements Closeable{

	public static final String dbFile = "todolist.db";
	public static final String tableName = "list";

	private Connection con = null;

	//
	public MyDatabase() {
//		System.out.println("Starting DB Connection");
		try {
			// SQLite JDBC 체크
			Class.forName("org.sqlite.JDBC");

			// SQLite 데이터베이스 파일에 연결
			con = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

			// 만약에 테이블이 없다면 생성
			Statement stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (id INTEGER NOT NULL, title TEXT NOT NULL, memo TEXT, category TEXT NOT NULL, current_date TEXT NOT NULL, due_date TEXT, is_completed integer DEFAULT 0,PRIMARY KEY(id AUTOINCREMENT));";
			stmt.execute(sql);
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	// 연결 종료
	@Override
	public void close() {
		try {
//			System.out.println("Closing DB Connection");
			con.close();
		} catch (SQLException e) {
		}
	}
	
	public Connection getConnection() {
		return con;
	}

//	public int pstmt(String sql, String ...args) {
//		try {
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			for(int i = 1; i <= args.length; i++) {
//				pstmt.set
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0;
//	}

}
