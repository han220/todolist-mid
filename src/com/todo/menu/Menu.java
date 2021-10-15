package com.todo.menu;

import java.util.Scanner;

public class Menu {

	public static void displaymenu() {
		System.out.println();
		System.out.println("add - Add a new item");
		System.out.println("del - Delete an existing item");
		System.out.println("edit - Update an item");
		System.out.println("ls - List all items");
		System.out.println("ls_name_asc - sort the list by name");
		System.out.println("ls_name_desc - sort the list by name ");
		System.out.println("ls_date - sort the list by date");
		System.out.println("ls_date_desc - sort the list by date (descending)");
		System.out.println("find [Keyword] - Find keyword in title or desc");
		System.out.println("find_cat [Keyword] - Find category");
		System.out.println("ls_cat - List Categories");
		System.out.println("comp [id] - Complete id");
		System.out.println("ls_comp - List completed items");
		System.out.println("exit - exit");
		
	}

	public static String[] prompt() {
		System.out.println("Enter your choice >");
		Scanner s = new Scanner(System.in);
		String in = s.nextLine();
		return in.split(" ");
	}
}
