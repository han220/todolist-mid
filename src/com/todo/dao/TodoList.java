package com.todo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.todo.service.MyDatabase;

public class TodoList {
//	private List<TodoItem> list;

	/**
	 * @deprecated all methods are static. No need to create a instant
	 */
	@Deprecated
	public TodoList() {
//		this.list = new ArrayList<TodoItem>();
	}

	public static void addItem(TodoItem t) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "insert into " + MyDatabase.tableName
				+ " (title, memo, category, current_date, due_date) values (?, ?, ?, ?, ?);";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date_str());
			pstmt.setString(5, t.getDueDate());
			int count = pstmt.executeUpdate();
			System.out.println("Saved " + count + " item(s)");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
//		list.add(t);
	}

	public static void deleteindex(int i) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "DELETE FROM " + MyDatabase.tableName + " WHERE id=?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, i + 1);
			int count = pstmt.executeUpdate();
			System.out.println("Deleted " + count + " item(s)");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}

//	public void set(int i, TodoItem newItem) {
//		list.set(i, newItem);
//	}

	public static void findItem(String niddle) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();
//		System.out.println("Finding..." + niddle);

		String sql = "SELECT * FROM " + MyDatabase.tableName + " WHERE title LIKE ? OR memo like ? ";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + niddle + "%");
			pstmt.setString(2, "%" + niddle + "%");
			ResultSet rs = pstmt.executeQuery();

			// 받은 데이터를 배열에 추가
			int count = 0;
			while (rs.next()) {
				count++;
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("category"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed")).toString());
			}
			System.out.println("Found " + count + " item(s)");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}

	public static void findCategory(String niddle) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();
//		System.out.println("Finding Category..." + niddle);

		String sql = "SELECT * FROM " + MyDatabase.tableName + " WHERE category LIKE ?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + niddle + "%");
			ResultSet rs = pstmt.executeQuery();

			// 받은 데이터를 배열에 추가
			int count = 0;
			while (rs.next()) {
				count++;
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("category"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed")).toString());
			}
			System.out.println("Found " + count + " item(s)");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}

	public static void editItem(int id, TodoItem updated) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "UPDATE " + MyDatabase.tableName
				+ " SET title=?, memo=?, category=?, current_date=?, due_date=? WHERE id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, updated.getTitle());
			pstmt.setString(2, updated.getDesc());
			pstmt.setString(3, updated.getCategory());
			pstmt.setString(4, updated.getCurrent_date_str());
			pstmt.setString(5, updated.getDueDate());
			pstmt.setInt(6, id);
			int count = pstmt.executeUpdate();
			System.out.println("Updated " + count + " item(s)");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}

	public static void completeItem(int id) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "UPDATE " + MyDatabase.tableName
				+ " SET is_completed=? WHERE id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setInt(2, id);
			int count = pstmt.executeUpdate();
			System.out.println("Completed " + count + " item(s)");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}
	
	public static void listCompleted() {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();
		
		String sql = "SELECT * FROM " + MyDatabase.tableName + " WHERE is_completed=1;";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			int counter = 0;

			while (rs.next()) {
				counter++;
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("category"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed")).toString());
			}
			
			System.out.println("Completed " + counter + " task(s)");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}

	/**
	 * @deprecated This shouldn't be used as it gets all of the data from user.
	 * @return
	 */
	@Deprecated
	public static List<TodoItem> getList() {
		List<TodoItem> todos = new ArrayList<>();
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "SELECT * FROM " + MyDatabase.tableName;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// 받은 데이터를 배열에 추가
			while (rs.next()) {
				todos.add(new TodoItem(rs.getInt("id"), rs.getString("category"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
		return todos;
	}

	public static void sortByName(boolean asc) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String desc = asc ? "" : "DESC";
		String sql = "SELECT * FROM " + MyDatabase.tableName + " ORDER BY title " + desc + ";";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("category"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed")).toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}

	/**
	 * @deprecated 정리 함수들이 바로 출력도 같이 함.
	 */
	@Deprecated
	public static void listAll() {
		System.out.println("\n" + "정렬 후 배열\n");
		int x = 1;
		for (TodoItem item : getList()) {
			System.out.println(String.format("%d. [%s] %s - %s - %s - %s", x, item.getCategory(), item.getTitle(),
					item.getDesc(), item.getDueDate(), item.getCurrent_date_str()));
			x++;
		}

	}

	public static void sortByDate(boolean asc) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String desc = asc ? "" : "DESC";
		String sql = "SELECT * FROM " + MyDatabase.tableName + " ORDER BY due_date " + desc + ";";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// 받은 데이터를 배열에 추가
			while (rs.next()) {
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("category"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed")).toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
	}

	public static int indexOf(TodoItem t) {
		return getList().indexOf(t);
	}

	public static Boolean isDuplicate(String title) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "SELECT * FROM " + MyDatabase.tableName + " WHERE title=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();

			// 만약에 값이 있다면 이미 데이터가 있다는 뜻.
			db.close();
			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
		return true;
	}

	public static void listCategories() {
		Set<String> category = new HashSet<>();
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "SELECT DISTINCT category FROM " + MyDatabase.tableName;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// 받은 데이터를 배열에 추가
			while (rs.next()) {
				category.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		db.close();
		System.out.println(String.join(" / ", category));
		System.out.println("총 " + category.size() + "개의 카테고리가 등록되어 있습니다.");
	}
}
