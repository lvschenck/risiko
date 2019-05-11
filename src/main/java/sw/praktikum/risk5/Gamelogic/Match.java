package sw.praktikum.risk5.Gamelogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;

/**
 * Class that simulates a single risk match
 *
 * @author lroell, fahaerte
 */

class Match {
  private ArrayList<Player> players;
  private ArrayList<Card> availableCards;
  private ArrayList<Card> allCards;
  private ArrayList<Country> countries;
  private String gameName;
  private Player currentPlayer;
  private int currentTurnPhase;
  private int currentGamePhase;
  private int count = 0;
  private int matchId; // fehlt noch
  private Statistics stats;
  private Database data = RiskMain.getInstance().getDomain().getData();

  /**
   * Constructor for the class Match
   * 
   * @author lroell
   * @param allCards All cards, initialized by GameController
   * @param allCountries All countries, initialized by GameController
   * @param stats Statistics of the current match
   */
  protected Match(ArrayList<Card> allCards, ArrayList<Country> allCountries, String gameName,
      Statistics stats) {
    this.players = new ArrayList<Player>();
    this.data = new Database();
    this.availableCards = allCards;
    this.countries = allCountries;
    this.gameName = gameName;
    this.currentTurnPhase = 0;
    this.currentGamePhase = 0;
    // this.matchId = Integer.parseInt(data.getCurrentGameData("ID"));
    this.stats = stats;
  }

  /**
   * When receiving skip_turn.json or a player placed all troops or a player moved once this method
   * is called. The method increases the turnphase
   * 
   * @author lroell
   */
  protected void endTurnPhase() {
    if (this.currentTurnPhase == 2) {
      int random = (int) (Math.random() * this.allCards.size());
      this.currentPlayer.drawCard(this.allCards.get(random));
      this.currentPlayer.setConquered(false);
      this.currentTurnPhase = 0;
      this.changeCurrentPlayer();
    } else {
      this.currentTurnPhase++;
    }
  }

