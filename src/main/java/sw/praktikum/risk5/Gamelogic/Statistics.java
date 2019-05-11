package sw.praktikum.risk5.Gamelogic;

import java.util.LinkedList;
import java.util.List;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;

/**
 * This class creates/calculates statistics and serves the database.
 * 
 * @author sdoehler
 *
 */
public class Statistics {

  private Database db;
  private int totalBattles; // totale Anzahl geführter Kämpfe in einem Spiel
  private List<Integer> allBattlesAmountTroops;
  private List<Integer> allWinner;
  private List<Integer> allLoser;
  private List<Boolean> conqueredCountries;
  private List<Integer> eliminationOrder;
  private int[] battlefieldCountryCounter;
  private int[][] biggestEmpire;
  private int playerLength;

  /**
   * Creates new instance of Statistics and initialises the class attributes.
   */
  public Statistics() {
    allBattlesAmountTroops = new LinkedList<Integer>();
    allWinner = new LinkedList<Integer>();
    allLoser = new LinkedList<Integer>();
    conqueredCountries = new LinkedList<Boolean>();
    eliminationOrder = new LinkedList<Integer>();

    battlefieldCountryCounter = new int[42];
    biggestEmpire = new int[playerLength][3];
    for (int i = 0; i < playerLength; i++) {
      for (int k = 0; k < playerLength; k++) {
        biggestEmpire[i][k] = 0;
      }
    }
    totalBattles = 0;
    for (int i = 0; i < battlefieldCountryCounter.length; i++) {
      battlefieldCountryCounter[i] = 0;
    }
  }

  /**
   * Gets statistics from the BattleHandler class and safes them into the attributes of this class.
   * The statistics are about how a battle outcomes.
   * 
   * @param attackingTroops the amount of troops used to attack
   * @param defendingTroops the amount of troops used to defend
   * @param countryID the country in which the battle took place
   * @param attackerId the attacking player
   * @param targetId the defending player
   * @param conquered whether the country got conquered 
   */
  
  public void createBattleStatistics(int attackingTroops, int defendingTroops, int countryID,
      int attackerId, int targetId, boolean conquered) {

    this.allBattlesAmountTroops.add(attackingTroops + defendingTroops);
    this.battlefieldCountryCounter[countryID - 1] = +1;
    this.conqueredCountries.add(conquered);
    this.totalBattles++;

    if (conquered) {
      this.allWinner.add(attackerId);
      this.allLoser.add(targetId);
    } else {
      this.allWinner.add(targetId);
      this.allLoser.add(attackerId);

    }
  }
  
  /**
   * Gets statistics from the Match class and safes them into the attributes of this class.
   * The statistics are about the ressources of a player.
   *
   * @param currentPlayerId the player to which the statistics belong
   * @param amountTroops how many troops the player has at this point of the game
   * @param amountCountries how many countries the player has this point of the game
   */

  public void createPossessionStatistics(int currentPlayerId, int amountTroops,
      int amountCountries) {
    if (amountCountries == 0 || amountCountries == 42) {
      eliminationOrder.add(currentPlayerId);
    } else {
      for (int i = 0; i < biggestEmpire.length; i++) {
        if (biggestEmpire[i][0] == currentPlayerId) {
          if (amountCountries > biggestEmpire[i][1]) {
            biggestEmpire[i][1] = amountCountries;
          }
          if (amountTroops > biggestEmpire[i][2]) {
            biggestEmpire[i][2] = amountTroops;
          }
        }
      }

    }
  }
  
  /**
   * Gets all troops which have been used in the game.
   * 
   * @return the amount of troops
   */

  public int getTotalTroops() {
    int totalTroops = 0;
    for (int i = 0; i < allBattlesAmountTroops.size(); i++) {
      totalTroops = +allBattlesAmountTroops.get(i);
    }
    return totalTroops;
  }
  
  /**
   * Gets the country where the most battles happened.
   * 
   * @return the id of the country
   */

