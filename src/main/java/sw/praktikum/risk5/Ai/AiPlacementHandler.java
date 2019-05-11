package sw.praktikum.risk5.Ai;

import java.util.ArrayList;


/**
 * This class contains the algorithms by which the AI decides how to place their troops at the
 * beginning of a match and at the beginning of every round.
 * 
 * @author gschakar, fahaerte
 */

class AiPlacementHandler {

  private ArrayList<AiCountry> allCountries;
  private double[] continentPriority;
  private final int[] continentIdsSortedByPriority = {6, 2, 1, 4, 3, 5};
  private final int[] continentSize = {9, 4, 6, 7, 12, 4};
  private int aiId;
  private AiType type;
  private AiCountry priorityAttackCountry;
  private AiCountry target;
  private int state;
  private int finalAmount;
  private ArrayList<AiCountry> priorityDefensiveCountries;

  /**
   * This method initializes all attributes of the class.
   * 
   * @param allCount this parameter represents a list of all countries
   * @param aiId this parameter represents the id of the AI
   * @author gschakar
   */
  protected AiPlacementHandler(int aiId, AiType t) {
    this.allCountries = new ArrayList<AiCountry>();
    this.continentPriority = new double[6];
    this.aiId = aiId;
    this.type = t;
    this.priorityAttackCountry = new AiCountry(0, 0, 0);
    this.priorityDefensiveCountries = new ArrayList<AiCountry>();
    this.target = null;
    this.state = 0;
    this.finalAmount = 0;
  }

  /**
   * Method that invokes needed methods according to the strength of the AI.
   * 
   * @author fahaerte
   * @param troopsToPlace Amount of available Troops
   */
  protected void calculate(int troopsToPlace) {

    if (this.state == 0) {
      switch (this.type) {
        case HARD:
          this.calculateContinentPriorityHard();
          this.placementHard(troopsToPlace);
          break;
        case MEDIUM:
          this.placementMedium(troopsToPlace);
          break;
        case EASY:
          this.placementEasy(troopsToPlace);
          break;
        default:
          break;
      }
    } else {
      switch (this.type) {
        case HARD:
          this.placementHard(troopsToPlace);
          break;
        case MEDIUM:
          this.placementMedium(troopsToPlace);
          break;
        case EASY:
          this.placementEasy(troopsToPlace);
          break;
        default:
          break;
      }
    }
    this.state++;
  }

  /**
   * This method calculates a priority for each continent Not finalized, the behavior must be tested
   * and optimized.
   * 
   * @author gschakar, fahaerte
   */

  private void calculateContinentPriorityHard() {
    for (int i = 0; i < this.continentPriority.length; i++) {
      if (this.checkOwningContinent(i + 1)) {
        this.continentPriority[i] = -1;
      } else {

        this.continentPriority[i] = AiParameters.continentsValue[i]
            * (AiParameters.weightForContinentPriority * this.getRatioCountriesInContinent(i + 1)
                + (1 - AiParameters.weightForContinentPriority)
                    * this.getRatioOfTroopsInContinent(i + 1))
            + AiParameters.continentsValue[i] / 10;
      }
    }
  }

  /**
   * This method creates a priority for each country in the continent with the highest priority Not
   * finalized, the behaviour must be tested and optimized.
   * 
   * @author gschakar, fahaerte
   */

  private void placementHard(int troopsToPlace) {

    if (this.state == 0) {
      ArrayList<AiCountry> countriesInContinent =
          this.getAiCountriesInContient(this.getMaxPriorityForContinent());

      for (AiCountry c : countriesInContinent) {
        c.setOffensivePriority(this.getRatioOfTroopsToNeighourTroops(c)
            + AiParameters.weightForBorderCountryA * c.getBorderCountry()
                * this.getRatioOfTroopsToNeighourTroops(c)
            + AiParameters.weightForAmountOfNeighbourCountries
                * c.getAmountOfEnemyNeighbors(this.aiId));
      }

      this.target = countriesInContinent.get(0);
      for (AiCountry c : countriesInContinent) {
        if (this.target.getOffensivePriority() < c.getOffensivePriority()) {
          this.target = c;
        }
      }

      int amountOff = this.calculateNeededTroops(this.target.getContinent(), troopsToPlace);
      this.finalAmount = amountOff;
      troopsToPlace -= amountOff;

      boolean owningContinent = false;

      if (troopsToPlace > 0) { // falls noch Truppen verfuegbar

        for (int i = 0; i < this.continentPriority.length; i++) {
          if (this.continentPriority[1] == -1) { // Falls ein kontinent unter kontrolle
            owningContinent = true;
            for (AiCountry c : this.getAiCountriesInContient(i + 1)) {
              // Wenn Grenzland von feindlichen Truppen besetzt
              if (c.getAmountOfEnemyTroopsInNeighborCountry(this.aiId) > 0) {
                // Truppenunterschied ist defensive Prioritaet
                c.setDefensivePriority(
                    c.getAmountOfEnemyTroopsInNeighborCountry(this.aiId) - c.getTroops());
                this.priorityDefensiveCountries.add(c);
              }
            }
          }
        }

        if (!owningContinent) {
          // Unterstuetzung des Haupt-Kontinents
          for (AiCountry a : this.getAiCountriesInContient(this.target.getContinent())) {
            a.setDefensivePriority(
                a.getAmountOfEnemyTroopsInNeighborCountry(this.aiId) - a.getTroops());
          }
        }
      }

    } else {

      if (this.priorityDefensiveCountries.size() == 0) { // Keine defensivlaener vorhanden: setze in
                                                         // letztes Ziel
        this.finalAmount = troopsToPlace;

      } else {
        // Setze defensivland mit hoechster Prio
        double max = this.priorityDefensiveCountries.get(0).getDefensivePriority();
        for (AiCountry c : this.priorityDefensiveCountries) {
          if (max <= c.getDefensivePriority()) {
            max = c.getDefensivePriority();
            this.target = c;
          }
        }
        this.priorityDefensiveCountries.remove(this.target);
        this.finalAmount = troopsToPlace - this.priorityDefensiveCountries.size();
      }
    }
  }

