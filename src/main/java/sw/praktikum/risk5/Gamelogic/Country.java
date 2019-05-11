package sw.praktikum.risk5.Gamelogic;

import java.util.HashSet;

/**
 * Class simulates a country in risk.
 *
 * @author fahaerte
 */

class Country {

  private String name;
  private int id;
  private int troops = 0;
  private Player owner = new Player(0,"");
  private HashSet<Country> neighbors = new HashSet<Country>();
  private ContinentEnum continent;

  /**
   * @author fahaerte
   * 
   * @param n The country's name
   * @param id The coutry's ID
   */
  protected Country(String n, int id) {
    this.name = n;
    this.id = id;
  }

  protected String getName() {
    return this.name;
  }

  protected int getTroops() {
    return this.troops;
  }

  protected Player getOwner() {
    return this.owner;
  }

  protected int getId() {
    return this.id;
  }

  protected HashSet<Country> getNeighbors() {
    return this.neighbors;
  }

  protected ContinentEnum getContinent() {
    return this.continent;
  }

  protected void addTroops(int t) {
    this.troops += t;
  }

  protected void setOwner(Player newOwner) {
    this.owner = newOwner;
  }

  protected void setContinent(ContinentEnum e) {
    this.continent = e;
  }

  protected void setNeighbors(Country[] neigh) {
    this.neighbors.clear();
    for (int i = 0; i < neigh.length; i++) {
      this.neighbors.add(neigh[i]);
    }
  }


  /**
   * This method checks if the country that is given by the parameter is a neighbor.
   * 
   * @param c The country to check whether it is a neighbor
   * @return boolean in case its true/false
   * @author gschakar
   */
  protected boolean isNeighbour(Country c) {
    boolean b = false;
    if (this.neighbors.contains(c)) {
      b = true;
    }
    return b;
  }
}

