package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.FilmCategory;
import com.skilldistillery.filmquery.entities.InventoryItem;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		// app.test();
		app.launch();
	}

	// private void test() {
	// Film film = db.getFilmById(1);
	// System.out.println(film);
	// }

	private void launch() {
		Scanner input = new Scanner(System.in);
		System.out.println(
				"Welcome to the \"Love My 'Rents\" (TM) video rental store movie database! \nGet information on a movie (even if we have the movie in stock!) before you even get in the car to drive to the video store!\n It's the way of the future! No other company offers this kind of movie rental convenience (not even Blockbuster)!\nEnter a choice to use the database!");
		System.out.println("What would you like to do?");
		startUserInterface(input);
		input.close();
	}

	private void startUserInterface(Scanner input) {

		while (true) {
			menuOfOptions();
			int selection = input.nextInt();

			switch (selection) {
			case 1:
				input.nextLine();
				System.out.print("Please enter a film ID: ");
				int filmId = input.nextInt();
				Film film = db.getFilmById(filmId);
				printOutAFilm(film);
				if (film != null) {
					subMenuToGetMoreDetails();
					input.nextLine();
					int subchoice = input.nextInt();
					if (subchoice == 2) {
						System.out.println("Make Another Choice!\n");
						break;
					} else {
						printOutAFilmWithAllDetails(film);
					}
				}
				System.out.println("Make Another Choice!\n");
				break;
			case 2:
				input.nextLine();
				System.out.println("Enter a search term to find movies by title or description:");
				String searchTerm = input.nextLine();
				System.out.println("Searching . . .");
				List<Film> foundFilmList = db.getFilmBySearchTerm(searchTerm);
				printOutSeveralFilms(foundFilmList);
				if (foundFilmList != null) {
					boolean checkIfTheyWantMoreDetails = false;
					subMenuToGetMoreDetailsOnOneFilmOfMany();
					int choice = input.nextInt();
					if(choice == 1) {
						checkIfTheyWantMoreDetails = true;
					}
					while(checkIfTheyWantMoreDetails){
						System.out.println("Which film would you like more details on? (Enter a film ID or \"0\" to exit):");
						int choiceOfId = input.nextInt();
						if(choiceOfId == 0) {
							checkIfTheyWantMoreDetails = false;
						}
						if(choiceOfId != 0) {
							printOutAFilmWithAllDetails(db.getFilmById(choiceOfId));
						}
					}
				}
				System.out.println();
				System.out.println("Make Another Choice!\n");

				break;
			case 3:
				System.out.println(
						"Thank you for using the \"Love My 'Rents\" video rental store database. We at \"Love My 'Rents\" strive to provide you the very best in video rental services.\n We at \"Love My 'Rents\" also use time-tested business strategies, and while other video rental companies are busy chasing the next shiny new fad like video streaming,\nwe at \"Love My 'Rents\" will outlast them all by continuing to provide you, the customer, with the no-nonsense video rental brick-and-mortar stores you really desire.\n\nHave a nice day!");
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
		if (film == null) {
			System.out.println("\n\nFilm not found, try a different search term! \n\n");
			return;
		}
		db.getFilmWithLanguageName(film);
		int id = film.getId();
		String title = film.getTitle();
		int year = film.getReleaseYear();
		String rating = film.getRating();
		String description = film.getDescription();
		String languageFull = film.getLanguageName();
		List<Actor> castOfFilm = film.getCast();

		System.out.println(
				"Film ID: " + id + "\nTitle of film (" + languageFull + "): " + title + "\nYear " + title + " was released: " + year + "\n"
						+ title + "'s rating: " + rating + "\n\n__________________________\n" + description + "\n");
		System.out.print("Cast List for " + title + ": ");
		if (film.getCast() == null) {
			System.out.print(
					"N/A \n");
			return;
		}
		for (int i = 0; i < castOfFilm.size(); i++) {
			Actor a = castOfFilm.get(i);
			String actorName = a.getFirstName() + " " + a.getLastName();
			System.out.print(actorName);
			if (i < (castOfFilm.size() - 1)) {
				System.out.print(", ");
			}
		}
		System.out.println("\n");
	}

	private void printOutAFilmWithAllDetails(Film film) {
		if (film == null) {
			System.out.println("\n\nFilm not found, try a different search term! \n\n");
			return;
		}
		db.getFilmWithLanguageName(film);
		int id = film.getId();
		String title = film.getTitle();
		String description = film.getDescription();
		int year = film.getReleaseYear();
		int languageId = film.getLanguageId();
		String rentalDuration = film.getRentalDuration();
		double rentalRate = film.getRentalRate();
		int length = film.getLength();
		double replacementCost = film.getReplacementCost();
		String rating = film.getRating();
		String specialFeatures = film.getSpecialFeatures();
		String languageFull = film.getLanguageName();
		List<Actor> castOfFilm = film.getCast();
		FilmCategory category = db.getFilmCategory(film); // <--
		String categoryName = "N/A";
		if(category != null) {
			categoryName = category.getCategoryName();
		}
		
		System.out.println(
				" + Film Bio Information: + \n***************************************************************\n");
		System.out.println("Film ID: " + id + "\nFilm Title: " + title + "\nFilm Language ID: " + languageId
				+ "\nFilm Language: " + languageFull + "\nFilm Length: " + length + "\nFilm Rating: " + rating
				+ "\nFilm Category: " + categoryName);
		System.out.println("\n***************************************************************\n");

		System.out.println(
				" + More Detailed Information: + \n***************************************************************\n");
		System.out.println("Film Description: " + description);
		System.out.print("Cast List for " + title + ": ");
		if (film.getCast() == null) {
			System.out.print(
					"No Cast found. Weird. Must be one of those fruity Liberal Arts grad student film projects that asks the audience to grapple with\nthe true essence of a film by showing us a really boring movie. Don't know how else you have a movie without a cast.\n");
		} else {
			for (int i = 0; i < castOfFilm.size(); i++) {
				Actor a = castOfFilm.get(i);
				String actorName = a.getFirstName() + " " + a.getLastName();
				System.out.print(actorName);
				if (i < (castOfFilm.size() - 1)) {
					System.out.print(", ");
				}
			}
		}
		System.out.println("\nSpecial Features: " + specialFeatures);

		System.out.println(
				"\n + Rental Information: +\n***************************************************************\n");
		System.out.println("Rental Duration: " + rentalDuration + "\nRental Rate: " + rentalRate
				+ "\nReplacement Cost: " + replacementCost);
		System.out.println("\n***************************************************************\n");

		System.out.println(
				"\n + Inventory Information: + \n***************************************************************\n");
		System.out.println("Inventory ID || Video Condition\n____________________________________________");
		List<InventoryItem> allItemsOfFilm = db.getInventoryItem(film);
		if (allItemsOfFilm == null) {
			System.out.println("Wow, there are no copies of that film to rent at the moment. It must be either really popular or not very popular.\n");
		}
		else {
			for (InventoryItem inventoryItem : allItemsOfFilm) {
				int idOfOneItem = inventoryItem.getId();
				String condition = inventoryItem.getMediaCondition();
				System.out.println(idOfOneItem + "\t" + condition);
			}
		}
		System.out.println("\n***************************************************************\n");
	}

	private void printOutSeveralFilms(List<Film> filmListQuery) {
		if (filmListQuery == null) {
			System.out.println("\n\nFilm not found, try a different search term or don't I'm not your dad \n\n");
			return;
		}

		for (int i = 0; i < filmListQuery.size(); i++) {
			System.out.println("Result " + (i + 1) + ":\n****************************************\n ");
			printOutAFilm(filmListQuery.get(i));
			System.out.println("\n****************************************\n");
		}
	}

	private void subMenuToGetMoreDetails() {
		System.out.println("Would you like to view more details about the film?");
		System.out.println("1: Yes");
		System.out.println("2: No");
	}
	
	private void subMenuToGetMoreDetailsOnOneFilmOfMany() {
		System.out.println("Would you like to view more details about a listed film?");
		System.out.println("1: Yes");
		System.out.println("2: No");
	}
}

