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

import com.todo.PriorityItem;
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
				+ " (title, memo, category, current_date, due_date, priority, owner, is_completed) values (?, ?, ?, ?, ?, ?, ?, ?);";

		String catsearch_sql = "select * from " + MyDatabase.catName + " where categoryName=?;";
		String catcreate_sql = "insert into " + MyDatabase.catName + " (categoryName) values (?);";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 카테고리 처리. 없으면 만들고, 있으면 id 찾기.
			// 카테고리가 있는지 확인
			long catId = -1; // 기본값을 -1 으로 만들고 못찾으면 1추가.
			pstmt = conn.prepareStatement(catsearch_sql);
			pstmt.setString(1, t.getCategory());
			rs = pstmt.executeQuery();
			if (rs.next())
				catId = rs.getLong("categoryId");
			pstmt.close();

			// 없으면 새로 만들기
			if (catId == -1) {
				pstmt = conn.prepareStatement(catcreate_sql);
				pstmt.setString(1, t.getCategory());
				pstmt.executeUpdate();
				rs = pstmt.getGeneratedKeys();
				if (rs.next())
					catId = rs.getLong(1);
				pstmt.close();
			}

			// 다 만들고도 없으면 그냥 리턴
			if (catId == -1) {
				System.out.println("[오류] 카테고리가 정의되어있지 않습니다.");
				db.close();
				return;
			}

			// Add TodoItem to DB
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setInt(3, (int) catId);
			pstmt.setString(4, t.getCurrent_date_str());
			pstmt.setString(5, t.getDueDate());
			pstmt.setInt(6, (t.getPrioirty() != null ? t.getPrioirty() : PriorityItem.NORMAL).getNo());
			pstmt.setString(7, t.getOwner());
			pstmt.setInt(8, t.getIsCompleted());
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

		String sql = "SELECT * FROM " + MyDatabase.tableName
				+ " LEFT JOIN category ON list.category = category.categoryId WHERE title LIKE ? OR memo like ? ;";
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
						rs.getInt("is_completed"), PriorityItem.fromNo(rs.getInt("priority")), rs.getString("owner"))
								.toString());
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

		String catsearch_sql = "select * from " + MyDatabase.catName + " where categoryName=?;";
		String sql = "SELECT * FROM " + MyDatabase.tableName
				+ " LEFT JOIN category ON list.category = category.categoryId WHERE category=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			long catId = -1; // 기본값을 -1 으로 만들고 못찾으면 1추가.
			pstmt = conn.prepareStatement(catsearch_sql);
			pstmt.setString(1, niddle);
			rs = pstmt.executeQuery();
			if (rs.next())
				catId = rs.getLong("categoryId");
			pstmt.close();

			// 다 만들고도 없으면 그냥 리턴
			if (catId == -1) {
				System.out.println("[오류] 카테고리가 정의되어있지 않습니다.");
				db.close();
				return;
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) catId);
			rs = pstmt.executeQuery();

			// 받은 데이터를 배열에 추가
			int count = 0;
			while (rs.next()) {
				count++;
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("categoryName"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed"), PriorityItem.fromNo(rs.getInt("priority")), rs.getString("owner"))
								.toString());
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
				+ " SET title=?, memo=?, category=?, current_date=?, due_date=?, priority=?, owner=? WHERE id=?";
		PreparedStatement pstmt = null;
		String catsearch_sql = "select * from " + MyDatabase.catName + " where categoryName=?;";
		String catcreate_sql = "insert into " + MyDatabase.catName + " (categoryName) values (?);";
		ResultSet rs = null;
		try {
			// 카테고리 처리. 없으면 만들고, 있으면 id 찾기.
			// 카테고리가 있는지 확인
			long catId = -1; // 기본값을 -1 으로 만들고 못찾으면 1추가.
			pstmt = conn.prepareStatement(catsearch_sql);
			pstmt.setString(1, updated.getCategory());
			rs = pstmt.executeQuery();
			if (rs.next())
				catId = rs.getLong("categoryId");
			pstmt.close();

			// 없으면 새로 만들기
			if (catId == -1) {
				pstmt = conn.prepareStatement(catcreate_sql);
				pstmt.setString(1, updated.getCategory());
				pstmt.executeUpdate();
				rs = pstmt.getGeneratedKeys();
				if (rs.next())
					catId = rs.getLong(1);
				pstmt.close();
			}

			// 다 만들고도 없으면 그냥 리턴
			if (catId == -1) {
				System.out.println("[오류] 카테고리가 정의되어있지 않습니다.");
				db.close();
				return;
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, updated.getTitle());
			pstmt.setString(2, updated.getDesc());
			pstmt.setInt(3, (int) catId);
			pstmt.setString(4, updated.getCurrent_date_str());
			pstmt.setString(5, updated.getDueDate());
			pstmt.setInt(6, (updated.getPrioirty() != null ? updated.getPrioirty() : PriorityItem.NORMAL).getNo());
			pstmt.setString(7, updated.getOwner());
			pstmt.setInt(8, id);
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

	public static void completeItems(Integer[] ids) {
		MyDatabase db = new MyDatabase();
		Connection conn = db.getConnection();

		String sql = "UPDATE " + MyDatabase.tableName + " SET is_completed=? WHERE id=?";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);

			for (int id : ids) {
				pstmt.setInt(1, 1);
				pstmt.setInt(2, id);
				int count = pstmt.executeUpdate();
				System.out.println("Completed " + count + " item(s)");

			}

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

		String sql = "SELECT * FROM " + MyDatabase.tableName
				+ " LEFT JOIN category ON list.category = category.categoryId WHERE is_completed=1;";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			int counter = 0;

			while (rs.next()) {
				counter++;
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("categoryName"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed"), PriorityItem.fromNo(rs.getInt("priority")), rs.getString("owner"))
								.toString());
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

		String sql = "SELECT * FROM " + MyDatabase.tableName
				+ " LEFT JOIN category ON list.category = category.categoryId;";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// 받은 데이터를 배열에 추가
			while (rs.next()) {
				todos.add(new TodoItem(rs.getInt("id"), rs.getString("categoryName"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed"), PriorityItem.fromNo(rs.getInt("priority")), rs.getString("owner")));
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
		String sql = "SELECT * FROM " + MyDatabase.tableName
				+ " LEFT JOIN category ON list.category = category.categoryId ORDER BY title " + desc + ";";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("categoryName"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed"), PriorityItem.fromNo(rs.getInt("priority")), rs.getString("owner"))
								.toString());
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
		String sql = "SELECT * FROM " + MyDatabase.tableName
				+ " LEFT JOIN category ON list.category = category.categoryId ORDER BY due_date " + desc + ";";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// 받은 데이터를 배열에 추가
			while (rs.next()) {
				System.out.println(new TodoItem(rs.getInt("id"), rs.getString("categoryName"), rs.getString("title"),
						rs.getString("memo"), rs.getString("due_date"), rs.getString("current_date"),
						rs.getInt("is_completed"), PriorityItem.fromNo(rs.getInt("priority")), rs.getString("owner"))
								.toString());
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

		String sql = "select COUNT(*) totalCount, cat.categoryId, cat.categoryName from " + MyDatabase.tableName
				+ " list inner join " + MyDatabase.catName
				+ " cat on list.category = cat.categoryId group by cat.categoryId, cat.categoryName;";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// 받은 데이터를 배열에 추가
			while (rs.next()) {
				category.add(rs.getString("categoryName") + "(" + rs.getInt("totalCount") + ")");
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
