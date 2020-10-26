package com.capstone.businessclasses;


import com.capstone.domainclasses.NPCFactory;
import com.capstone.domainclasses.Player;
import com.capstone.domainclasses.Pokemon;
import com.capstone.domainclasses.Room;


import java.util.Scanner;

public class GameEngine {

    public GameEngine() {
    }


    //Choose pokemon starter method.
    //TODO - complete the choose starter pokemon method.
    void chooseStarter(InitXML game, Player player){

        System.out.println("Professor Oak: Hey! You're finally here, I've been waiting for you.\nI'm going on vacation soon... and the flight I'm going on has a strict 1 Pokemon carry on limit.\nI'm going to need you to look after one while I'm gone! I'll even let you choose who you want to take!\nChoose one: (Bulbasaur (Grass-Type), Charmander (Fire-Type), Squirtle (Water-Type))");

        Scanner scanner = new Scanner(System.in);
        String starter = scanner.nextLine();
        if (!starter.equalsIgnoreCase("bulbasaur") && !starter.equalsIgnoreCase("charmander") && !starter.equalsIgnoreCase("squirtle")){
            System.out.println("Invalid entry");
            chooseStarter(game,player);
        } else {
            for(Pokemon pokemon: game.listOfPokemon) {
                if (pokemon.getName().equalsIgnoreCase(starter)) {
                    player.playersPokemon.add(pokemon);
                    System.out.println("You chose: ");
                    for (Pokemon playersFirstPokemon : player.playersPokemon) {
                        playersFirstPokemon.displayOutStatsAndAll();
                    }
                }
            }
        }
    }

    //TODO find out where this is called


    public boolean useItem(String item, Pokemon pokemon) {
        item = item.toLowerCase();
        if (item.equals("potion")){
            if (pokemon.getCurrentHealth() == pokemon.getMaxHealth()) {
                System.out.println(pokemon.getName() + " is already at max hp! It won't have any effect!");
                return false;
            }
            else if (pokemon.getCurrentHealth() != 0) {
                pokemon.setCurrentHealth(pokemon.getCurrentHealth() + 20);
                if (pokemon.getCurrentHealth() > pokemon.getMaxHealth()) {
                    pokemon.setCurrentHealth(pokemon.getMaxHealth());
                }
                System.out.println(pokemon.getName() + " recovered 20 hp!");
                return true;
            }
            else {
                System.out.println(pokemon.getName() + " has fainted! You can only use a revive or a PokeCenter!");
                return false;
            }

        }
        else if (item.equals("super potion")){
            if (pokemon.getCurrentHealth() == pokemon.getMaxHealth()) {
                System.out.println(pokemon.getName() + " is already at max hp! It won't have any effect!");
                return false;
            }
            else if (pokemon.getCurrentHealth() != 0) {
                pokemon.setCurrentHealth(pokemon.getCurrentHealth() + 50);
                if (pokemon.getCurrentHealth() > pokemon.getMaxHealth()) {
                    pokemon.setCurrentHealth(pokemon.getMaxHealth());
                    return false;
                }
                System.out.println(pokemon.getName() + " recovered 50 hp!");
                return true;
            }
            else {
                System.out.println(pokemon.getName() + " has fainted! You can only use a revive or a PokeCenter!");
                return false;
            }

        }
        else if (item.equals("full heal")){
            if (pokemon.getCurrentHealth() == pokemon.getMaxHealth()) {
                System.out.println(pokemon.getName() + " is already at max hp! It won't have any effect!");
                return false;
            }
            else if (pokemon.getCurrentHealth() != 0) {
                pokemon.setCurrentHealth(pokemon.getMaxHealth());
                System.out.println(pokemon.getName() + " recovered to max hp!");
                return true;
            }
            else {
                System.out.println(pokemon.getName() + " has fainted! You can only use a revive or a PokeCenter!");
                return false;
            }

        }
        else if (item.equals("revive")){
            if (pokemon.getCurrentHealth() == 0) {
                pokemon.setCurrentHealth(pokemon.getMaxHealth() / 2);
                System.out.println(pokemon.getName() + " revived with 1/2 Max HP!");
                return true;
            }
            else {
                System.out.println(pokemon.getName() + " isn't fainted! You can't use a revive!");
                return false;
            }
        }
        else {
            System.out.println(item + " hasn't been implemented yet :<");
            return false;
        }

    }
}
