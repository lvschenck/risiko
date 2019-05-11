package sw.praktikum.risk5.Gamelogic;

import java.io.File;


/**
 * Interface for the GameController Used for Package - Communication
 * 
 * @author lroell, Fabian
 */
public interface GameControllerInterface {

  /**
   * Recieves the json data from the GUI
   * 
   * @author lroell
   * @param f JSON file to be read
   */
  void receiveData(File f);

  /**
   * Starts a game
   * 
   * @author lroell
   * @param ids all ids
   * @param usernames all usernames
   */
  void startGame(int[] ids, String[] usernames);

  /**
   * receives this message when a player 
   * @author lroell
   * @param id Message id
   */
  void receiveMessageLogoff(int id);
  
  /**
   * gets the current player length
   * @author lroell
   */
  int getPlayerLength();

}
