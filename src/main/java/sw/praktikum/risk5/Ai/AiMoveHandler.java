package sw.praktikum.risk5.Ai;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class that simulates the move operation of the AI.
 * 
 * @author gschakar
 *
 */
class AiMoveHandler {

  private ArrayList<AiCountry> allCountriesFromAi;
  private int aiId;
  private AiCountry source;
  private AiCountry target;
  private int amount;
  private AiType type;

  /**
   * Constructor for the AiMoveHandler.
   * 
   * @author fahaerte
   * @param aiId The ID of the AI
   * @param t The strength of the AI
   */
  protected AiMoveHandler(int id, AiType t) {
    this.allCountriesFromAi = new ArrayList<AiCountry>();
    this.aiId = id;
    this.source = null;
    this.target = null;
    this.type = t;
    this.amount = 0;
  }

  protected void move() {
    switch (this.type) {
      case HARD:
      case MEDIUM:
        this.checkPotentialSourceCountry();
        this.selectTargetCountry();
        this.calculateAmount();
        break;
      case EASY:
        this.amount = 0;
        this.source = null;
        this.target = null;
        break;
      default:
        break;
    }
  }

  private void calculateAmount() {
    if (this.source != null) {
      this.amount = this.source.getTroops() - 1;
    } else {
      this.amount = 0;
    }
  }

  /**
   * This method initializes all Countries that belongs to the Ai.
   * 
   * @param countrySet: the parameter is a HashSet of countries
   * @author gschakar
   */

  protected void setAllCountriesFromAi(ArrayList<AiCountry> countrySet) {
    this.allCountriesFromAi = countrySet;
  }

  /**
   * This method returns the a Country who's owner is the AI, that is the best Option to send troops
   * from, by checking all countries if it has more than 2 troops and no enemy neighbors.
   * 
   * @return the Country that is the bestOption
   * @author gschakar
   */

  private void checkPotentialSourceCountry() {
    ArrayList<AiCountry> potentialSources = new ArrayList<AiCountry>();

    for (AiCountry aiC : this.allCountriesFromAi) {
      ArrayList<AiCountry> neighborsOfaiC = aiC.getNeighborsSet();
      if (aiC.getTroops() > 1) {
        for (AiCountry tmp : neighborsOfaiC) {
          if (tmp.getOwner() == this.aiId) {
            potentialSources.add(aiC);
          }
        }
      }
    }
    if (potentialSources.size() != 0) {
      this.source = potentialSources.get(0);
      for (AiCountry aiSelect : potentialSources) {
        if (aiSelect.getNeighbor().length < this.source.getNeighbor().length) {
          this.source = aiSelect;
        }
      }
    } else {
      this.source = null;
    }
  }

  /**
   * This method selects the Country that is the best option for receiving troops for the country
   * which sends troops, by checking all neighbors of the neighbors from the country which sends the
   * troops and deciding which of these has the most enemy neighbors.
   * 
   * @return country which needs the sended troops the most
   * @author gschakar
   */

  private void selectTargetCountry() {
    if (this.source != null) {
      AiCountry countryTarget = null;
      int counter = 0;
      int targetCounter = 0;
      ArrayList<AiCountry> potentialCountry = this.source.getNeighborsSet();
      for (AiCountry a : potentialCountry) {
        ArrayList<AiCountry> neighborsOfPotentialCountry = a.getNeighborsSet();
        for (AiCountry b : neighborsOfPotentialCountry) {
          if (b.getOwner() != this.aiId) {
            counter++;
          }
        }
        for (AiCountry b : neighborsOfPotentialCountry) {
          if (countryTarget == null) {
            countryTarget = b;
            targetCounter = counter;
          } else if (counter > targetCounter) {
            countryTarget = b;
            targetCounter = counter;
          }
        }
      }
      this.target = countryTarget;
    } else {
      this.target = null;
    }
  }

  protected int getAmount() {
    return this.amount;
  }

  protected AiCountry getSourceCountry() {
    return this.source;
  }

  protected AiCountry getTargetCountry() {
    return this.target;
  }
}
