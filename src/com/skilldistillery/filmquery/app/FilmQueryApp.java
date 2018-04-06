package com.skilldistillery.filmquery.app;

import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  
  DatabaseAccessor db = new DatabaseAccessorObject();

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
    app.test();
    app.launch();
  }

  private void test() {
    Film film = db.getFilmById(1);
    System.out.println(film);
  }

  private void launch() {
    Scanner input = new Scanner(System.in);
    System.out.println("Welcome to the BMYNHO - Best Movies You've Never Heard Of - database! Enter a choice to use the database!");
    System.out.println("What would you like to do?");

    
    input.close();
  }

  private void startUserInterface(Scanner input) {
	  
	  int selection = input.nextInt();
	  
	  switch (selection) {
	  case 1:
		  break;
	  case 2:
		  break;
	  case 3:
		  System.out.println("K Bai!");
		  System.exit(0);
	  default:
		  System.out.println("Please Enter a Valid Selection!");
		  break;
	  }
  }

  private void menuOfOptions() {
	  System.out.println("1: Lookup a film by ID");
	  System.out.println("2: Lookup a film by a search keyword");
	  System.out.println("3: Exit");
  }
}


//Look up a film by its id.
//Look up a film by a search keyword.
//Exit the application.