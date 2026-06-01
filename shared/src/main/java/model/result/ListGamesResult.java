package model.result;

import model.Game;

import java.util.Collection;

public record ListGamesResult(Collection<Game> games) {
}
