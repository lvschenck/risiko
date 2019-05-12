package sw.praktikum.risk5.Gamelogic;

/**
 * Class that simulates a battle in risk.
 *
 * @author fahaerte
 */

class BattleHandler {

  private Dice dice;
  private Match currentMatch;
  private Statistics stats;

  /**
   * Constructer for the Battle-Handler.
   *
   * @param d Dice of the gameboard
   * @param m The current match
   * @param stats The statistic - Controller
   * @author fahaerte
   */
  protected BattleHandler(Dice d, Match m, Statistics stats) {
    this.dice = d;
    this.currentMatch = m;
    this.stats = stats;
  }

  /**
   * Compares the dice and determines the winner.
   *
   * @param diceAtt Integer-Array with the values of the attackers dice
   * @param diceDef Integer-Array with the values of the defenders dice
   * @return Boolean-Array which outlines the winner of each dice-comparison: true - the attacker
   * wins
   * @author fahaerte
   */

  protected boolean[] attackHandler(int[] diceAtt, int[] diceDef) {

    boolean[] win = new boolean[diceDef.length];
    diceAtt = this.dice.sortDice(diceAtt);
    diceDef = this.dice.sortDice(diceDef);

    for (int i = 0; i < diceDef.length; i++) {
      win[i] = false;

      if (diceAtt[i] > diceDef[i]) {
        win[i] = true;
      }

      diceDef[i] = 0;
    }
    diceAtt[0] = 0;
    diceAtt[1] = 0;
    diceAtt[2] = 0;
    return win;
  }

  /**
   * Simulates an attack in risk, resets the owner and the troops of the countries If something
   * fails, the method returns null.
   *
   * @param attackerId The ID of the attacker
   * @param attackerCountry The ID of the attacking country
   * @param attackingTroops The amount of attacking troops
   * @param defendingTroops The amount of defending troops
   * @param targetId The ID of the target player
   * @param targetCountry The ID of the attacked country
   * @author fahaerte
   */

  protected boolean attack(int attackerId, int attackerCountry, int attackingTroops,
      int defendingTroops, int targetId, int targetCountry) {

    Player winner = null;
    boolean countryConquered = false;

    Player attacker = this.currentMatch.findPlayer(attackerId);
    Player defender = this.currentMatch.findPlayer(targetId);
    Country attackingCountry = this.currentMatch.findCountry(attackerCountry);
    Country defendingCountry = this.currentMatch.findCountry(targetCountry);

    // Prueft alle Errors
    if ((!attackingCountry.getNeighbors().contains(defendingCountry)) || attackerId == targetId
        || !(attackerId == attackingCountry.getOwner().getId()) || attackingCountry.getTroops() <= 1
        || attackingCountry.getTroops() <= attackingTroops) {
      System.out.println("PIPI");
      return false;
    }

    int amountAtt = 0;
    int amountDef = 0;
    if (attackingTroops >= 3) {
      amountAtt = 3;
    } else {
      amountAtt = attackingTroops;
    }

    if (defendingTroops >= 2) {
      amountDef = 2;
    } else {
      amountDef = 1;
    }

    int[] diceAtt = this.dice.rollDice(amountAtt, true);
    int[] diceDef = this.dice.rollDice(amountDef, false);

    boolean[] battleResult = attackHandler(diceAtt, diceDef);

    //Truppen zerstoeren und Land neu besetzen
    for (int i = 0; i < battleResult.length; i++) {

      if (battleResult[i]) {// Angreifer gewinnt Wuerfel
        defendingCountry.addTroops(-1);

        if (defendingCountry.getTroops() == 0) { // Wenn Land erobert
          System.out.println("LOL");
          defendingCountry.setOwner(attackingCountry.getOwner());
          attackingCountry.getOwner().addCountry(defendingCountry);
          attackingCountry.getOwner().setConquered(true);
          countryConquered = true;
          this.currentMatch.checkAmountCountries();
          this.currentMatch.updateStatistics();
          attackingCountry.addTroops((-1) * attackingTroops);
          defendingCountry.addTroops(attackingTroops);
          break;
        }

      } else { // Verteidiger gewinnt Wuerfel
        System.out.println("verloren");
        attackingTroops--;
      }
    }

    // provisorisch, die Instanz der Klasse Statistics sollte woanders erstellt
    // werden
    // die BattleStatistics müssen aber hier erstellt werden
    this.stats.createBattleStatistics(attackingTroops, defendingTroops, targetCountry, attackerId,
        targetId, countryConquered);

    return true;
  }
}
