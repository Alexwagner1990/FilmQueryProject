package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.FilmCategory;
import com.skilldistillery.filmquery.entities.InventoryItem;

public class DatabaseAccessorObject implements DatabaseAccessor {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("I can't load the database driver. I dunno what to tell ya man.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid";
	private static final String user = "student";
	private static final String pass = "student";

	@Override
	public Film getFilmById(int filmId) {
		ResultSet rs;
		Connection conn;
		PreparedStatement stmt;
		DatabaseAccessorObject castGetter = new DatabaseAccessorObject();
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select id, title, description, release_year, language_id, rental_duration, "
					+ "rental_rate, length, replacement_cost, rating, special_features from film where id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Film f = new Film(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getString(6), rs.getDouble(7), rs.getInt(8), rs.getDouble(9), rs.getString(10),
						rs.getString(11), castGetter.getActorsByFilmId(filmId));
				rs.close();
				conn.close();
				stmt.close();
				return f;
			} else {
//				System.out.println("Film not found, try a different ID or don't I'm not your dad");
				rs.close();
				conn.close();
				stmt.close();
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Database problem. Dunno what to tell ya.");
			return null;
		}
	}

	@Override
	public Actor getActorById(int actorId) {
		ResultSet rs;
		Connection conn;
		PreparedStatement stmt;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select id, first_name, last_name from actor where id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Actor a = new Actor(rs.getInt(1), rs.getString(2), rs.getString(3));
				rs.close();
				conn.close();
				stmt.close();
				return a;
			} else {
				conn.close();
				stmt.close();
				rs.close();
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Database problem. Dunno what to tell ya.");
			return null;
		}

	}

	@Override
	public List<Actor> getActorsByFilmId(int filmId) {
		ResultSet rs;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select a.id, a.first_name, a.last_name from actor a join film_actor fa on a.id = fa.actor_id where fa.film_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			rs = stmt.executeQuery();
			boolean checkIfIdFindsAnything = false;
			List<Actor> actorList = new ArrayList<>();
			while (rs.next()) {
				checkIfIdFindsAnything = true;
				Actor foundActor = new Actor(rs.getInt(1), rs.getString(2), rs.getString(3));
				actorList.add(foundActor);
			}
			if (!checkIfIdFindsAnything) {
				rs.close();
				conn.close();
				stmt.close();
				return null;
			}
			rs.close();
			conn.close();
			stmt.close();
			return actorList;

		} catch (SQLException e) {
			System.out.println("Database problem. Dunno what to tell ya.");
			return null;
		}

	}

	@Override
	public List<Film> getFilmBySearchTerm(String search) {
		ResultSet rs;
		Connection conn;
		PreparedStatement stmt;
		DatabaseAccessorObject castGetter = new DatabaseAccessorObject();
		List<Film> foundFilms = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select id, title, description, release_year, language_id, rental_duration, "
					+ "rental_rate, length, replacement_cost, rating, special_features from film where title like ? or description like ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + search + "%");
			stmt.setString(2, "%" + search + "%");
			rs = stmt.executeQuery();
			boolean didYouFindAFilm = false;
			while (rs.next()) {
				didYouFindAFilm = true;
				Film f = new Film(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5),
						rs.getString(6), rs.getDouble(7), rs.getInt(8), rs.getDouble(9), rs.getString(10),
						rs.getString(11), castGetter.getActorsByFilmId(rs.getInt(1)));
				foundFilms.add(f);
			}
			if (!didYouFindAFilm) {
				conn.close();
				stmt.close();
				rs.close();
				return null;
			}
			return foundFilms;
		} catch (SQLException e) {
			System.out.println("Database problem. Dunno what to tell ya.");
			return null;
		}

	}

	@Override
	public Film getFilmWithLanguageName(Film filmWithoutLanguage) {
		if (filmWithoutLanguage == null) {
			return null;
		}
		int languageId = filmWithoutLanguage.getLanguageId();
		
		ResultSet rs;
		Connection conn;
		PreparedStatement stmt;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select id, name from language where id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, languageId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String languageName = rs.getString(2);
				filmWithoutLanguage.setLanguageName(languageName);
				rs.close();
				conn.close();
				stmt.close();
				return filmWithoutLanguage;
			} else {
				conn.close();
				stmt.close();
				rs.close();
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Database problem. Dunno what to tell ya.");
			return null;
		}
	}
	
	public FilmCategory getFilmCategory(Film filmWithNoDetails) {
		FilmCategory category = new FilmCategory();
		ResultSet rs;
		Connection conn;
		PreparedStatement stmt;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select c.name from film_category fc join category c on fc.category_id = c.id where fc.category_id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmWithNoDetails.getId());
			rs = stmt.executeQuery();
			if (rs.next()) {
				String categoryName = rs.getString(1);
				category.setCategoryName(categoryName);
				rs.close();
				conn.close();
				stmt.close();
				return category;
			} else {
				conn.close();
				stmt.close();
				rs.close();
				return null;
			}
		} catch (SQLException e) {
			System.out.println("Database problem. Dunno what to tell ya.");
			return null;
		}

	}

	@Override
	public List<InventoryItem> getInventoryItem(Film filmWithNoInventoryDetails) {
		List<InventoryItem> inInventory = new ArrayList<>();
		InventoryItem invItem = null;
		ResultSet rs;
		Connection conn;
		PreparedStatement stmt;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "select i.id, i.media_condition from inventory_item i where i.film_id = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmWithNoInventoryDetails.getId());
			rs = stmt.executeQuery();
			boolean checkIfAnyInStock = false;
			while (rs.next()) {
				checkIfAnyInStock = true;
				int invId  = rs.getInt(1);
				String mediaCondition = rs.getString(2);
				invItem = new InventoryItem();
				invItem.setId(invId);
				invItem.setMediaCondition(mediaCondition);
				inInventory.add(invItem);
			} 
			
			if(!checkIfAnyInStock) {
				conn.close();
				stmt.close();
				rs.close();
				return null;
			}
			rs.close();
			conn.close();
			stmt.close();
			return inInventory;
		} catch (SQLException e) {
			System.out.println("Database problem. Dunno what to tell ya.");
			return null;
		}
	}

}
