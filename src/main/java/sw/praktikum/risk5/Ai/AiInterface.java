package sw.praktikum.risk5.Ai;

import java.io.File;
import sw.praktikum.risk5.Network.ClientInterface;

/**
 * Interface for AI
 * 
 * @author lroell
 *
 */
public interface AiInterface {

  /**
   * Method that receives a GameState JSON, calculates the AI's turn and sends back the needed
   * action-JSON
   * 
   * @author fahaerte
   */
  void performAction(File file);
  
  void setId(int id);
}
