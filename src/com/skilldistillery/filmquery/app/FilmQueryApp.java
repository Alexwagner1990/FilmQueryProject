package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  
  DatabaseAccessor db = new DatabaseAccessorObject();

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
//    app.test();
    app.launch();
  }

//  private void test() {
//    Film film = db.getFilmById(1);
//    System.out.println(film);
//  }

  private void launch() {
    Scanner input = new Scanner(System.in);
    System.out.println("Welcome to the BMYNHO - Best Movies You've Never Heard Of - database! Enter a choice to use the database!");
    System.out.println("What would you like to do?");
    startUserInterface(input);
    input.close();
  }

  private void startUserInterface(Scanner input) {
	  
	 while(true) { 
	  menuOfOptions();
	  int selection = input.nextInt();
	  
	  switch (selection) {
	  case 1:
		  input.nextLine();
		  System.out.print("Please enter a film ID: ");
		  int filmId = input.nextInt();
		  Film film = db.getFilmById(filmId);
		  printOutAFilm(film);
		  System.out.println("Make Another Choice!");
		  break;
	  case 2:
		  input.nextLine();
		  System.out.println("Gimmie a search term and I'll find something!");
		  String searchTerm = input.nextLine();
		  System.out.println("Searching . . .");
		  List<Film> foundFilmList = db.getFilmBySearchTerm(searchTerm);
		  printOutSeveralFilms(foundFilmList);
		  System.out.println("Make Another Choice!");
		  break;
	  case 3:
		  System.out.println("K Bai!");
		  return;
	  default:
		  System.out.println("Please Enter a Valid Selection!");
		  break;
	  }
	 }
  }

  private void menuOfOptions() {
	  System.out.println("1: Lookup a film by ID");
	  System.out.println("2: Lookup a film by a search keyword");
	  System.out.println("3: Exit");
  }
  
  private void printOutAFilm(Film film) {
	  if(film == null) {
		  System.out.println("\n\nFilm not found, try a different search term or don't I'm not your dad \n\n");
		  return;
	  }
	  db.getFilmWithLanguageName(film);
	  String title = film.getTitle();
	  int year = film.getReleaseYear();
	  String rating = film.getRating();
	  String description = film.getDescription();
	  String languageFull = film.getLanguageName();
	  
	  
	  
	  System.out.println("Title of film (" + languageFull + "): " + title + "\nYear " + title + " was released: " + year + "\n" + title + "'s rating: " + rating + "\n\n__________________________\n" + description + "\n\n");
  }
  
  private void printOutSeveralFilms(List<Film> filmListQuery) {
	  if(filmListQuery == null) {
		  System.out.println("\n\nFilm not found, try a different search term or don't I'm not your dad \n\n");
		  return;
	  }
	  
	  for (int i = 0; i < filmListQuery.size(); i++) {
		System.out.println("Result " + (i + 1) + ":\n****************************************\n ");
		printOutAFilm(filmListQuery.get(i));
		System.out.println("\n****************************************\n");
	}
  }
  

}


//Look up a film by its id.
//Look up a film by a search keyword.
//Exit the application.