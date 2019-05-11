package sw.praktikum.risk5.Ai;

import java.io.File;
import sw.praktikum.risk5.Json.ReadJson;
import sw.praktikum.risk5.Json.WriteJson;

class AiTest {

  public static void main(String[] args) {
    WriteJson writer = new WriteJson();
    ReadJson reader = new ReadJson();
    reader.readRiskGetGameStateJSON(new File("src/main/resources/JSON/risk_getGameState.json"));
    int[][] neigh = reader.getGameStateNeighbours();
    String[] names = new String[42];
    int[] countriesId = new int[42];
    int[] countriesOwner = new int[42];
    int[] countriesUnits = new int[42];
    int[] pIds = {1, 2};
    int[] playerTroops = {10, 20};
    int[][] playerCards = {{1, 3, 0, 0, 0}, {0, 0, 0, 0, 0}};
    String[] pNames = {"a", "b"};
    boolean[] pAi = {true, false};
    for (int i = 0; i < 42; i++) {
      names[i] = "a";
      countriesId[i] = i + 1;
      countriesOwner[i] = i % 2 + 1;
      countriesUnits[i] = 1;
    }

//    AiMain ai = new AiMain(AiType.HARD, 1, null);

//    File result = ai.performAction(writer.writeGetGameStateJson(names, countriesId, countriesOwner,
//        countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops, playerCards, 1, 0, 1));
//    reader.readPlaceJSON(result);
//
//    countriesUnits[reader.getPlaceCountry() - 1] += reader.getPlaceNumberOfUnits();
//    result = ai.performAction(writer.writeGetGameStateJson(names, countriesId, countriesOwner,
//        countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops, playerCards, 1, 1, 1));
//
//    System.out.println("After place:");
//    for (int i = 0; i < 42; i++) {
//      System.out.println("ID: " + countriesId[i]);
//      System.out.println("Owner: " + countriesOwner[i]);
//      System.out.println("Units: " + countriesUnits[i]);
//    }
//    System.out.println();
//
//    result = ai.performAction(writer.writeGetGameStateJson(names, countriesId, countriesOwner,
//        countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops, playerCards, 1, 2, 1));
//    
//    reader.readMoveJSON(result);
//    System.out.println("Source country: " + reader.getMoveSourceCountry());
//    System.out.println("Target Country: " + reader.getMoveTargetCountry());
//    System.out.println("Troops: " + reader.getMoveUnitsInfantry());
    
//    reader.readAttackJSON(result);
//
//    System.out.println("attack");
//    System.out.println("amount Troops: " + reader.getAttackInfantry());
//    System.out.println("source country: " + reader.getAttackSourceCountry());
//    System.out.println("source player: " + reader.getAttackSourcePlayer());
//    System.out.println("target country: " + reader.getAttackTargetCountry());
//    System.out.println("target player: " + reader.getAttackTargetPlayer());
//    System.out.println("performed");
//    System.out.println();
//
//    countriesOwner[39] = 1;
//    countriesUnits[39] = 3;
//    countriesUnits[40] = 2;
//
//    result = ai.performAction(writer.writeGetGameStateJson(names, countriesId, countriesOwner,
//        countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops, playerCards, 1, 1, 1));
//
//    reader.readAttackJSON(result);
//
//    System.out.println("amount Troops: " + reader.getAttackInfantry());
//    System.out.println("source country: " + reader.getAttackSourceCountry());
//    System.out.println("source player: " + reader.getAttackSourcePlayer());
//    System.out.println("target country: " + reader.getAttackTargetCountry());
//    System.out.println("target player: " + reader.getAttackTargetPlayer());
//    System.out.println("performed");
  }
}
