package sw.praktikum.risk5.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Writes the JSON files
 * 
 * @author lroell
 */

public class WriteJson {

  /**
   * creates attack json with given parameters
   * 
   * @author lroell
   * @param attackCountryId id of the country a player attacks from.
   * @param attackPlayerId id of the player that wants to attack.
   * @param cavalry troops to attack.
   * @param infantry troops to attack.
   * @param artillery troops to attack.
   * @param targetCountryId id of the country the player wants to attack.
   * @param targetPlayerId id of the player that gets attacked.
   * @return file created json.
   */
  public File writeAttackJson(int attackCountryId, int attackPlayerId, int cavalry, int infantry,
      int artillery, int targetCountryId, int targetPlayerId) {
    File file = new File("src/main/resources/JSON/attack.json");
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      JSONObject source = (JSONObject) command.get("source");
      source.put("country", attackCountryId);
      source.put("player", attackPlayerId);
      JSONObject unitsToAttack = (JSONObject) source.get("unitsToAttack");
      unitsToAttack.put("infantry", infantry);
      unitsToAttack.put("cavalry", cavalry);
      unitsToAttack.put("artillery", artillery);

      JSONObject target = (JSONObject) command.get("target");
      target.put("country", targetCountryId);
      target.put("player", targetPlayerId);

      FileWriter writer = new FileWriter("src/main/resources/JSON/attack.json");
      writer.write(jobj.toJSONString());
      writer.flush();
      writer.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * creates endGame json.
   * 
   * @author lroell
   * @param firstPlayerId id of the winner.
   * @param secondPlayerId id of the second place. 
   * @return file created json.
   */
  public File writeEndGameJson(int firstPlayerId, int secondPlayerId) {
    File file = new File("src/main/resources/JSON/end_game.json");
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      JSONObject position = (JSONObject) command.get("positions");
      position.put("firstPlace", firstPlayerId);
      position.put("secondPlace", secondPlayerId);

      FileWriter writer = new FileWriter("src/main/resources/JSON/end_Game.json");
      writer.write(jobj.toJSONString());
      writer.flush();
      writer.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * creates statistics json.
   * 
   * @author lroell
   * @param playerId id of the player that wants to get the statistics
   * @return file created json
   */
  public File writeGetStatisticsJson(int playerId) {
    File file = new File("src/main/resources/JSON/get_statistics.json");
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      command.put("forPlayer", playerId);

      FileWriter writer = new FileWriter("src/main/resources/JSON/get_statistics.json");
      writer.write(jobj.toJSONString());
      writer.flush();
      writer.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * writes move json.
   * 
   * @author lroell
   * @param sourceCountryId id of the country a player wants to move troops from.
   * @param sourcePlayerId id of the player that owns the sourceCountry.
   * @param cavalry troops to move.
   * @param infantry troops to move.
   * @param artillery troops to move.
   * @param targetCountryId id of the country a player wants to move troops to.
   * @param targetPlayerId id of the player that owns the targetCountry.
   * @return file created json.
   */
  public File writeMoveJson(int sourceCountryId, int sourcePlayerId, int cavalry, int infantry,
      int artillery, int targetCountryId, int targetPlayerId) {
    File file = new File("src/main/resources/JSON/move.json");
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      JSONObject source = (JSONObject) command.get("source");
      source.put("country", sourceCountryId);
      source.put("player", sourcePlayerId);
      JSONObject unitsToAttack = (JSONObject) source.get("unitsToMove");
      unitsToAttack.put("infantry", infantry);
      unitsToAttack.put("cavalry", cavalry);
      unitsToAttack.put("artillery", artillery);

      JSONObject target = (JSONObject) command.get("target");
      target.put("country", targetCountryId);
      target.put("player", targetPlayerId);

      FileWriter writer = new FileWriter("src/main/resources/JSON/move.json");
      writer.write(jobj.toJSONString());
      writer.flush();
      writer.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * write place json
   * 
   * @author lroell
   * @param numberOfUnits amount of troops to place in the selected country.
   * @param countryId id of the country the player wants to place the troops.
   * @param playerId id of the player that wants to place troops.
   * @return file created json.
   */
  public File writePlaceJson(int numberOfUnits, int countryId, int playerId) {
    File file = new File("src/main/resources/JSON/place.json");
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      JSONObject target = (JSONObject) command.get("target");
      target.put("numberOfUnits", numberOfUnits);
      target.put("country", countryId);
      target.put("player", playerId);

      FileWriter writer = new FileWriter("src/main/resources/JSON/place.json");
      writer.write(jobj.toJSONString());
      writer.flush();
      writer.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    return file;
  }


  /**
   * creates GameState json
   * 
   * @author lroell
   * @param countriesName the names of all countries.
   * @param countriesId the IDs of all countries.
   * @param countriesOwner the owner of each country sorted by country IDs.  
   * @param countriesUnits units that are currently placed in a country sorted by country IDs.
   * @param countriesNeighbors the neighbors of each country sorted by country IDs.
   * @param matchName name of the current match.
   * @param matchId ID of the match.
   * @param playerId the IDs of all the players in the match with a fixed order.
   * @param aiPlayer tells if the player is an AI or not.
   * @param playerName the name of the players sorted by player IDs.
   * @param playerTroops amount of troops the player can place ordered by player IDs.
   * @param playerCards cards of each player ordered by player IDs.
   * @param currentPlayer player that is currently playing.
   * @param currentTurnPhase current turn.
   * @param currentGamePhase current game phase.
   * @return file created json.
   */
  public File writeGetGameStateJson(String[] countriesName, int[] countriesId, int[] countriesOwner,
      int[] countriesUnits, int[][] countriesNeighbors, String matchName, int matchId,
      int[] playerId, boolean[] aiPlayer, String[] playerName, int[] playerTroops,
      int[][] playerCards, int currentPlayer, int currentTurnPhase, int currentGamePhase) {

    File file = new File("src/main/resources/JSON/risk_getGameState.json");
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");

      JSONObject gameJSON = (JSONObject) command.get("game");
      gameJSON.put("name", matchName);
      gameJSON.put("id", matchId);

      JSONArray continents = (JSONArray) command.get("continents");
      continents.clear();

      JSONObject northAmerica = new JSONObject();
      northAmerica.put("name", "North America");
      northAmerica.put("id", 1);
      northAmerica.put("unitsToReceive", 5);
      JSONObject southAmerica = new JSONObject();
      southAmerica.put("name", "South America");
      southAmerica.put("id", 2);
      southAmerica.put("unitsToReceive", 2);
      JSONObject europe = new JSONObject();
      europe.put("name", "Europe");
      europe.put("id", 3);
      europe.put("unitsToReceive", 5);
      JSONObject africa = new JSONObject();
      africa.put("name", "Africa");
      africa.put("id", 4);
      africa.put("unitsToReceive", 3);
      JSONObject asia = new JSONObject();
      asia.put("name", "Asia");
      asia.put("id", 5);
      asia.put("unitsToReceive", 7);
      JSONObject oceania = new JSONObject();
      oceania.put("name", "Oceania");
      oceania.put("id", 6);
      oceania.put("unitsToReceive", 2);

      JSONArray countriesJSON = new JSONArray();

      for (int i = 0; i < countriesName.length; i++) {
        JSONObject currentCountry = new JSONObject();
        currentCountry.put("name", countriesName[i]);
        currentCountry.put("owner", countriesOwner[i]);
        currentCountry.put("id", countriesId[i]);

        JSONArray neighbor = new JSONArray();
        for (int j = 0; j < countriesNeighbors[i].length; j++) {
          if (countriesNeighbors[i][j] != 0) {
            neighbor.add(countriesNeighbors[i][j]);
          }
        }
        currentCountry.put("connectionTo", neighbor);
        currentCountry.put("units", countriesUnits[i]);
        countriesJSON.add(currentCountry);

        if (countriesId[i] == 9) {
          northAmerica.put("countries", countriesJSON);
          countriesJSON = new JSONArray();
        } else if (countriesId[i] == 13) {
          southAmerica.put("countries", countriesJSON);
          countriesJSON = new JSONArray();
        } else if (countriesId[i] == 20) {
          europe.put("countries", countriesJSON);
          countriesJSON = new JSONArray();
        } else if (countriesId[i] == 26) {
          africa.put("countries", countriesJSON);
          countriesJSON = new JSONArray();
        } else if (countriesId[i] == 38) {
          asia.put("countries", countriesJSON);
          countriesJSON = new JSONArray();
        } else if (countriesId[i] == 42) {
          oceania.put("countries", countriesJSON);
          countriesJSON = new JSONArray();
        }
      }

      continents.add(northAmerica);
      continents.add(southAmerica);
      continents.add(europe);
      continents.add(africa);
      continents.add(asia);
      continents.add(oceania);

      JSONArray players = (JSONArray) command.get("players");
      players.clear();

      JSONObject playerJSON;
      for (int i = 0; i < playerId.length; i++) {
        playerJSON = new JSONObject();
        playerJSON.put("name", playerName[i]);
        playerJSON.put("id", playerId[i]);
        playerJSON.put("aiPlayer", aiPlayer[i]);
        playerJSON.put("numberOfUnitsToPlace", playerTroops[i]);
        JSONArray cards = new JSONArray();
        for (int j = 0; j < playerCards[i].length; j++) {
          cards.add(playerCards[i][j]);
        }
        playerJSON.put("cards", cards);

        players.add(playerJSON);
      }

      command.put("currentPlayer", currentPlayer);
      command.put("currentTurnPhase", currentTurnPhase);
      command.put("currentGamePhase", currentGamePhase);

      FileWriter writer = new FileWriter("src/main/resources/JSON/risk_getGameState.json");
      writer.write(jobj.toJSONString());
      writer.flush();
      writer.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * Creates the cardRedemption - File
   * 
   * @author Fabian
   * @param card1 ID of card 1
   * @param card1Symbol symbol of card 1
   * @param card2 ID of card 2
   * @param card2Symbol symbol of card 2
   * @param card3 ID of card 3
   * @param card3Symbol symbol of card 3
   * @return The JSON - File
   */
  public File writeCardRedemption(int card1, int card2, int card3) {
    File file = new File("src/main/resources/JSON/cardRedemption.json");

    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      JSONArray cards = (JSONArray) command.get("cards");
      cards.clear();
      cards.add(card1);
      cards.add(card2);
      cards.add(card3);

      FileWriter writer = new FileWriter("src/main/resources/JSON/cardRedemption.json");
      writer.write(jobj.toJSONString());
      writer.flush();
      writer.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    }
    return file;
  }

  /**
   * creates skipTurn json
   * 
   * @author lroell
   * @return file created json
   */
  public File writeSkipTurnJson() {
    File file = new File("src/main/resources/JSON/skip_turn.json");
    return file;
  }
}
