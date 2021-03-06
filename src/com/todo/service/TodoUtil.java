package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.todo.PriorityItem;
import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {

	public static void createItem() {

		String title, desc;
		Scanner sc = new Scanner(System.in);

		System.out.print("\n" + "========== 생성 \n" + "제목 > ");

		title = sc.next();
		if (TodoList.isDuplicate(title)) {
			System.out.printf("[오류:중복] 이미 있는 제목입니다.\n");
			return;
		}

		System.out.print("카테고리 > ");
		String category = sc.next().trim();

		System.out.print("설명 > ");
		sc.nextLine(); // ignores the next line from previous next()
		desc = sc.nextLine();

		System.out.print("마감일자 (예: 2021-10-17) > ");
		String duedate = sc.next().trim();

		PriorityItem p = null;

		while (p == null) {
			System.out.print(PriorityItem.getAllOptions());
			System.out.print("우선순위 > ");
			p = PriorityItem.fromNo(sc.nextInt());
		}

		TodoItem t = new TodoItem(category, title, desc, duedate, p);
		TodoList.addItem(t);
	}

	public static void deleteItem() {

		Scanner sc = new Scanner(System.in);

		System.out.println("\n" + "========== 삭제");
		System.out.println("========== , 을 활용하여 다중 입력 가능. (ex 2,3,4)");
		System.out.print("삭제할 번호 입력: ");

		String no = sc.next().trim();
		for(String n : no.split(","))
			TodoList.deleteindex(Integer.parseInt(n) - 1);
	}

	public static void updateItem() {

		Scanner sc = new Scanner(System.in);

		System.out.print("\n" + "========== 수정 \n" + "수정할 항목 번호 입력: ");
		String no = sc.next().trim();
		int no_i = Integer.parseInt(no);

		System.out.print("새로운 제목 > ");
		String new_title = sc.next().trim();
		if (TodoList.isDuplicate(new_title)) {
			System.out.println("[오류:중복] 제목이 중복될 수 없음.");
			return;
		}

		System.out.print("카테고리 > ");
		String category = sc.next().trim();

		System.out.print("새로운 설명 > ");
		sc.nextLine(); // ignores the next line from previous next()
		String new_description = sc.nextLine().trim();

		System.out.print("마감일자 (예: 2021-10-17) > ");
		String duedate = sc.next().trim();

		PriorityItem p = null;

		while (p == null) {
			System.out.print(PriorityItem.getAllOptions());
			System.out.print("우선순위 입력 > ");
			p = PriorityItem.fromNo(sc.nextInt());
		}

		// 업데이트
		TodoList.editItem(no_i, new TodoItem(category, new_title, new_description, duedate, p));
		System.out.println("업데이트 완료.");

	}

	public static void listAll() {
		@SuppressWarnings("deprecation")
		List<TodoItem> list = TodoList.getList();
		System.out.println("[전체 목록, 총 " + list.size() + "개]");
		for (TodoItem item : list) {
			System.out.println(item.toString());
//			System.out.println("Item Title: " + item.getTitle() + "  Item Description:  " + item.getDesc());
		}
	}

//	public static void find(TodoList l) {
//		Scanner sc = new Scanner(System.in);
//		String niddle = sc.nextLine();
//		l.findItem(niddle);
//	}

	public static void importCSV(String filename) {
		try {
			FileReader filereader = new FileReader(filename);

			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();

			for (String[] row : allData) {
				TodoList.addItem(new TodoItem(row[1], row[2], row[3], row[6], row[4],
						PriorityItem.fromNo(Integer.valueOf(row[8])), Integer.valueOf(row[5]), row[7]));
			}
			System.out.println("Imported " + allData.size() + " items.");

		} catch (Exception e) {
			System.out.println("Error while importing CSV");
			e.printStackTrace();
		}

	}

	@Deprecated
	public static void saveList(String filename) {
		try {
			Writer w = new FileWriter(filename);
			for (TodoItem t : TodoList.getList())
				w.write(t.toSaveString());
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public static void loadList(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String oneline;
			while ((oneline = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(oneline, "##");
				TodoList.addItem(
						new TodoItem(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
								PriorityItem.fromNo(Integer.valueOf(st.nextToken())), Integer.valueOf(st.nextToken())));
			}
			br.close();
		} catch (FileNotFoundException e) {
			// File does not exists
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
