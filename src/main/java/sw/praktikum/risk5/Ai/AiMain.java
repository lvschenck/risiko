package sw.praktikum.risk5.Ai;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Json.ReadJson;
import sw.praktikum.risk5.Json.WriteJson;
import sw.praktikum.risk5.Network.ClientInterface;

/**
 * Class that controlles the AI and communicates with the game.
 * 
 * @author lroell, fahaerte
 *
 */
public class AiMain implements AiInterface {
  private ReadJson jsonReader;
  private WriteJson jsonWriter;
  private int count;
  private ArrayList<AiCountry> allCountries;
  private int[] playerIds;
  private int personalId;
  private int personalUnitsToPlace;
  private int turnPhase;
  private int gamePhase;
  private int currentPlayer;
  private AiType type;
  private AiMoveHandler aiMoveHandler;
  private AiPlacementHandler aiPlacementHandler;
  private AiAttackHandler aiAttackHandler;
  private AiCardRedemptionHandler aiCardRedemptionHandler;
  private int[] cards;
  private ClientInterface client;
  private String aiName;

  /**
   * Constructor for an AI.
   * 
   * @author fahaerte
   * @param type The strength of the AI
   * @param aiId The ID of the AI
   * @param client The Server-Client
   */
  public AiMain(AiType type, int aiId, ClientInterface client, String aiName) {
    this.aiName = aiName;
    this.turnPhase = 0;
    this.gamePhase = -1;
    this.personalUnitsToPlace = 0;
    this.count = 1;
    this.jsonReader = new ReadJson();
    this.jsonWriter = new WriteJson();
    this.allCountries = new ArrayList<AiCountry>(42);
    this.type = type;
    this.cards = new int[3];
    this.client = client;
//    this.aiPlacementHandler = new AiPlacementHandler(aiId, type);
//    this.aiMoveHandler = new AiMoveHandler(aiId, type);
//    this.aiAttackHandler = new AiAttackHandler(aiId, type);
//    this.aiCardRedemptionHandler = new AiCardRedemptionHandler();
//    this.personalId = aiId;
  }

  /**
   * Checks which action of the AI is requested and performs this action.
   * 
   * @author fahaerte
   * @param json The GameStateJson
   */
  @Override
  public void performAction(File json) {
    File j;;
    this.receiveData(json);
    System.out.println(this.currentPlayer + ", " + personalId);
    if (this.currentPlayer == this.personalId) {
      switch (this.gamePhase) {

        case 0:
         
          AiCountry target = this.aiPlacementHandler.placeInEmptyCountries();
          
          if (target != null) {
            j = this.jsonWriter.writePlaceJson(1, target.getId(), this.personalId);
            this.sendJson(j, 'p');
          }
          break;
        case 1:
          this.aiPlacementHandler.calculate(this.personalUnitsToPlace);
          j = this.jsonWriter.writePlaceJson(this.aiPlacementHandler.getAmount(),
              this.aiPlacementHandler.getTarget().getId(), this.personalId);
          this.sendJson(j, 'p');
          break;
        case 2:
          switch (this.turnPhase) {
            case 0:

              if (this.aiCardRedemptionHandler.checkPossibilityToTurnIn()) {

                int[] cards = this.aiCardRedemptionHandler.redeemCards();
                j = this.jsonWriter.writeCardRedemption(cards[0], cards[1], cards[2]);
                this.sendJson(j, 'r');

              } else {

                this.aiPlacementHandler.calculate(this.personalUnitsToPlace);
                j = this.jsonWriter.writePlaceJson(this.aiPlacementHandler.getAmount(),
                    this.aiPlacementHandler.getTarget().getId(), this.personalId);
                this.sendJson(j, 'p');
              }
              break;

            case 1:

              if (this.aiAttackHandler.stillInProgress()) {
                this.aiAttackHandler.attack();
                j = this.jsonWriter.writeAttackJson(this.aiAttackHandler.getSourceCountry().getId(),
                    this.personalId, 0, this.aiAttackHandler.getTroopsToAttack(), 0,
                    this.aiAttackHandler.getTargetCountry().getId(),
                    this.aiAttackHandler.getTargetCountry().getOwner());
                String[] countryNames = RiskMain.getInstance().getDomain().getCountryNames();
                this.client.sendMessageChat(this.aiName, "I am attacking " + countryNames[this.aiAttackHandler.getTargetCountry().getId()] + " from " + countryNames[this.aiAttackHandler.getSourceCountry().getId()] + ".", "all");
                this.sendJson(j, 'a');
              } else {
                j = this.jsonWriter.writeSkipTurnJson();
                this.sendJson(j, 's');
              }
              break;

            case 2:
              this.aiPlacementHandler.setState(0);
              this.defineAllCountriesAi();
              this.aiMoveHandler.move();
              if (this.aiMoveHandler.getSourceCountry() == null) {
                j = this.jsonWriter.writeSkipTurnJson();
              } else {
                j = this.jsonWriter.writeMoveJson(this.aiMoveHandler.getSourceCountry().getId(),
                    this.personalId, 0, this.aiMoveHandler.getAmount(), 0,
                    this.aiMoveHandler.getTargetCountry().getId(), this.personalId);
              }

              this.sendJson(j, 'm');
              break;
            default:
              break;
          }
          break;
        default:
          break;
      }
    }

//     return j;

  }

