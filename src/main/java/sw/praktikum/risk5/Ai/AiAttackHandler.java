package sw.praktikum.risk5.Ai;

import java.util.ArrayList;

/**
 * Class that simulates the attack for the AI.
 * 
 * @author hakhtar, fahaerte
 *
 */
class AiAttackHandler {
  private AiType type;
  private int aiId;
  private AiCountry target;
  private AiCountry source;
  private int troopsToAttack;
  private boolean inProgress;
  private ArrayList<AiCountry> allCountries;
  private ArrayList<AiCountry> ownedCountries;

  /**
   * Constructor for the AiAttackHandler.
   * 
   * @author fahaerte
   * @param id The ID of the AI
   * @param t The strength of the AI
   */

  protected AiAttackHandler(int id, AiType t) {
    this.type = t;
    this.aiId = id;
    this.inProgress = false;
    this.allCountries = new ArrayList<AiCountry>(42);
    this.ownedCountries = new ArrayList<AiCountry>();
    this.troopsToAttack = 0;
  }

  /**
   * Performs the attack of the AI and invokes the right algorithms to calculate the AI's actions
   * due to it's strength.
   * 
   * @author fahaerte
   */
  protected void attack() {
    this.setOwnCountries();

    switch (this.type) {
      case HARD:
        this.setSourceCountryHard();
        this.setTargetCountryHard();
        this.setAmountToAttackHard();
        break;
      case MEDIUM:
        this.setSourceCountryMedium();
        this.setTargetCountryMedium();
        this.setAmountToAttackMedium();
        break;
      case EASY:
        this.setSourceCountryEasy();
        this.setTargetCountryEasy();
        this.setAmountToAttackEasy();
        break;
      default:
        break;
    }
  }

  /**
   * Sets the target country for the attack action. Algorithm for the hard AI.
   * 
   * @author fahaerte
   */
  private void setTargetCountryHard() {
    ArrayList<AiCountry> enemyNeighbors = new ArrayList<AiCountry>();
    this.target = new AiCountry(0, 0, 0);
    for (AiCountry a : this.source.getNeighborsSet()) {
      if (a.getOwner() != this.aiId) {
        enemyNeighbors.add(a); // alle feindl. NBL
      }
    }

    double ratioTroopsToEnemyTroops = 0.0;
    for (AiCountry t : enemyNeighbors) {
      if (this.source.getTroops() / t.getTroops() > ratioTroopsToEnemyTroops) {
        ratioTroopsToEnemyTroops = this.source.getTroops() / t.getTroops();
        this.target = t;
      }
    }
  }

  /**
   * Sets the source country of the attack action. Algorithm for the hard AI.
   * 
   * @author fahaerte
   */
  private void setSourceCountryHard() {
    int max = 0;
    this.source = null;
    for (AiCountry potentialSource : this.ownedCountries) {
      if (potentialSource.getTroops() > max) {
        max = potentialSource.getTroops();
        this.source = potentialSource;
      }
    }
  }

  /**
   * In this method the amount of troops to attack is calculated. Method for the hard AI Not
   * finalized, the behavior must be tested and optimized.
   * 
   * @author fahaerte
   */
  private void setAmountToAttackHard() {
    ArrayList<AiCountry> enemyNeighborsOfTarget = new ArrayList<AiCountry>();
    for (AiCountry a : this.target.getNeighborsSet()) {
      if (a.getOwner() != this.aiId) {
        enemyNeighborsOfTarget.add(a);
      }
    }

    if (enemyNeighborsOfTarget.size() == 0) {
      this.troopsToAttack = (int)
          (this.target.getTroops() + (this.target.getTroops() * this.target.getTroops()) / this.source.getTroops());
    } else {
      for (AiCountry n : enemyNeighborsOfTarget) {
        this.troopsToAttack =(int)
            (this.target.getTroops() + (this.target.getTroops() * this.target.getTroops()) / this.source.getTroops())
                + n.getTroops() + 1;
      }
      if (this.troopsToAttack > this.source.getTroops() - 1) {
        this.troopsToAttack = this.source.getTroops() - 1;
      }
    }
  }


  /**
   * In this method the target country is selected. That means the attacked country is chosen.
   * 
   * @author hakhtar
   */

  private void setTargetCountryMedium() {

    ArrayList<AiCountry> attackingCountries = new ArrayList<AiCountry>();
    this.target = new AiCountry(0, 0, 0);

    for (AiCountry c : this.source.getNeighborsSet()) {
      if (c.getOwner() != this.aiId) {
        attackingCountries.add(c);
      }
    }

    // Medium should attack countries which have at least the same size of troops

    for (AiCountry c1 : attackingCountries) {
      if (this.source.getTroops() >= c1.getTroops()) {
        this.target = c1;
      }
    }
  }

