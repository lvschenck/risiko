package sw.praktikum.risk5.Gamelogic;

/**
 * This class displays a die and contains several functions like rollDice and sort.
 *
 * @author fahaerte
 */

class Dice {

  /**
   * Rolls a specified amount of dice.
   *
   * @param amount The amount of dice
   * @param attacker true if attacker dice false if defender dice
   * @return Integer-Array with the result of the dice-roll
   * @author fahaerte
   */
  protected int[] rollDice(int amount, boolean attacker) {
    int[] result = null;
    if (attacker) {
      result = new int[3];
      for (int i = 0; i < amount; i++) {
        result[i] = (int) (Math.random() * 6 + 1);
      }
    } else {
      result = new int[amount];
      for (int i = 0; i < amount; i++) {
        result[i] = (int) (Math.random() * 6 + 1);
      }
    }
    return result;
  }

  /**
   * Sorts the Integer Array of the dice, max. length: 3.
   *
   * @param dice: Integer-Array with the value of each dice
   * @return The sorted Integer-Array
   * @author fahaerte
   */
  protected int[] sortDice(int[] dice) {

    if (dice.length == 1) {
      return dice;
    }

    if (dice.length == 2) {
      if (dice[0] >= dice[1]) {
        return dice;
      } else {
        int cache = dice[0];
        dice[0] = dice[1];
        dice[1] = cache;
      }
      return dice;
    }

    int cache = 0;

    if (dice[0] < dice[1]) {
      cache = dice[0];
      dice[0] = dice[1];
      dice[1] = cache;
    }
    if (dice[0] < dice[2]) {
      cache = dice[0];
      dice[0] = dice[2];
      dice[2] = cache;
    }
    if (dice[1] < dice[2]) {
      cache = dice[1];
      dice[1] = dice[2];
      dice[2] = cache;
    }
    return dice;
  }
}