  public int getMostBattlesCountry() {
    int mostBattles = 0;
    for (int i = 0; i < battlefieldCountryCounter.length; i++) {
      if (mostBattles < battlefieldCountryCounter[i]) {
        mostBattles = battlefieldCountryCounter[i];
      }
    }
    return mostBattles;
  }

  /**
   * Gets the player who won the most battles.
   * 
   * @return the player id
   */
  
  public int getBestFighter() {
    int playerWins[] = new int[playerLength];
    for (int i = 0; i < allWinner.size(); i++) {
      playerWins[allWinner.get(i)]++;
    }
    int mostWins = 0;
    for (int i = 0; i < playerWins.length; i++) {
      if (mostWins < playerWins[i]) {
        mostWins = i;
      }
    }
    return mostWins;
  }

  /**
   * Gets the player who lost the most battles.
   * 
   * @return the id of the player
   */
  
  public int getWorstFighter() {
    int playerLosses[] = new int[playerLength];
    for (int i = 0; i < allLoser.size(); i++) {
      playerLosses[allLoser.get(i)]++;
    }
    int mostLoses = 0;
    for (int i = 0; i < playerLosses.length; i++) {
      if (mostLoses < playerLosses[i]) {
        mostLoses = i;
      }
    }
    return mostLoses;
  }

  /**
   * Gets the percentage of how many battles lead to conquering the country
   * 
   * @return the id of the country
   */
  
  public int getConquerPercentage() {
    int successes = 0;
    for (int i = 0; i < conqueredCountries.size(); i++) {
      if (conqueredCountries.get(i)) {
        successes++;
      }
    }
    return successes / conqueredCountries.size();
  }

  /**
   * Gets
   * 
   * @param playerID
   * @return
   */
  
  public int getAllTroops(int playerID) {
    int length = db.getMatches(playerID);
    int allTroops = 0;
    for (int i = 0; i < length; i++) {
      allTroops += Integer.parseInt(
          db.getGameData(Integer.toString(playerID), Integer.toString(i + 1), "TroopsUsed"));
    }
    return allTroops;

  }
  
  /**
   * Gets the amount of countries a player had at the peak of his empire.
   * 
   * @param playerID the id of the player
   * @return the amount of countries
   */

  public int getPeakCountries(int playerID) {
    for (int i = 0; i < biggestEmpire.length; i++) {
      if (biggestEmpire[i][0] == playerID) {
        return biggestEmpire[i][1];
      }
    }
    return 0;
  }
  
  /**
   * Gets the amount of troops a player had at the peak of his empire.
   * 
   * @param playerID the id of the player
   * @return the amount of troops
   */

  public int getPeakTroops(int playerID) {
    for (int i = 0; i < biggestEmpire.length; i++) {
      if (biggestEmpire[i][0] == playerID) {
        return biggestEmpire[i][2];
      }
    }
    return 0;
  }

  /**
   * Gets the outcome of all battles of a player, depending on whether he won it and whether he was attacker or target.
   * 
   * @param playerID the id of the player
   * @return a integer field with offensive battle wins, defensive battle wins, offensive battle losses, defensive battle losses
   */
  
  public int[] winAndLosses(int playerID) {
    int winOff = 0;
    int winDef = 0;
    int losOff = 0;
    int losDef = 0;
    for (int i = 0; i < allWinner.size(); i++) {
      if (playerID == allWinner.get(i)) {
        if (conqueredCountries.get(i)) {
          winOff++;
        } else {
          winDef++;
        }
      } else if (playerID == allLoser.get(i)) {
        if (conqueredCountries.get(i) == false) {
          losOff++;
        } else {
          losDef++;
        }
      }
    }
    return new int[] {winOff, winDef, losOff, losDef};
  }
  
  /**
   * Gets the elimination point / placement of a player.
   * 
   * @param playerID the id of the player
   * @return the place of the player
   */

  public int getPlacement(int playerID) {
    for (int i = 0; i < eliminationOrder.size(); i++) {
      if (eliminationOrder.get(i) == playerID) {
        return eliminationOrder.size() + 1 - i;
      }
    }
    return 1;
  }
  