  /**
   * In this method the source country is selected. That means the attacking country is chosen.
   * 
   * @author hakhtar
   */

  private void setSourceCountryMedium() {

    ArrayList<AiCountry> attackingCountries = new ArrayList<AiCountry>();

    for (AiCountry c : this.ownedCountries) {
      for (AiCountry c1 : c.getNeighborsSet()) {
        if (c1.getOwner() != this.aiId) {
          attackingCountries.add(c1);
        }
      }

      if (attackingCountries.size() != 0) {
        for (AiCountry c1 : attackingCountries) {
          if (c.getTroops() > c1.getTroops()) {
            this.source = c;
          }
        }
      }
    }


  }

  /**
   * In this method the Amount of attacking troops for the medium AI is initialized.
   * 
   * @author hakhtar
   */

  private void setAmountToAttackMedium() {
    this.troopsToAttack = this.source.getTroops() - 1;
  }

  /**
   * In this method the the target country is chosen for the AI easy mode.
   * 
   * @author hakhtar
   */

  private void setTargetCountryEasy() {

    ArrayList<AiCountry> attackingCountries = new ArrayList<AiCountry>();
    this.target = new AiCountry(0, 0, 0);

    for (AiCountry c : this.source.getNeighborsSet()) {
      if (c.getOwner() != this.aiId) {
        attackingCountries.add(c);
      }
    }

    for (AiCountry c1 : attackingCountries) {
      this.target = c1;
    }
  }

  /**
   * In this method the source (attacking) country is selected for the easy mode AI.
   * 
   * @author hakhtar
   * 
   */

  private void setSourceCountryEasy() {

    this.source = new AiCountry(0, 0, 0);
    for (AiCountry c1 : this.ownedCountries) {
      if (c1.getTroops() > 1) {
        this.source = c1;
      }
    }
  }

  /**
   * In this method the amount of attacking troops from the source country is calculated.
   * 
   * @author hakhtar
   */

  private void setAmountToAttackEasy() {
    this.troopsToAttack = this.source.getTroops() - 1;
  }

  /**
   * Sets true if the AI wants to attack another time, false if not
   * 
   * @author fahaerte
   */
  private void setStillInProgressHard() {
    boolean continueAttack = false;
    for (AiCountry a : this.ownedCountries) {
      for (AiCountry n : a.getNeighborsSet()) {
        if (n.getOwner() != this.aiId
            && a.getTroops() > n.getTroops() * AiParameters.ratioToConquerCountry) {
          continueAttack = true;
        }
      }
    }
    this.inProgress = continueAttack;
  }

  /**
   * this method sets true if the medium mode ai still wants to attaack
   * 
   * @author hakhtar
   */

  private void setStillInProgressMedium() {
    this.inProgress = false;
    for (AiCountry c : this.ownedCountries) {
      if (c.getTroops() > 1) {
        for (AiCountry c2 : c.getNeighborsSet()) {
          if ((c2.getOwner() != this.aiId) && c.getTroops() > 2 && c.getTroops() > c2.getTroops()) {
            this.inProgress = true;
          }
        }
      }
    }
  }


  /**
   * this method sets true if the easy mode ai still wants to attack
   * 
   * @author hakhtar
   */

  private void setStillInProgressEasy() {
    boolean continueAttacking = false;
    for (AiCountry c : this.ownedCountries) {
      if (c.getTroops() > 1) {
        for (AiCountry c2 : c.getNeighborsSet()) {
          if (c2.getOwner() != this.aiId) {
            continueAttacking = true;
            break;
          }
        }
      }
    }
    this.inProgress = continueAttacking;
  }


  protected AiCountry getTargetCountry() {
    return this.target;
  }

  protected AiCountry getSourceCountry() {
    return this.source;
  }

  protected int getTroopsToAttack() {
    return this.troopsToAttack;
  }

  /**
   * Invokes the specified method according to the AI-level
   * 
   * @author fahaerte
   * @return boolean whether the AI wants to attack another time or not
   */
  protected boolean stillInProgress() {
    this.setOwnCountries();
    switch (this.type) {
      case HARD:
        this.setStillInProgressHard();
        break;
      case MEDIUM:
        this.setStillInProgressMedium();
        break;
      case EASY:
        this.setStillInProgressEasy();
        break;
    }
    return this.inProgress;
  }

  protected void setAllCountries(ArrayList<AiCountry> c) {
    this.allCountries = c;
  }

  /**
   * Checks for every country if it's owned by the AI. If so it saves the country in the instance
   * ownedCountries
   * 
   * @author fahaerte
   * 
   */
  private void setOwnCountries() {
    this.ownedCountries.clear();
    for (AiCountry a : this.allCountries) {
      if (a.getOwner() == this.aiId) {
        this.ownedCountries.add(a);
      }
    }
  }
}