  /**
   * When receiving a GameState-json this method updates all values.
   * 
   * @param json The JSON date
   * @author lroell
   */
  private void receiveData(File json) {
    this.jsonReader.readJson(json);
    if (!this.jsonReader.getJsonName().equals("gameEnded")) {
      this.jsonReader.readRiskGetGameStateJSON(json);
      int[][] neighbours = this.jsonReader.getGameStateNeighbours();
      int[] continent = this.jsonReader.getGameStateContinentId();
      int[] countries = this.jsonReader.getGameStateCountriesId();
      this.currentPlayer = jsonReader.getGameStateCurrentPlayer();

      if (count == 1) {

        playerIds = this.jsonReader.getGameStatePlayerIds();

        for (int i = 0; i < countries.length; i++) {
          AiCountry current = new AiCountry(continent[i], 0, countries[i]);
          this.allCountries.add(current);
        }

        Iterator<AiCountry> it = this.allCountries.iterator();
        int i = 0;

        while (it.hasNext()) {
          AiCountry current = it.next();
          current.setNeighbor(neighbours[i]);
          i++;
        }
        Iterator<AiCountry> it2 = this.allCountries.iterator();
        int j = 0;
        while (it2.hasNext()) {
          AiCountry current = it2.next();
          for (int k = 0; k < neighbours[j].length; k++) {
            if (this.getCountry(neighbours[j][k]) != null) {
              current.setNeighbor(this.getCountry(neighbours[j][k]));
            }
          }
          j++;
        }
      }

      // owner,units, cards
      int[] unitsToPlaceFromAllPlayers = this.jsonReader.getGameStatePlayerUnits();
      int[][] playerCards = this.jsonReader.getGameStatePlayerCards();

      for (int x = 0; x < this.playerIds.length; x++) {
        if (this.playerIds[x] == this.personalId) {
          this.personalUnitsToPlace = unitsToPlaceFromAllPlayers[x];
          this.cards = playerCards[x];
        }
      }
      // add cards
      this.aiCardRedemptionHandler.clearCards();
      for (int i = 0; i < this.cards.length; i++) {
        this.aiCardRedemptionHandler.addCard(new AiCard(this.cards[i]));
      }

      // game and turn phase
      this.turnPhase = this.jsonReader.getGameStateCurrentTurnPhase();
      this.gamePhase = this.jsonReader.getGameStateCurrentGamePhase();
      this.currentPlayer = this.jsonReader.getGameStateCurrentPlayer();

      // update countries
      int[] troopsInCountries = this.jsonReader.getGameStateCountriesTroops();
      int[] owner = this.jsonReader.getGameStateCountriesOwner();
      Iterator<AiCountry> it2 = this.allCountries.iterator();
      int j = 0;
      while (it2.hasNext()) {
        AiCountry current = it2.next();
        current.setTroops(troopsInCountries[j]);
        current.setOwner(owner[j]);
        j++;
      }
      this.aiPlacementHandler.setAllCountries(this.allCountries);
      this.aiAttackHandler.setAllCountries(this.allCountries);
      this.count++;
    } else {
      // loggoff ai
    }
  }

  /**
   * Creates a Json File and sends it to the Client. You can send attack-, move-, place-json,
   * redeemCard-json.
   * 
   * @param json the first letter of the json you want to send
   * @param j The first letter of the command in the json-file.
   * @author lroell
   */
  private void sendJson(File json, char j) {
    System.out.println("sendet AI");
    switch (j) {
      case 'a':
        this.client.sendJSON(json);
        break;
      case 'm':
        this.client.sendJSON(json);
        break;
      case 'p':
        this.client.sendJSON(json);
        break;
      case 'r':
        this.client.sendJSON(json);
        break;
      default:
        break;
    }
  }

  /**
   * This method returns a country that is searched by the Id of the country.
   * 
   * @param id The parameter is the id in form of an Integer
   * @author gschakar
   */

  protected AiCountry getCountry(int id) {
    Iterator<AiCountry> it = this.allCountries.iterator();
    while (it.hasNext()) {
      AiCountry c = it.next();
      if (id == c.getId()) {
        return c;
      }
    }
    return null;
  }

  /**
   * This method defines all countries of the AI.
   * 
   * @author gschakar
   */

  protected void defineAllCountriesAi() {
    ArrayList<AiCountry> allCountriesFromAi = new ArrayList<AiCountry>();
    Iterator<AiCountry> it = this.allCountries.iterator();
    while (it.hasNext()) {
      AiCountry c = it.next();
      if (c.getOwner() == this.personalId) {
        allCountriesFromAi.add(c);
      }
    }
    this.aiMoveHandler.setAllCountriesFromAi(allCountriesFromAi);
  }

  @Override
  public void setId(int id) {
    this.personalId = id;
    this.aiAttackHandler = new AiAttackHandler(id, type);
    this.aiMoveHandler = new AiMoveHandler(id, type);
    this.aiPlacementHandler = new AiPlacementHandler(id, type);
    this.aiCardRedemptionHandler = new AiCardRedemptionHandler();
  }
}
