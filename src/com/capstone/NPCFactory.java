package com.capstone;

import java.util.ArrayList;
import java.util.Collection;

public class NPCFactory extends Player {
    //fields
    private String dialog = "I got nothing to say to you :<";
    private String pokemonName;


    public ArrayList<Pokemon> npcPokemonList = new ArrayList<>(); //NPC's pokemon list


    //ctors
//    public NPCFactory(String name, String dialog){  //outdates, going with full constructor
//        super(name);
//        this.dialog = dialog;
//
//    };

    public NPCFactory(String name, String dialog, String items, int money, String pokemonsName, Collection<Pokemon> dataList){
        super(name,money);
        this.dialog = dialog;
        String[] itemList = items.split(",");
        for (String item: itemList) {
            if (!item.equals("none")) {
                this.addInventory(item);
            }
        }
        pokemonName = pokemonsName;
        processPokemon(dataList);
    }

    //methods
    public String getDialog(){
        return this.dialog;
    }


    @Override
    public String toString() {
        return "NPCFactory{" +
                "name='" + getName() + '\'' +
                "dialog='" + getDialog() + '\'' +
                '}';
    }

    //If the npc has a pokemon listed in the xml doc, they will get the pokemon added to their pokemonlist.
    void processPokemon(Collection<Pokemon> dataList){
        for(Pokemon pokemon : dataList){
            if (pokemon.getName().equalsIgnoreCase(pokemonName)){
                npcPokemonList.add(pokemon);
            }
        }
    }
}
