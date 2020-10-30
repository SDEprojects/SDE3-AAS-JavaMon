package com.capstone.domainclasses;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class NPC {
    //fields
    private String dialog = "I got nothing to say to you :<";
    String name;
    private String pokemonName;
    private int money;
    private ArrayList<String> inventory = new ArrayList<>(); //inventory
    public ArrayList<Pokemon> npcPokemonList = new ArrayList<>(); //NPC's pokemon list


    public NPC(String name, String dialog, String items, int money, String pokemonsName, Collection<Pokemon> dataList){
        setName(name);
        setMoney(money);
        setDialog(dialog);
        String[] itemList = items.split(",");
        for (String item: itemList) {
            if (!item.equals("none")) {
                this.addInventory(item);
            }
        }
        setPokemonName(pokemonsName);
        processPokemon(dataList);
    }

    //methods
    public String getDialog(){
        return this.dialog;
    }

    private void setDialog(String dialog){
        this.dialog = dialog;
    }

    public String getName(){
        return name;
    }
    private void setName(String name){
        this.name = name;
    }

    public String getPokemonName(){
        return pokemonName;
    }
    private void setPokemonName(String pokemonName){
        this.pokemonName = pokemonName;
    }
    private void setMoney(int money){
        this.money = money;
    }

    public int getMoney(){
        return money;
    }

    public ArrayList<String> getInventory(){
        return inventory;
    }

    public void addInventory(String item){
        inventory.add(item);
    }

    public void clearInventory(){
        inventory.clear();
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
