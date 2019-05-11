package sw.praktikum.risk5.Database;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.XMLOutputter;

/**
 * This class writes / reads data from the statistics.xml file, which is used as database.
 * 
 * @author sdoehler
 *
 */
public class Database {

  private static Document statistics;
  private static List<Element> savedPlayers, playedGames;
  private static int matchId, playerId;

  /**
   * When you construct a database the existing statistics from the xml file are integrated.
   */
  public Database() {
    this.parseExistingStatistics();
  }

  /**
   * The userID gets returned.
   * 
   * @param player The name of the player whose ID you are looking for.
   * @return The ID, or 0 if the player doesn't exist.
   */
  public int getUserId(String player) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("name").equals(player)) {
        return Integer.parseInt(savedPlayers.get(i).getAttributeValue("ID"));
      }
    }
    return 0;
  }

  /**
   * Checks if a user is saved in the database, returns true if it is.
   * 
   * @param player The name of the player which gets checked.
   * @return whether the user exists
   */
  public boolean checkUser(String player) {
    if (savedPlayers.isEmpty()) {
      return false;
    }
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (player.isEmpty()) {
        return false;
      }
      if (savedPlayers.get(i).getAttributeValue("name").equals(player)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks whether the name and password exists in this combination in the database.
   * 
   * @param player the user name of the account
   * @param password the password for the user account
   * @return Whether the user exists and the password is correct.
   */

  public boolean checkPassword(String player, String password) {
    if (checkUser(player)) {
      for (int i = 0; i < savedPlayers.size(); i++) {
        if (savedPlayers.get(i).getAttributeValue("name").equals(player)) {
          return savedPlayers.get(i).getAttributeValue("password").equals(password);
        }
      }
      return false;
    }
    return false;
  }

  /**
   * Creates a new user composed of the parameter name and password.
   *
   * @param player The name which the account should get.
   * @param password The password which the account should get.
   * @return Whether the operation was successful and the player name didn't exist before.
   */
  public boolean createUser(String player, String password) {
    if (player.isEmpty() || password.isEmpty()) {
      return false;
    }
    if (!checkUser(player)) {
      playerId = savedPlayers.size();
      Element newPlayer = new Element(player);
      newPlayer.setAttribute("name", player);
      newPlayer.setAttribute("password", password);
      newPlayer.setAttribute("ID", Integer.toString(++playerId));
      savedPlayers.add(newPlayer);
      this.saveChanges();
      return true;
    }
    return false;
  }

  /**
   * Deletes the player which is given through the parameter player name
   *
   * @param player the name of the player to delete
   * @return Whether the player was deleted successful.
   */

  public boolean deleteUser(String player) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("name").equals(player)) {
        savedPlayers.remove(i);
        this.saveChanges();
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a temporary user in the database.
   * 
   * @param player the name the temporary user has
   * @return Whether the temporary user was created successfully.
   */

  public boolean createTempUser(String player) {
    if (checkUser(player) == false) {
      playerId = savedPlayers.size();
      Element newPlayer = new Element(player);
      newPlayer.setAttribute("name", player);
      newPlayer.setAttribute("ID", Integer.toString(++playerId));
      savedPlayers.add(newPlayer);
      saveChanges();
      return true;
    }
    return false;
  }

  /**
   * Creates 5 AIs in the database.
   */

  public void createAis() {
    createAis(5);
  }
  
  /**
   * Creates a certain amount of AIs.
   * 
   * @param amount the amount of ais.
   */
  
  public void createAis(int amount) {
    for (int i = 1; i < amount+1; i++) {
      Element ai = new Element("ai " + i);
      ai.setAttribute("name", "AI" + i);
      ai.setAttribute("ID", Integer.toString(-i));
      savedPlayers.add(ai);
    }
    saveChanges();
  }

  /**
   * Removes all KIs in the database.
   */
  public void deleteAis() {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (Integer.parseInt(savedPlayers.get(i).getAttributeValue("ID")) < 0) {
        savedPlayers.remove(i);
      }
    }
  }

  /**
   * Gets statistic for a player as they are presented if the game lobby
   * 
   * @param id the player whose statistic you want
   * @return a list with the attributes
   */
  public List<Attribute> getStatisticsLobby(String id) {
    List<Element> list = new LinkedList<Element>();
    List<Attribute> atts = new LinkedList<Attribute>();

    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("ID").equals(id)) {
        list = savedPlayers.get(i).getChildren();
        for (int k = 0; k < list.size(); k++) {
          atts.addAll(list.get(k).getAttributes());
        }
      }
    }
    for (int i = 0; i < playedGames.size(); i++) {
      for (int k = 0; k < list.size(); k++) {
        if (list.get(k).getAttributeValue("ID")
            .equals(playedGames.get(i).getAttributeValue("ID"))) {
          atts.addAll(playedGames.get(i).getAttributes());
        }
      }
    }
    return atts;
  }

  /**
   * Gets the amount of players saved in the database.
   * 
   * @return amount of players
   */

  public int getPlayers() {
    return savedPlayers.size();
  }

  /**
   * Gets the amount of matches player by the given player.
   * 
   * @param playerId The player whose matches you want to get.
   * @return amount of matches played by the player
   */

  public int getMatches(int playerId) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("ID").equals(Integer.toString(playerId))) {
        return savedPlayers.get(i).getChildren().size();
      }
    }
    return 0;
  }

  /**
   * Creates a match in the database with the given players.
   * 
   * @param players The players which take part in the game.
   */

  public void initializeMatch(String[] players) {

    matchId = playedGames.size();
    Element match = new Element("match_" + ++matchId);
    match.setAttribute("ID", Integer.toString(matchId));
    for (int i = 0; i < players.length; i++) {
      for (int k = 0; k < savedPlayers.size(); k++) {
        if (players[i].equals(savedPlayers.get(k).getAttribute("name"))) {
          savedPlayers.get(k).addContent(match);
        }
      }
    }
    playedGames.add(match);
    saveChanges();
  }

  /**
   * Read the existing statistics from statistics.xml and creates the jdom2 objects which are used
   * to further changes.
   */
  private void parseExistingStatistics() {

    try {
      statistics = new SAXBuilder().build("src/main/resources/database/statistics.xml");

    } catch (JDOMException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Element wurzel = statistics.getRootElement();
    Element pl = ((Element) wurzel).getChild("players");
    Element pg = ((Element) wurzel).getChild("playedGames");
    savedPlayers = pl.getChildren();
    playedGames = pg.getChildren();
  }

  /**
   * Writes the changes, which were done through jdom2 objects, in the database (statistics.xml)
   */
  public void saveChanges() {

    XMLOutputter out = new XMLOutputter();
    FileWriter fw;
    try {
      fw = new FileWriter("src/main/resources/database/statistics.xml");
      out.output(statistics, fw);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the attribute value of the current
   * 
   * @param attribute the attribute you want to get the value of
   * @return the value as a string
   */

  public String getCurrentGameData(String attribute) {
    return playedGames.get(matchId).getAttributeValue(attribute);
  }

  /**
   * Sets a attribute value of the current game.
   * 
   * @param attribute
   * @param value
   */

  public void setCurrentGameData(String attribute, String value) {
    playedGames.get(matchId).setAttribute(attribute, value);
    saveChanges();
  }

  /**
   * Gets the attribute value of a certain player.
   * 
   * @param playerId the player you get the attribute of
   * @param attribute the attribute which you want to get the value of
   * @return the attribute value
   */

  public String getPlayerData(String playerId, String attribute) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("ID").equals(playerId)) {
        return savedPlayers.get(i).getAttributeValue(attribute);
      }
    }
    return null;
  }

  /**
   * Sets the attribute of the player you want.
   * 
   * @param attribute the attribute which is to change
   * @param value the value which will be set
   * @param playerId the player who will be changed
   */

  public void setPlayerData(String attribute, String value, String playerId) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("ID").equals(playerId)) {
        savedPlayers.get(i).setAttribute(attribute, value);
      }
    }
    saveChanges();
  }

  /**
   * Gets a value of game statistics in which the player participated
   * 
   * @param playerId the player you ask the game statistics of
   * @param gameId the game whose statistic you want to get
   * @param attribute the attribute which you want the value of
   * @return the value of the attribute
   */

  public String getGameData(String playerId, String gameId, String attribute) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("ID").equals(playerId)) {
        if (savedPlayers.get(i).getChildren().isEmpty()) {
          return null;
        } else {
          List<Element> games = savedPlayers.get(i).getChildren();
          for (int k = 0; k < savedPlayers.get(i).getChildren().size(); k++) {
            if (games.get(k).getAttributeValue("ID").equals(gameId)) {
              return games.get(k).getAttributeValue(attribute);
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Sets the value of game statistics in which the player participated.
   * 
   * @param attribute the attribute which you want the value of
   * @param value the value which is to set
   * @param playerId the player whose game statistic you want to edit
   * @param game the game whichs statistic is to edit
   */

  public void setGameData(String attribute, String value, String playerId, String game) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (savedPlayers.get(i).getAttributeValue("ID").equals(playerId)) {
        if (savedPlayers.get(i).getChildren(game).isEmpty()) {
          Element newGame = new Element(game);
          newGame.setAttribute(attribute, value);
          savedPlayers.get(i).addContent(newGame);

        } else {
          savedPlayers.get(i).getChild(game).setAttribute(attribute, value);
        }
      }
    }
    saveChanges();
  }

  /**
   * Sends all attributes of a player to integrate it in another database.
   * 
   * @param playerId the player who you want to send
   * @return a field with all player attributes
   */

  public String[] sendPlayer(int playerId) {
    for (int i = 0; i < savedPlayers.size(); i++) {
      if (this.savedPlayers.get(i).getAttributeValue("ID").equals(Integer.toString(playerId))) {
        List<Element> matches = savedPlayers.get(i).getChildren();
        for (int k = 0; k < matches.size(); k++) {
          if (matches.get(k).getAttributeValue("ID").equals(Integer.toString(matchId))) {
            List<Attribute> atts = matches.get(k).getAttributes();
            String[] output = new String[atts.size()];
            for (int l = 0; l < atts.size(); l++) {
              output[l] = atts.get(l).getValue();
            }
            return output;
          }
        }

      }
    }
    return null;
  }

  /**
   * Sends a game as an element to integrate it in another database.
   * 
   * @return the game as an element
   */

  public Element sendGame() {
    return playedGames.get(playedGames.size());
  }

  /**
   * Creates a message with playerdata and a game to send it to another database.
   * 
   * @param playerId the player whose data is to send
   */

  public void sendMessageData(int playerId) {
    sendPlayer(playerId);
    sendGame();
  }

  /**
   * Integrates player data and a match, where the player participated, into the database.
   * 
   * @param player the player where the data is to add
   * @param genMatch the match with general statistics about it
   * @param data specific player statistics regarding the match
   */
  public void implementGame(String player, String[] data, Element genMatch) {
    if (checkUser(player)) {
      for (int i = 0; i < savedPlayers.size(); i++) {
        if (savedPlayers.get(i).getAttributeValue("name").equals(player)) {
          Element match = new Element("match_" + matchId);
          match.setAttribute("placement", data[0]);
          match.setAttribute("mostTroops", data[1]);
          match.setAttribute("mostCountries", data[2]);
          match.setAttribute("wonAttacks", data[3]);
          match.setAttribute("wonDefences", data[4]);
          match.setAttribute("lostAttacks", data[5]);
          match.setAttribute("lostDefences", data[6]);
          savedPlayers.get(i).addContent(match);
          genMatch.setAttribute("ID", Integer.toString(matchId++));
          playedGames.add(genMatch);
          saveChanges();
        }
      }
    }
  }
}
