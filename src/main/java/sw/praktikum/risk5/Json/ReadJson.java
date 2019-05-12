package sw.praktikum.risk5.Json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Reads all data from the specific json file and safes them into the instances of this class. The
 * instances can be read by using the getter methods.
 * 
 * @author fahaerte
 */
public class ReadJson {

  private int attackSourceCountry;
  private int attackSourcePlayer;
  private int attackCavalry;
  private int attackInfantry;
  private int attackAtillery;
  private int attackTargetCountry;
  private int attackTargetPlayer;
  private int endGameFirstPosition;
  private int endGameSecondPosition;
  private int getStatsForPlayer;
  private int moveSourceCountry;
  private int moveSourcePlayer;
  private int moveTargetCountry;
  private int moveTargetPlayer;
  private int moveUnitsInfantry;
  private int moveUnitsCavalry;
  private int moveUnitsArtillery;
  private int placeNumberOfUnits;
  private int placeCountry;
  private int placePlayer;
  private String gameStateGameName;
  private int gameStateGameId;
  private int gameStateCurrentPlayer;
  private int gameStateCurrentTurnPhase;
  private int gameStateCurrentGamePhase;
  private int[] gameStateCountriesOwner;
  private int[] gameStateCountriesTroops;
  private int[] gameStateCountriesId;
  private String[] gameStatePlayerNames;
  private int[] gameStatePlayerIds;
  private int[] gameStatePlayerUnits;
  private boolean[] gameStateAiPlayer;
  private int[][] gameStatePlayerCards;
  private int[] gameStateContinentId;
  private int[][] gameStateNeighbours;
  private int[] cardRedemptionCardsId;
  private String jsonName;

  /**
   * Initializes a ReadJSON() - Object.
   * 
   * @author fahaerte
   */
  public ReadJson() {
    this.attackSourceCountry = 0;
    this.attackSourcePlayer = 0;
    this.attackCavalry = 0;
    this.attackInfantry = 0;
    this.attackAtillery = 0;
    this.attackTargetCountry = 0;
    this.attackTargetPlayer = 0;
    this.endGameFirstPosition = 0;
    this.endGameSecondPosition = 0;
    this.getStatsForPlayer = 0;
    this.moveSourceCountry = 0;
    this.moveSourcePlayer = 0;
    this.moveTargetCountry = 0;
    this.moveTargetPlayer = 0;
    this.moveUnitsInfantry = 0;
    this.moveUnitsCavalry = 0;
    this.moveUnitsArtillery = 0;
    this.placeNumberOfUnits = 0;
    this.placeCountry = 0;
    this.placePlayer = 0;
    this.gameStateGameName = null;
    this.gameStateGameId = 0;
    this.gameStateCurrentPlayer = 0;
    this.gameStateCurrentTurnPhase = 0;
    this.gameStateCurrentGamePhase = -1;
    this.gameStateCountriesOwner = new int[42];
    this.gameStateContinentId = new int[42];
    this.gameStateCountriesTroops = new int[42];
    this.gameStatePlayerCards = new int[6][5];
    this.gameStateCountriesId = new int[42];
    this.gameStateNeighbours = new int[42][7];
    this.gameStatePlayerNames = new String[6];
    this.gameStatePlayerIds = new int[6];
    this.gameStatePlayerUnits = new int[6];
    this.gameStateAiPlayer = new boolean[6];
    this.cardRedemptionCardsId = new int[3];
    this.jsonName = "";

  }

