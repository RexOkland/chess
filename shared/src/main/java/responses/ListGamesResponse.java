package responses;

import models.GameData;

import java.util.Collection;
import java.util.HashSet;

public record ListGamesResponse (HashSet<GameData> games, String message){}