  /**
   * This method sets the placement for the medium AI by walking through the priority list of
   * continents.
   * 
   * @param availableTroops Parameter represents the amount of available troops to place
   * @author gschakar
   */
  private void placementMedium(int availableTroops) {
    AiCountry countryMostEnemies = null;
    int numberEnemies = 0;
    for (int i = 0; i < this.continentIdsSortedByPriority.length; i++) {
      if (this.getAiCountriesInContient(this.continentIdsSortedByPriority[i]).size() != 0
          && this.getAmountOfEnemyCountriesInContinent(this.continentIdsSortedByPriority[i]) != 0) {
        for (AiCountry a : this.getAiCountriesInContient(this.continentIdsSortedByPriority[i])) {
          if (a.getAmountOfEnemyNeighbors(this.aiId) > numberEnemies) {
            numberEnemies = a.getAmountOfEnemyNeighbors(this.aiId);
            countryMostEnemies = a;
          }
        }
        break;
      }
    }
    this.target = countryMostEnemies;
    this.finalAmount = availableTroops;
  }

  /**
   * This method sets the placement for the easy Ai by placing troops randomly in countries whose
   * owner is the Ai.
   * 
   * @param availableTroops represents available troops
   * @author gschakar
   */
  private void placementEasy(int availableTroops) {
    for (AiCountry a : this.allCountriesFromAi()) {
      if (true) {
        this.target = a;
        this.finalAmount = availableTroops;
        break;
      }
    }
  }

  /**
   * This method calculates the number of countries in a continent whose owner is the Ai.
   * 
   * @param continentId the continent is represented by his id
   * @return number of countries with the owner AI
   * @author gschakar
   */
  private ArrayList<AiCountry> getAiCountriesInContient(int continentId) {
    ArrayList<AiCountry> allAiCountriesInContinent = new ArrayList<AiCountry>();
    for (AiCountry a : this.allCountries) {
      if (a.getOwner() == this.aiId && a.getContinent() == continentId) {
        allAiCountriesInContinent.add(a);
      }
    }
    return allAiCountriesInContinent;
  }

  /**
   * This method counts and returns the amount of enemies in a continent.
   * 
   * @param continentId the continent is presented by his id
   * @return number of enemies in form of enemies in a continent
   * @author gschakar
   */
  private int getAmountOfEnemyCountriesInContinent(int continentId) {
    int counter = 0;
    for (AiCountry a : this.allCountries) {
      if (a.getContinent() == continentId && a.getOwner() != this.aiId && a.getOwner() != 0) {
        counter++;
      }
    }
    return counter;
  }

  /**
   * This methods calculates the ratio of the AI countries in comparison to enemy countries in a
   * specific continent.
   * 
   * @param continentId: the continent is represented by his id
   * @return: the ratio
   * @author gschakar
   */
  private double getRatioCountriesInContinent(int continentId) {
    if (this.getAmountOfEnemyCountriesInContinent(continentId) == 0) {
      return 0.0;
    }
    return this.getAiCountriesInContient(continentId).size()
        / this.getAmountOfEnemyCountriesInContinent(continentId);
  }

  /**
   * This method counts and returns the amount of all troops that do not belong to the AI in a
   * specific continent.
   * 
   * @param continentId the continent is represented by his id
   * @return number of troops
   * @author gschakar
   */
  private int getAmountOfTroopsInContinent(int continentId) {
    int amount = 0;
    for (AiCountry a : this.allCountries) {
      if (a.getContinent() == continentId && a.getOwner() == this.aiId) {
        amount += a.getTroops();
      }
    }
    return amount;
  }

