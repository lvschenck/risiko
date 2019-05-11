package sw.praktikum.risk5.Ai;

import java.util.ArrayList;

/**
 * This class defines an AiCountry with all attributes that are needed.
 * 
 * @author gschakar
 */

class AiCountry {
  private int continent;
  private int owner;
  private int id;
  private int[] neighbor;
  private ArrayList<AiCountry> neighborsSet;
  private int troops;
  private double offensivePriority;
  private double defensivePriority;
  private int isBorderCountry;
  private int troopsToPlace;

  protected AiCountry(int con, int o, int i) {
    this.continent = con;
    this.owner = o;
    this.id = i;
    this.offensivePriority = 0.0;
    this.defensivePriority = 0.0;
    this.isBorderCountry = 0;
    if (i == 1 || i == 6 || i == 5 || i == 10 || i == 12 || i == 14 || i == 16 || i == 19 || i == 20
        || i == 21 || i == 22 || i == 24 || i == 29 || i == 30 || i == 31 || i == 33 || i == 38
        || i == 39) {
      this.isBorderCountry = 1;
    }
    this.troopsToPlace = 0;
    this.neighborsSet = new ArrayList<AiCountry>();
  }

  /**
   * Set the amount of Troops for the Placement - Method.
   * 
   * @param amount: Amount of troops
   */
  protected void setTroopsToPlace(int amount) {
    this.troopsToPlace = amount;
  }

  protected int getTroopsToPlace() {
    return this.troopsToPlace;
  }

  protected int getBorderCountry() {
    return this.isBorderCountry;
  }

  protected double getOffensivePriority() {
    return this.offensivePriority;
  }

  protected void setOffensivePriority(double p) {
    this.offensivePriority = p;
  }

  protected double getDefensivePriority() {
    return this.defensivePriority;
  }

  protected void setDefensivePriority(double p) {
    this.defensivePriority = p;
  }

  protected int getContinent() {
    return continent;
  }

  protected void setContinent(int continent) {
    this.continent = continent;
  }

  protected int getOwner() {
    return owner;
  }

  protected void setOwner(int owner) {
    this.owner = owner;
  }

  protected int getId() {
    return id;
  }

  protected int[] getNeighbor() {
    return neighbor;
  }

  protected ArrayList<AiCountry> getNeighborsSet() {
    return this.neighborsSet;
  }

  protected void setNeighbor(int[] neighbor) {
    this.neighbor = neighbor;
  }

  protected void setNeighbor(AiCountry c) {
    this.neighborsSet.add(c);
  }

  protected void setTroops(int troops) {
    this.troops = troops;
  }

  protected int getTroops() {
    return troops;
  }

  /**
   * This method counts the amount of enemy neighbors of the current country.
   * 
   * @param aiId the country is represented with the Id
   * @return amount returns amount of enemies
   * @author gschakar
   */
  protected int getAmountOfEnemyNeighbors(int aiId) {
    int amount = 0;
    for (AiCountry a : this.getNeighborsSet()) {
      if (a.owner != aiId && a.owner != 0) {
        amount++;
      }
    }
    return amount;
  }

  /**
   * This method return the amount of all neighbor troops.
   * 
   * @param aiId: represents the Ai id.
   * @return amount: int number of amount of all neighbors.
   * @author gschakar
   */
  protected int getAmountOfEnemyTroopsInNeighborCountry(int aiId) {
    int amount = 0;
    for (AiCountry a : this.getNeighborsSet()) {
      if (a.getOwner() != aiId && a.getOwner() != 0) {
        amount += a.getTroops();
      }
    }
    return amount;
  }
}