  /**
   * Gets the elimination order of all players in a game. The first in the field is the first eliminated.
   * 
   * @return a field with the names of the player
   */

  public String[] getPlacements() {
    String[] output = new String[eliminationOrder.size()];
    for (int i = 0; i < eliminationOrder.size(); i++) {
      output[i] = Integer.toString(eliminationOrder.get(i));
    }
    return output;
  }
  
  /**
   * Fills the database with the statistics of a player.
   * 
   * @param playerId the id of the player
   */

  public void fillPlayerStatistics(int playerId) {

    // int biggestRival = 0;
    // for (int i = 0; i < playerRivalry[playerId - 1].length; i++) {
    // if (biggestRival < playerRivalry[playerId - 1][i]) {
    // biggestRival = i;
    // int rivalLoses = playerRivalry[playerId - 1][i];
    // db.setGameData("biggestRival",
    // db.getPlayerData(Integer.toString(i + 1), "name") + " / "
    // + Integer.toString(rivalLoses),
    // Integer.toString(playerId), db.getCurrentGameData("ID"));
    // }
    // }

    db.setGameData("placement", Integer.toString(getPlacement(playerId)),
        Integer.toString(playerId), this.db.getCurrentGameData("ID"));
    db.setGameData("mostTroops", Integer.toString(getPeakTroops(playerId)),
        Integer.toString(playerId), this.db.getCurrentGameData("ID"));
    db.setGameData("mostCountires", Integer.toString(getPeakCountries(playerId)),
        Integer.toString(playerId), this.db.getCurrentGameData("ID"));

    // db.setGameData("attacks", Integer.toString(playerAttacked.get(playerID)),
    // Integer.toString(playerID), db.getCurrentGameData("ID"));
    // db.setGameData("defences", Integer.toString(playerDefended.get(playerID)),
    // Integer.toString(playerID), db.getCurrentGameData("ID"));

    int[] battleStats = winAndLosses(playerId);
    this.db.setGameData("wonAttacks", Integer.toString(battleStats[0]), Integer.toString(playerId),
        db.getCurrentGameData("ID"));
    this.db.setGameData("wonDefences", Integer.toString(battleStats[1]), Integer.toString(playerId),
        db.getCurrentGameData("ID"));
    this.db.setGameData("lostAttacks", Integer.toString(battleStats[2]), Integer.toString(playerId),
        db.getCurrentGameData("ID"));
    this.db.setGameData("lostDefences", Integer.toString(battleStats[0]),
        Integer.toString(playerId), db.getCurrentGameData("ID"));
  }
  
  /**
   * Fills the database with the statistic of the last game.
   */
  
  public void fillGameStatistics() {
    this.db.setCurrentGameData("TroopsUsed", Integer.toString(getTotalTroops()));
    this.db.setCurrentGameData("mostContestedCountry", Integer.toString(getMostBattlesCountry()));
    this.db.setCurrentGameData("bestFighter", Integer.toString(getBestFighter()));
    this.db.setCurrentGameData("worstFighter", Integer.toString(getWorstFighter()));
    this.db.setCurrentGameData("conquerPercentage", Integer.toString(getConquerPercentage()));
    this.db.setCurrentGameData("battles", Integer.toString(totalBattles));
    String[] placements = this.getPlacements();
    for (int i = 0; i < placements.length; i++) {
      this.db.setCurrentGameData(i + 1 + ". place", placements[placements.length - i]);
    }
  }

  // public void fillGeneralPlayerStatistics(int playerID) {
  // db.setPlayerData("AllTroopsPlaced", Integer.toString(getAllTroops(playerID)),
  // Integer.toString(playerID));
  // db.setPlayerData("gamesPlayed", Integer.toString(db.getMatches(playerID)),
  // Integer.toString(playerID));
  // }
  
  public void setPlayerLength(int playerLength) {
    this.playerLength = playerLength;
  }


}