  /**
   * This method counts and returns all countries in a continent whose owner is not the AI.
   * 
   * @param continentId the continent you check is given by his id
   * @return the method returns the amount of enemy troops in a continent
   * @author gschakar
   */
  private int getAmountOfEnemyTroopsInContinent(int continentId) {
    int amount = 0;
    for (AiCountry a : this.allCountries) {
      if (a.getContinent() == continentId && a.getOwner() != this.aiId && a.getOwner() != 0) {
        amount += a.getTroops();
      }
    }
    return amount;
  }

  /**
   * This method calculates the ration of the troops of the AI in comparison to all enemy troops.
   * 
   * @param continentId the parameter represents the continent you want to check on
   * @return ratio in form of a double
   * @author gschakar
   */
  private double getRatioOfTroopsInContinent(int continentId) {
    if (this.getAmountOfEnemyTroopsInContinent(continentId) == 0) {
      return 0.0;
    }
    return this.getAmountOfTroopsInContinent(continentId)
        / this.getAmountOfEnemyTroopsInContinent(continentId);
  }

  /**
   * This methods returns the continent with the highest priority in order of the geographical.
   * location
   * 
   * @return id the return is the id of the continent
   * @author gschakar
   */
  private int getMaxPriorityForContinent() {
    double max = 0.0;
    int id = 0;
    for (int i = 0; i < this.continentPriority.length; i++) {
      if (this.continentPriority[i] > max) {
        id = i;
        max = this.continentPriority[i];
      }
    }
    return id + 1;
  }

