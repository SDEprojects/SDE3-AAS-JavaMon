package com.capstone.businessclasses;


import com.capstone.domainclasses.Player;
import com.capstone.domainclasses.Pokemon;


import java.util.Scanner;

public class GameEngine {

    public GameEngine() {
    }


    //Choose pokemon starter method.
    //TODO - complete the choose starter pokemon method.
    void chooseStarter(Player player){

        System.out.println("Professor Oak: Hey! You're finally here, I've been waiting for you.\nI'm going on vacation soon... and the flight I'm going on has a strict 1 Pokemon carry on limit.\nI'm going to need you to look after one while I'm gone! I'll even let you choose who you want to take!\nChoose one: (Bulbasaur (Grass-Type), Charmander (Fire-Type), Squirtle (Water-Type))");

        Scanner scanner = new Scanner(System.in);
        String starter = scanner.nextLine();
        if (!starter.equalsIgnoreCase("bulbasaur") && !starter.equalsIgnoreCase("charmander") && !starter.equalsIgnoreCase("squirtle")){
            System.out.println("Invalid entry");
            chooseStarter(player);
        } else {
            for(Pokemon pokemon: InitXML.getListOfPokemon()) {
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



}
