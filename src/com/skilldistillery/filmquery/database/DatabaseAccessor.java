package com.skilldistillery.filmquery.database;

import java.sql.SQLException;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.FilmCategory;
import com.skilldistillery.filmquery.entities.InventoryItem;

public interface DatabaseAccessor{
  public Film getFilmById(int filmId);
  public Actor getActorById(int actorId);
  public List<Actor> getActorsByFilmId(int filmId);
  public List<Film> getFilmBySearchTerm(String search);
  public Film getFilmWithLanguageName(Film filmWithoutLanguage);
  public FilmCategory getFilmCategory(Film filmWithNoDetails);
  public List<InventoryItem> getInventoryItem(Film filmWithNoInventoryDetails);
}
