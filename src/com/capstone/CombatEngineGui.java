package com.capstone;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

public class CombatEngineGui {
    //Class Fields


    //Constructor

    public CombatEngineGui() {

    }

    //Class Methods

    //Action Phase - this is the phase where the player gets to choose between fight, use item, or run (They cant run from trainer battles)
    //This method is not yet used. Stubbed out and reserved for the wild pokemon encounter.
    String actionPhaseChoiceWildPoke(){
        //The userChoice here is what the user chooses to do
        Scanner scanner = new Scanner(System.in);
        String userChoice = scanner.nextLine();

        if (!userChoice.equalsIgnoreCase("attack") && !userChoice.equalsIgnoreCase("item") && !userChoice.equalsIgnoreCase("run")){
            System.out.println("You can't do that.");
            actionPhaseChoiceWildPoke();
        }
        return userChoice;
    }
    //This method is for taking in user input if they want to attack or use item.
    //Basic input validation is used here to limit choices to attack or item.
    String actionPhaseChoiceTrainerBattle(){
        //The userChoice here is what the user chooses to do
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("What would you like to do? <attack> or <item>");
//        String userChoice = scanner.nextLine();

        String[] options = { "Attack","Use item" };
        String res = (String) JOptionPane.showInputDialog(null, "What would you like to do? <attack> or <item>", "Attack or use items",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
//
//
//        if (!userChoice.equalsIgnoreCase("attack") && !userChoice.equalsIgnoreCase("item")){
//            System.out.println("You can't do that.");
//            actionPhaseChoiceTrainerBattle();
//        }
        return res;
    }

    //Combat loop : encounter with trainer - this is the main combat loop that is called in the game engine.

    String combatLoopTrainer(Player player, NPCFactory npc, GameEngine game, PrintStream commonDisplayOut, PrintStream pokeDisplayOut, JTextArea pokeDisplay){

        String result = "";
        pokeDisplay.setText("");
        System.setOut(pokeDisplayOut);
        player.getPlayersPokemon().get(0).displayOutStatsAndAll();
        System.setOut(commonDisplayOut);


        //Runs until the player or the opponent is defeated.

        while (true){

            if(player.playersPokemon.get(0).getCurrentHealth() <= 0){
                result = "Player Lost";
                break;
            } else if (npc.npcPokemonList.get(0).getCurrentHealth() <= 0){

                //Reward exp to player's pokemon when the enemy pokemon is defeated.
                double expReward = npc.npcPokemonList.get(0).getLevel() * 10; //Current xp reward scales with level and is hard coded.
                player.playersPokemon.get(0).rewardEXP(expReward);

                //Reward Player money for winning.
                System.out.println("You received: 1000 for winning!"); //hard coded
                player.addMoney(1000); //hard coded 1000 money to add as reward.
                result = "NPC Lost";
                break;
            } else {
                player.playersPokemon.get(0).displayOutStatsAndAll();
                npc.npcPokemonList.get(0).displayOutStatsAndAll();

                String userChoice = actionPhaseChoiceTrainerBattle();

                //This processes user choice and applies attack from user's choice
                processActionPhase(userChoice,player,npc,game);
                //If opponent's pokemon's hp reaches 0, break out of combat loop.
                if (npc.npcPokemonList.get(0).getCurrentHealth() <= 0){
                    System.out.println("The opponent's Pokemon fainted!");
                    double expReward = npc.npcPokemonList.get(0).getLevel() * 10; //Current xp reward scales with level and is hard coded.
                    player.playersPokemon.get(0).rewardEXP(expReward);
                    System.out.println("You received: 1000 for winning!"); //hard coded
                    player.addMoney(1000); //hard coded 1000 money to add as reward.
                    result = "NPC Lost";
                    break;
                }
                //If player's pokemon's hp reaches 0, break out of combat loop
                opponentAttack(player,npc,game);
                pokeDisplay.setText("");
                System.setOut(pokeDisplayOut);
                player.getPlayersPokemon().get(0).displayOutStatsAndAll();
                System.setOut(commonDisplayOut);

                if (player.playersPokemon.get(0).getCurrentHealth() <= 0){
                    System.out.println("Your Pokemon fainted!");
                    result = "Player Lost";
                    break;

                }
            }
        }
        return result;
    }

    //This method is used in the main combat loop , runs after your attack move
    void opponentAttack(Player player, NPCFactory npc, GameEngine game){
        int opponentAttack;
        int opponentAttackChoice;
        //New arraylist object to hold the Player's Pokemon and seperate one for npc for combat phase
        ArrayList<Pokemon> playersPokemon = new ArrayList<>();
        ArrayList<Pokemon> npcPokemon = new ArrayList<>();

        //Set the arrayList to equal to the current player's pokemon
        playersPokemon = player.playersPokemon;
        npcPokemon = npc.npcPokemonList;

        //Get the first Pokemon from the arrayList
        Pokemon playerFirstPoke = playersPokemon.get(0);
        playerFirstPoke.displayOutStatsAndAll();

        //Get the npc's first Pokemon
        Pokemon npcFirstPoke = npcPokemon.get(0);
        npcFirstPoke.displayOutStatsAndAll();

        Random random = new Random();

        //Randomize opponent's move choice.
        opponentAttackChoice = random.nextInt(2);

        System.out.println("Opponent attacks!");


        if (opponentAttackChoice == 0){

            opponentAttack = npcFirstPoke.getMove1().attack(npcFirstPoke.getAttack());
            System.out.println("The opposing " + npcFirstPoke.getName() + " uses " + npcFirstPoke.getMove1().getAttackName());
            playerFirstPoke.takeDamage(opponentAttack);

        } else if (opponentAttackChoice == 1){

            opponentAttack = npcFirstPoke.getMove2().attack(npcFirstPoke.getAttack());
            System.out.println("The opposing " + npcFirstPoke.getName() + " uses " + npcFirstPoke.getMove2().getAttackName());
            playerFirstPoke.takeDamage(opponentAttack);
        }


    }
    //used in the main combat loop to process the action phase. does the damage calc, energy usage etc etc.
    void processActionPhase(String userChoice, Player player, NPCFactory npc, GameEngine game){

        //Scanner scanner = new Scanner(System.in);

        int playerPokeAttack;



        //New arraylist object to hold the Player's Pokemon and seperate one for npc for combat phase
        ArrayList<Pokemon> playersPokemon = new ArrayList<>();
        ArrayList<Pokemon> npcPokemon = new ArrayList<>();

        //Set the arrayList to equal to the current player's pokemon
        playersPokemon = player.playersPokemon;
        npcPokemon = npc.npcPokemonList;

        //Get the first Pokemon from the arrayList
        Pokemon playerFirstPoke = playersPokemon.get(0);
        playerFirstPoke.displayOutStatsAndAll();

        //Get the npc's first Pokemon
        Pokemon npcFirstPoke = npcPokemon.get(0);
        npcFirstPoke.displayOutStatsAndAll();

        //If userChoice is attack
        if (userChoice.equalsIgnoreCase("Attack")){
            System.out.println("Which attack would you like to use?");
            playerFirstPoke.getMove1().displayOutAttackStats(playerFirstPoke.getLevel());
            playerFirstPoke.getMove2().displayOutAttackStats(playerFirstPoke.getLevel());
            String[] attacks = {playerFirstPoke.getMove1().getAttackName(),playerFirstPoke.getMove2().getAttackName(), "back"};
            String res = (String) JOptionPane.showInputDialog(null, "Which attack would you like to use?", "Attacks",
                    JOptionPane.PLAIN_MESSAGE, null, attacks, attacks[0]);


            //String attackChoice = scanner.nextLine();
            //If user between attack move one or two
            if (res.equalsIgnoreCase(playerFirstPoke.getMove1().getAttackName())){
                System.out.println(playerFirstPoke.getName() + " use " + playerFirstPoke.getMove1().getAttackName());
                playerPokeAttack = playerFirstPoke.getMove1().attack(playerFirstPoke.getAttack());
                playerFirstPoke.getMove1().attackUsed();
                npcFirstPoke.takeDamage(playerPokeAttack);
            } else if (res.equalsIgnoreCase(playerFirstPoke.getMove2().getAttackName())){
                System.out.println(playerFirstPoke.getName() + " use " + playerFirstPoke.getMove2().getAttackName());
                playerPokeAttack = playerFirstPoke.getMove2().attack(playerFirstPoke.getAttack());
                playerFirstPoke.getMove2().attackUsed();
                npcFirstPoke.takeDamage(playerPokeAttack);

            } else if (userChoice.equalsIgnoreCase("back")){
                processActionPhase(userChoice,player,npc, game);
            } else {
                System.out.println("Invalid entry.");
            }

        }
        //If user choice is use item
        else if (userChoice.equalsIgnoreCase("Use item")){
            //Display to player their inventory
            player.checkInventory();

            if (player.getInventory().isEmpty()) {
                System.out.println("You don't have any items to use!");
            }
            else {
                String itemString[] = new String[player.getInventory().size()];

                // ArrayList to Array Conversion
                for (int j = 0; j < player.getInventory().size(); j++) {
                    // Assign each value to String array
                    itemString[j] = player.getInventory().get(j);
                }

                System.out.println("Which item would you like to use?");

                String res = (String) JOptionPane.showInputDialog(null, "Which item would you like to use?", "Items",
                        JOptionPane.PLAIN_MESSAGE, null, itemString, itemString[0]);

                //String itemChoice = scanner.nextLine();
                game.useItem(res, playerFirstPoke);

            }

        }



    }

}