  /**
   * This method checks if the AI is the owner of the whole continent.
   * 
   * @param continentId the continent you want to check is represented by the id in form of an int
   * @return: boolean whether the AI is the owner of the whole continent or is not
   * @author gschakar
   */
  private boolean checkOwningContinent(int continentId) {
    for (AiCountry a : this.allCountries) {
      if (a.getContinent() == continentId && a.getOwner() != this.aiId) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method calculates the ratio of your troops in comparison of all enemies neighbor troops.
   * 
   * @param c the parameter is a AiCountry of which you want to check the neighbors
   * @return ratio in form of a double
   * @author gschakar
   */
  private double getRatioOfTroopsToNeighourTroops(AiCountry c) {
    int amount = 0;
    for (AiCountry a : this.allCountries) {
      if (a.getOwner() != this.aiId) {
        amount += a.getTroops();
      }
    }

    if (amount == 0) {
      return 0.0;
    }
    return c.getTroops() / amount;
  }

  /**
   * This method returns a boolean whether you could potentially conquer a whole continent with the
   * troops of the attckPrioCountry and the available troops.
   * 
   * @param continentId the continent is represented by the id
   * @param availableTroops represents the available troops
   * @return true/false
   * @author gschakar
   */
  private boolean enoughTroopsInCountryToConquerWholeContinent(int continentId,
      int availableTroops) {
    if (this.getAmountOfEnemyTroopsInContinent(continentId) == 0) {
      return true;
    }
    if ((this.priorityAttackCountry.getTroops() + availableTroops)
        / this.getAmountOfEnemyTroopsInContinent(
            continentId) >= AiParameters.ratioForConqueringWholeContinent) {
      return true;
    }
    return false;
  }

  /**
   * This method calculates the troops that the attackPrioCountry needs to conquer the whole
   * continent.
   * 
   * @param continentId the continent is represented by the id
   * @return the amount of troops that are needed
   * @author gschakar
   */
  private int calculateNeededTroops(int continentId, int troopsToPlace) {
    int amount = 0;
    if (this.enoughTroopsInCountryToConquerWholeContinent(continentId, troopsToPlace)) {
      amount += (int) ((this.getAmountOfEnemyTroopsInContinent(continentId) + 0.3)
          * AiParameters.ratioForConqueringWholeContinent);
    } else {
      if (this.getBorderCountries(continentId).size() > 0) {
        amount = troopsToPlace - this.getBorderCountries(continentId).size();
      } else {
        amount = troopsToPlace;
      }
    }
    if (amount > troopsToPlace) {
      amount = troopsToPlace;
    }
    return amount;
  }

  /**
   * This method returns an ArrayList of Countries which owner is the ai and which are border
   * countries as well.
   * 
   * @param continentId the continent is represented by the id
   * @return ArrayList of countries
   * @author gschakar
   */
  private ArrayList<AiCountry> getBorderCountries(int continentId) {
    ArrayList<AiCountry> bc = new ArrayList<AiCountry>();
    for (AiCountry a : this.allCountries) {
      if (a.getOwner() == this.aiId && a.getBorderCountry() == 1
          && a.getContinent() == continentId) {
        bc.add(a);
      }
    }
    return bc;
  }

  /**
   * This method places at round start troops in a country of the continent with the highest
   * priority.
   * 
   * @author fahaerte
   */
  protected AiCountry placeInEmptyCountries() {
    switch (this.type) {
      case HARD:
        return this.placeInEmptyCountriesHard();
      case MEDIUM:
        return this.placeInEmptyCountriesMedium();
      case EASY:
        return this.placeInEmptyCountriesEasy();
      default:
        break;
    }
    return null;
  }

  /**
   * This method places at the start of a match troops in a country of the continent with the
   * highest priority for the hard AI.
   * 
   * @author gschakar, fahaerte
   */
  private AiCountry placeInEmptyCountriesHard() {
    
    for (int i = 0; i < 6; i++) {
      if(this.getAiCountriesInContient(i+1).size() == 0 && this.getAmountOfEnemyCountriesInContinent(i+1) == continentSize[i] - 1
          && !this.moreThanOneEnemiyInContient(i+1)) {
        for (AiCountry a : this.allCountries) {
          if (a.getOwner() == 0 && a.getContinent() == i + 1) {
            System.out.println("true");
            return a;
          }
        }
      }
    }
    
    this.calculateContinentPriorityHard();
    int preferedContinent = 0;
    AiCountry target = null;
    ArrayList<AiCountry> targets = new ArrayList<AiCountry>();
    boolean continentOccupied = true;
    while (continentOccupied) {
      preferedContinent = this.getMaxPriorityForContinent();
      for (AiCountry a : this.allCountries) {
        if (a.getOwner() == 0 && a.getContinent() == preferedContinent) {
          targets.add(a);
          continentOccupied = false;
        }
      }
      if (continentOccupied) {
        this.continentPriority[this.getMaxPriorityForContinent() - 1] = 0;
      }
    }

    for (AiCountry a : targets) {
      a.setOffensivePriority(-1 * a.getAmountOfEnemyNeighbors(this.aiId));
    }

    target = targets.get(0);
    for (AiCountry a : targets) {
      if (a.getOffensivePriority() > target.getOffensivePriority()) {
        target = a;
      }
    }
    target.setTroopsToPlace(1);
    target.setOwner(this.aiId);
    return target;
  }

  /**
   * This method sets the placement for the medium AI in the beginning of a match.
   * 
   * @return output: the country you place troops in
   * @author gschakar
   */
  private AiCountry placeInEmptyCountriesMedium() {
    boolean breakFor = false;
    AiCountry output = null;

    for (int i = 0; i < this.continentIdsSortedByPriority.length; i++) {
      for (AiCountry a : this.allCountries) {
        if (a.getContinent() == this.continentIdsSortedByPriority[i] && a.getOwner() == 0) {
          output = a;
          a.setOwner(this.aiId);
          a.setTroops(1);
          breakFor = true;
          break;
        }
        if (breakFor) {
          break;
        }
      }
      if (breakFor) {
        break;
      }
    }
    return output;
  }

  /**
   * This method sets the placement for the easy AI at the beginning of a match.
   * 
   * @return An AiCountry you place troops in randomly
   * @author gschakar
   */
  private AiCountry placeInEmptyCountriesEasy() {
    AiCountry output = null;

    for (AiCountry a : this.allCountries) {
      if (a.getOwner() == 0) {
        output = a;
        a.setOwner(this.aiId);
        a.setTroops(1);
        break;
      }
    }
    return output;
  }

  protected void setAllCountries(ArrayList<AiCountry> allCountries) {
    this.allCountries = allCountries;
  }

  protected AiCountry getTarget() {
    return this.target;
  }

  protected void setState(int s) {
    this.state = s;
  }

  /**
   * This method returns a HashSet of all countries that belong to the AI.
   * 
   * @return a hashSet of all countries whose owner is the AI
   * @author gschakar
   */
  protected ArrayList<AiCountry> allCountriesFromAi() {
    ArrayList<AiCountry> allCountriesFromAi = new ArrayList<AiCountry>();
    for (AiCountry a : this.allCountries) {
      if (a.getId() == this.aiId) {
        allCountriesFromAi.add(a);
      }
    }
    return allCountriesFromAi;
  }

  protected int getAmount() {
    return this.finalAmount;
  }
  
  /**
   * Proves if a continent is occupied by more than one player except of the AI.
   * 
   * @author fahaerte
   * @param continentId The required continent
   * @return True if there's more than one enemy player.
   */
  private boolean moreThanOneEnemiyInContient(int continentId) {
    int owner = 0;
    int amount = 0;
    for (AiCountry a : this.allCountries) {
      if (a.getOwner() != 0 && a.getOwner() != this.aiId && a.getOwner() != owner && a.getContinent() == continentId) {
        owner = a.getOwner();
        amount++;
      }
    }
    if (amount > 1) {
      return true;
    }
    return false;
  }
}