  protected void changeCurrentPlayer() {
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      if (it.next().equals(this.currentPlayer)) {
        try {
          this.currentPlayer = it.next();
        } catch (NoSuchElementException e) {
          this.currentPlayer = this.players.get(0);
        }
      }
    }
  }

  /**
   * Establishes order at the beginning of a match. Player with highest "rolled dice" starts.
   * 
   * @author lroell
   */
  protected void establishOrder() {
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      Player tmp = it.next();
      if (tmp.getId() == 1) {
        this.currentPlayer = tmp;
      }
    }
    // int random = (int) (Math.random() * this.players.size());
    // this.currentPlayer = this.players.get(random);
  }

  protected int getCurrentPlayerId() {
    return this.currentPlayer.getId();
  }

  protected Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  protected int getCurrentTurnPhase() {
    return this.currentTurnPhase;
  }

  protected int getCurrentGamePhase() {
    return this.currentGamePhase;
  }

  /**
   * Returns the cards of each player.
   * 
   * @author fahaerte
   * @return 2-D int array with all Cards from each player
   */
  protected int[][] getPlayerCards() {
    int[][] playerCards = new int[this.players.size()][5];
    Iterator<Player> it = this.players.iterator();
    for (int i = 0; i < this.players.size() && it.hasNext(); i++) {
      Player current = it.next();
      int count = 0;
      Iterator<Card> it2 = current.getHoldingCards().iterator();
      while (it2.hasNext()) {
        playerCards[i][count] = it2.next().getId();
        count++;
      }
    }
    return playerCards;
  }

  /**
   * Returns the countries ids
   * 
   * @author fahaerte
   * @return all ids from the countries
   */
  protected int[] getCountriesIds() {
    int[] ids = new int[42];
    int counter = 0;
    Iterator<Country> it = this.countries.iterator();
    while (it.hasNext()) {
      ids[counter] = it.next().getId();
      counter++;
    }
    return ids;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected int[] getCountriesOwners() {
    int[] ownerIds = new int[42];
    int counter = 0;
    Iterator<Country> it = this.countries.iterator();
    while (it.hasNext()) {
      Country c = it.next();
      if (c.getOwner() == null) {
        ownerIds[counter] = 0;
      } else {
        ownerIds[counter] = c.getOwner().getId();
      }
      counter++;
    }
    return ownerIds;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected String[] getCountriesName() {
    String[] names = new String[42];
    int counter = 0;
    Iterator<Country> it = this.countries.iterator();
    while (it.hasNext()) {
      names[counter] = it.next().getName();
      counter++;
    }
    return names;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected int[] getCountriesTroops() {
    int[] troops = new int[42];
    int counter = 0;
    Iterator<Country> it = this.countries.iterator();
    while (it.hasNext()) {
      troops[counter] = it.next().getTroops();
      counter++;
    }
    return troops;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected int[][] getCountriesNeighbors() {
    int[][] neigbors = new int[42][7];
    Iterator<Country> it = this.countries.iterator();

    for (int i = 0; i < 42 && it.hasNext(); i++) {
      int counter = 0;
      Country c = it.next();
      HashSet<Country> n = c.getNeighbors();
      Iterator<Country> it2 = n.iterator();
      while (it2.hasNext()) {
        neigbors[i][counter] = it2.next().getId();
        counter++;
      }
    }
    return neigbors;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected int[] getPlayersId() {
    int[] ids = new int[this.players.size()];
    int counter = 0;
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      ids[counter] = it.next().getId();
      counter++;
    }
    return ids;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected boolean[] getPlayerAi() {
    boolean[] ais = new boolean[this.players.size()];
    int counter = 0;
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      ais[counter] = it.next().getAi();
      counter++;
    }
    return ais;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected String[] getPlayerNames() {
    String[] names = new String[this.players.size()];
    int counter = 0;
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      names[counter] = it.next().getName();
      counter++;
    }
    return names;
  }

  /**
   * 
   * @author fahaerte
   * @return
   */
  protected int[] getPlayersUnitsToPlace() {
    int[] units = new int[this.players.size()];
    int counter = 0;
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      units[counter] = it.next().getAmountOfUnitsToPlace();
      counter++;
    }
    return units;
  }

  /**
   * creates a player with an unique ID
   * 
   * @author lroell
   * @param newP: Player that should be created
   * @return int: ID of the created player
   */
  protected int createPlayer(Player newP) {
    this.players.add(newP);
    newP.setMatch(this);
    return newP.getId();
  }

  /**
   * deletes a player with an ID
   * 
   * @param id: ID of the Player
   * @author lroell
   */
  protected void deletePlayer(int id) {
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      Player p = it.next();
      if (p.getId() == id) {
        players.remove(p);
      }
    }
  }

  protected ArrayList<Player> getPlayers() {
    return this.players;
  }

  protected String getName() {
    return this.gameName;
  }

  protected ArrayList<Card> getAvailableCards() {
    return this.availableCards;
  }

  /**
   * Places troops on a country the player chooses Used to first own a country or to place troops on
   * a country you already own
   * 
   * @param amount: the amount of troops the player want to place in the country
   * @return boolen true, if troops placed successfully
   * @author lroell
   */
  protected boolean chooseCountry(int countryId, int playerId, int amount) {
    boolean b = false;
    Iterator<Country> it = this.countries.iterator();
    Country country = this.findCountry(countryId);
    Player player = this.findPlayer(playerId);
    boolean countriesOccupied = true;

    while (it.hasNext()) {
      Country c = it.next();
      if (c.getId() == country.getId()
          && (c.getOwner().getId() == 0 || c.getOwner().getId() == player.getId())
          && player.getAmountOfUnitsToPlace() >= amount) {
        if (this.currentGamePhase == 0) {
          this.changeCurrentPlayer();
          if (c.getOwner().getId() != 0) {
            return false;
          }
        }
        c.setOwner(player);
        player.addCountry(c);
        country.addTroops(amount);
        player.setAmountOfUnitsToPlace(-amount);
        b = true;
      }

      if (c.getOwner().getId() == 0) {
        countriesOccupied = false;
      }
    }
    boolean allTroopsSetFirstTime = true;
    for (Player p : this.players) {
      if (!(p.getAmountOfUnitsToPlace() == 0)) {
        allTroopsSetFirstTime = false;
      }
    }
    if (countriesOccupied && this.count == 0) {
      System.out.println("AMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMK");
      this.currentGamePhase = 1;
      count++;
    }
    if (allTroopsSetFirstTime) {
      this.currentGamePhase = 2;
    }
    if (player.getAmountOfUnitsToPlace() == 0 && this.currentGamePhase != 0) {
      this.changeCurrentPlayer();
    }
    return b;
  }

  /**
   * Fortifying of your position. The method checks wwether the inputs were valid
   * 
   * @author fahaerte
   * @param sourcePlayerId : Player ID
   * @param sourceCountry : Country ID
   * @param amount : amount of troops to place
   * @param targetPlayerId : PlayerID of the target. Should be equal to sourcePlayerID
   * @param targetCountry : Country ID
   * @return boolean: success of the action
   */
  protected boolean fortifyPosition(int sourcePlayerId, Country sourceCountry, int amount,
      int targetPlayerId, Country targetCountry) {
    boolean result = false;
    Player p = this.findPlayer(sourcePlayerId);
    Player p2 = this.findPlayer(targetPlayerId);

    if (amount < sourceCountry.getTroops() && sourceCountry.getOwner().equals(p)
        && targetCountry.getOwner().equals(p)
        && targetCountry.getOwner().equals(sourceCountry.getOwner()) && p.equals(p2)) {
      result = true;
      sourceCountry.addTroops((-1) * amount);
      targetCountry.addTroops(amount);
    }

    return result;
  }

  /**
   * Ends a single match and sets the Instance "Match" on "null" for each player
   * 
   * @author lroell
   */
  protected void endMatch() {
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      Player p = it.next();
      p.setMatch(null);
    }
  }

  /**
   * Searches a player by his id and returns the player
   * 
   * @author fahaerte
   * @param id of the player
   * @return Player: the player if found, else null
   */
  protected Player findPlayer(int id) {
    Player p;

    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      p = it.next();
      if (p.getId() == id) {
        return p;
      }
    }
    return null;
  }

  /**
   * Adds card to the available cards
   * 
   * @param c: Card to add
   * @author fahaerte
   */
  protected void addAvailableCard(Card c) {
    this.availableCards.add(c);
  }

  /**
   * Searches a country by his id and returns the country
   * 
   * @author fahaerte
   * @param id
   * @return Country
   */
  protected Country findCountry(int id) {
    Iterator<Country> it = this.countries.iterator();
    Country c;
    while (it.hasNext()) {
      c = it.next();
      if (c.getId() == id) {
        return c;
      }
    }
    return null;
  }

  /**
   * Finds and returns a specified card from allCards
   * 
   * @author fahaerte
   * @param id The id of the searched card
   * @return
   */
  protected Card findCard(int id) {
    Iterator<Card> it = this.allCards.iterator();
    while (it.hasNext()) {
      Card c = it.next();
      if (id == c.getCountry().getId()) {
        return c;
      }
    }
    return null;
  }

  protected int getMatchId() {
    return this.matchId;
  }

  protected void updateStatistics() {
    // laender zum hoechsten punkt verschiedenen Plaetze
    int amountOfTroops = 0;
    int amountOfCountries = 0;
    Iterator<Country> it = this.countries.iterator();
    while (it.hasNext()) {
      Country tmp = it.next();
      if (tmp.getOwner().equals(this.currentPlayer)) {
        amountOfCountries++;
        amountOfTroops += tmp.getTroops();
      }
    }
    this.stats.createPossessionStatistics(this.currentPlayer.getId(), amountOfTroops,
        amountOfCountries);
  }

  /**
   * checks if a player has countries. When a player has zero countries he gets deleted and the
   * player who defeated him gets his holding cards
   * 
   * @author lroell
   */
  protected void checkAmountCountries() {
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      Player tmp = it.next();
      if (tmp.getConqueredCountries().size() == 0) {
        int index = 0;
        while (this.currentPlayer.getHoldingCards().size() < 5) {
          tmp.addCard(tmp.getHoldingCards().get(index));
          index++;
        }
        this.deletePlayer(tmp.getId());
      }
    }
  }

  /**
   * Gives all players troops to place at the beginning of the game.
   * 
   * @author lroell
   */
  protected void receiveTroopsBeginning() {
    Iterator<Player> it = this.players.iterator();
    while (it.hasNext()) {
      switch (this.players.size()) {
        case 2:
          it.next().setAmountOfUnitsToPlace(40);
          break;
        case 3:
          it.next().setAmountOfUnitsToPlace(35);
          break;
        case 4:
          it.next().setAmountOfUnitsToPlace(30);
          break;
        case 5:
          it.next().setAmountOfUnitsToPlace(25);
          break;
        case 6:
          it.next().setAmountOfUnitsToPlace(20);
          break;
        default:
          System.out.println("Truppen platzieren fehler anfang");
          break;

      }
    }
  }
}