  /**
   * Reads the command - name of the JSON file.
   * 
   * @author lroell
   * @param file JSON file
   */
  public void readJson(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      this.jsonName = (String) command.get("name");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the attackJSON - data and saves the values in this class. Instances can be returned by
   * the getter-methods.
   * 
   * @author fahaerte
   * @param file that should be read
   */
  public void readAttackJSON(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");

      JSONObject source = (JSONObject) command.get("source");
      this.attackSourceCountry = Integer.parseInt(source.get("country").toString());
      this.attackSourcePlayer = Integer.parseInt(source.get("player").toString());

      JSONObject units = (JSONObject) source.get("unitsToAttack");
      this.attackCavalry = Integer.parseInt(units.get("cavalry").toString());
      this.attackInfantry = Integer.parseInt(units.get("infantry").toString());
      this.attackAtillery = Integer.parseInt(units.get("artillery").toString());

      JSONObject target = (JSONObject) command.get("target");
      this.attackTargetCountry = Integer.parseInt(target.get("country").toString());
      this.attackTargetPlayer = Integer.parseInt(target.get("player").toString());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the endGameJSON - data and saves the values in this class. Instances can be returned by
   * the getter-methods.
   * 
   * @author fahaerte
   * @param file that should be read
   */
  public void readEndGameJSON(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");

      JSONObject positions = (JSONObject) command.get("positions");
      this.endGameFirstPosition = Integer.parseInt(positions.get("firstPlace").toString());
      this.endGameSecondPosition = Integer.parseInt(positions.get("secondPlace").toString());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the getStatistcsJSON - data and saves the values in this class. Instances can be returned
   * by the getter-methods.
   * 
   * @author fahaerte
   * @param file that should be read
   */
  public void readGetStatisticsJSON(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;
      JSONObject command = (JSONObject) jobj.get("command");

      this.getStatsForPlayer = Integer.parseInt(command.get("forPlayer").toString());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the moveJSON - data and saves the values in this class. Instances can be returned by the
   * getter-methods.
   * 
   * @author fahaerte
   * @param file that should be read
   */
  public void readMoveJSON(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");

      JSONObject source = (JSONObject) command.get("source");
      JSONObject target = (JSONObject) command.get("target");

      this.moveSourceCountry = Integer.parseInt(source.get("country").toString());
      this.moveSourcePlayer = Integer.parseInt(source.get("player").toString());
      this.moveTargetCountry = Integer.parseInt(target.get("country").toString());
      this.moveTargetPlayer = Integer.parseInt(target.get("player").toString());
      
      JSONObject units = (JSONObject) source.get("unitsToMove");
      
      this.moveUnitsInfantry = Integer.parseInt(units.get("infantry").toString());
      this.moveUnitsCavalry = Integer.parseInt(units.get("cavalry").toString());
      this.moveUnitsArtillery = Integer.parseInt(units.get("artillery").toString());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the placeJSON - data and saves the values in this class. Instances can be returned by the
   * getter-methods.
   * 
   * @author fahaerte
   * @param file that should be read
   */
  public void readPlaceJSON(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");

      JSONObject target = (JSONObject) command.get("target");
      this.placeNumberOfUnits = Integer.parseInt(target.get("numberOfUnits").toString());
      this.placeCountry = Integer.parseInt(target.get("country").toString());
      this.placePlayer = Integer.parseInt(target.get("player").toString());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the getGameStateJSON - data and saves the values in this class. Instances can be returned
   * by the getter-methods.
   * 
   * @author fahaerte
   * @param file that should be read
   */
  public void readRiskGetGameStateJSON(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;

      JSONObject command = (JSONObject) jobj.get("command");
      JSONObject gameJ = (JSONObject) command.get("game");
      this.gameStateGameName = gameJ.get("name").toString();
      this.gameStateGameId = Integer.parseInt(gameJ.get("id").toString());
      this.gameStateCurrentPlayer = Integer.parseInt(command.get("currentPlayer").toString());
      this.gameStateCurrentTurnPhase = Integer.parseInt(command.get("currentTurnPhase").toString());
      this.gameStateCurrentGamePhase = Integer.parseInt(command.get("currentGamePhase").toString());

      JSONArray playersArray = (JSONArray) command.get("players");
      JSONArray continentsArray = (JSONArray) command.get("continents");
      JSONArray countriesArray = new JSONArray();
      JSONObject player = new JSONObject();

      for (int i = 0; i < playersArray.size(); i++) {
        player = (JSONObject) playersArray.get(i);
        this.gameStateAiPlayer[i] = Boolean.parseBoolean(player.get("aiPlayer").toString());
        this.gameStatePlayerIds[i] = Integer.parseInt(player.get("id").toString());
        this.gameStatePlayerNames[i] = player.get("name").toString();
        this.gameStatePlayerUnits[i] =
            Integer.parseInt(player.get("numberOfUnitsToPlace").toString());

        JSONArray cardsArray = (JSONArray) player.get("cards");
        int size = cardsArray.size();
        int[] cardIds = new int[size];
        for (int k = 0; k < size; k++) {
          cardIds[k] = Integer.parseInt(cardsArray.get(k).toString());
        }
        this.gameStatePlayerCards[i] = cardIds;
      }

      JSONObject continent = new JSONObject();
      JSONObject country = new JSONObject();

      int counter = 0;

      for (int i = 0; i < continentsArray.size(); i++) {

        continent = (JSONObject) continentsArray.get(i);
        countriesArray = (JSONArray) continent.get("countries");

        for (int j = 0; j < countriesArray.size(); j++) {
          country = (JSONObject) countriesArray.get(j);

          this.gameStateCountriesOwner[counter] = Integer.parseInt(country.get("owner").toString());
          this.gameStateCountriesId[counter] = Integer.parseInt(country.get("id").toString());
          this.gameStateCountriesTroops[counter] =
              Integer.parseInt(country.get("units").toString());
          JSONArray neighbourArray = (JSONArray) country.get("connectionTo");
          int size = neighbourArray.size();
          int[] neighbourIds = new int[7];
          for (int z = 0; z < size; z++) {
            neighbourIds[z] = Integer.parseInt(neighbourArray.get(z).toString());
          }
          this.gameStateNeighbours[counter] = neighbourIds;
          this.gameStateContinentId[counter] = i + 1;
          counter++;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the cardRedemptionJSON - data and saves the values in this class. Instances can be
   * returned by the getter-methods.
   * 
   * @author fahaerte
   * @param file that should be read
   */
  public void readCardRedemption(File file) {
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(new FileReader(file));
      JSONObject jobj = (JSONObject) obj;
      JSONObject command = (JSONObject) jobj.get("command");
      JSONArray cards = (JSONArray) command.get("cards");

      for (int i = 0; i < cards.size(); i++) {
        this.cardRedemptionCardsId[i] = Integer.parseInt(cards.get(i).toString());
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter for all instances.
   * 
   * @return Returns The safed instances from this class
   * @author fahaerte
   */

  public int getAttackSourceCountry() {
    return this.attackSourceCountry;
  }

  public int getAttackSourcePlayer() {
    return this.attackSourcePlayer;
  }

  public int getAttackCavalry() {
    return this.attackCavalry;
  }

  public int getAttackInfantry() {
    return this.attackInfantry;
  }

  public int getAttackAtillery() {
    return this.attackAtillery;
  }

  public int getAttackTargetCountry() {
    return this.attackTargetCountry;
  }

  public int getAttackTargetPlayer() {
    return this.attackTargetPlayer;
  }

  public int getEndGameFirstPosition() {
    return this.endGameFirstPosition;
  }

  public int getEndGameSecondPosition() {
    return this.endGameSecondPosition;
  }

  public int getGetStatsForPlayer() {
    return this.getStatsForPlayer;
  }

  public int getMoveSourceCountry() {
    return this.moveSourceCountry;
  }

  public int getMoveSourcePlayer() {
    return this.moveSourcePlayer;
  }

  public int getMoveTargetCountry() {
    return this.moveTargetCountry;
  }

  public int getMoveTargetPlayer() {
    return this.moveTargetPlayer;
  }

  public int getMoveUnitsInfantry() {
    return this.moveUnitsInfantry;
  }

  public int getMoveUnitsCavalry() {
    return this.moveUnitsCavalry;
  }

  public int getMoveUnitsArtillery() {
    return this.moveUnitsArtillery;
  }

  public int getPlaceNumberOfUnits() {
    return this.placeNumberOfUnits;
  }

  public int getPlaceCountry() {
    return this.placeCountry;
  }

  public int getPlacePlayer() {
    return this.placePlayer;
  }

  public String getGameStateGameName() {
    return this.gameStateGameName;
  }

  public int getGameStateGameId() {
    return this.gameStateGameId;
  }

  public int getGameStateCurrentPlayer() {
    return this.gameStateCurrentPlayer;
  }

  public int getGameStateCurrentTurnPhase() {
    return this.gameStateCurrentTurnPhase;
  }

  public int getGameStateCurrentGamePhase() {
    return this.gameStateCurrentGamePhase;
  }

  public int[] getGameStateCountriesOwner() {
    return this.gameStateCountriesOwner;
  }

  public int[] getGameStateCountriesTroops() {
    return this.gameStateCountriesTroops;
  }

  public int[] getGameStateCountriesId() {
    return this.gameStateCountriesId;
  }

  public String[] getGameStatePlayerNames() {
    return this.gameStatePlayerNames;
  }

  public int[] getGameStatePlayerIds() {
    return this.gameStatePlayerIds;
  }

  public int[] getGameStatePlayerUnits() {
    return this.gameStatePlayerUnits;
  }

  public boolean[] getGameStateAiPlayer() {
    return this.gameStateAiPlayer;
  }

  public int[][] getGameStatePlayerCards() {
    return this.gameStatePlayerCards;
  }

  public int[] getGameStateContinentId() {
    return this.gameStateContinentId;
  }

  public int[][] getGameStateNeighbours() {
    return this.gameStateNeighbours;
  }

  public int[] getCardRedemptionCardsId() {
    return this.cardRedemptionCardsId;
  }

  public String getJsonName() {
    return this.jsonName;
  }
}
