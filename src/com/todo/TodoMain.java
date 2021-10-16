package com.todo;




import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	private static String username = "";
	
	public static String getUserName() {
		return username;
	}
	
	public static void start() {
		// Get userinfo
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter your username: ");
		username = sc.nextLine();
		
		// Load Data
//		TodoUtil.loadList("todolist.txt");
		
		boolean quit = false;
		do {
			Menu.displaymenu();
//			isList = false;
			String[] input = Menu.prompt();
			switch (input[0]) {
			case "add":
				TodoUtil.createItem();
				break;
			
			case "del":
				TodoUtil.deleteItem();
				break;
				
			case "edit":
				TodoUtil.updateItem();
				break;
				
			case "ls":
				TodoUtil.listAll();
				break;

			case "ls_name_asc":
				TodoList.sortByName(true);
//				isList = true;
				break;

			case "ls_name_desc":
				TodoList.sortByName(false);
//				isList = true;
				break;
				
			case "ls_date":
				TodoList.sortByDate(true);
//				isList = true;
				break;
			
			case "ls_date_desc":
				TodoList.sortByDate(false);
//				isList = true;
				break;
				
			case "comp":
				try {
					List<Integer> numbers = new ArrayList<>();
					
					for(String in : input[1].split(","))
						numbers.add(Integer.valueOf(in));
						
					TodoList.completeItems(numbers.toArray(new Integer[0]));					
				} catch (NumberFormatException nfe) {
					System.out.println("[ERROR] Not a number");
				}
				break;
				
			case "ls_comp":
				TodoList.listCompleted();
				break;
			
			case "find":
				TodoList.findItem(String.join(" ", List.of(input).subList(1, input.length)));
				break;
			
			case "find_cat":
				TodoList.findCategory(String.join(" ", List.of(input).subList(1, input.length)));
				break;
				
			case "ls_cat":
				TodoList.listCategories();
				break;

			case "exit":
				quit = true;
				break;
				
			case "import":
				TodoUtil.importCSV(String.join(" ", List.of(input).subList(1, input.length)));
				break;

			default:
				System.out.println("알 수 없는 명령어입니다.");
				break;
			}
		} while (!quit);
		
		// 파일 저장
//		TodoUtil.saveList(l, "todolist.txt");
	}
}
