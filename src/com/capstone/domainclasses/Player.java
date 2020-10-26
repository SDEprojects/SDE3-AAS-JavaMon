package com.capstone.domainclasses;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    //fields, name and inventory
    private static String name = "Jay"; //default player name
    private static ArrayList<String> inventory = new ArrayList<>(); //inventory
    private static int money = 9001; //initialize with 100 monies
    private static ArrayList<Pokemon> playersPokemon = new ArrayList<>(); //This collection is where the player's pokemon is saved.
    private static Room currentRoom;  //to keep track of the current room the player is in

    //ctors
    private Player(){
    }

    // accessors

    public static ArrayList<Pokemon> getPlayersPokemon() {
        return playersPokemon;
    }

    public static int getMoney() {
        return money;
    }
    public static void setMoney(int dollars){
        money=dollars;
    }

    public static String getName() {
        return name;
    }
    public static void setName(String nameInput){
        name = nameInput;
    }

    public static ArrayList<String> getInventory() {
        return inventory;
    }

    public static void addInventory(String item) {
        inventory.add(item);
    } //for later sprints

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room room) {
        currentRoom = room;
    }


    // business methods

    /*
    *Displays the current room Details
     */
    public static void showRoomDetails() {
        String roomDetails = currentRoom.getRoomDetails();
        System.out.println(roomDetails);
    }

    public static void checkMap(){
        //displays rooms in the 4 cardinal directions and your current room
        System.out.println("HERE IS YOUR MAP: ");
        System.out.println(" ");
        System.out.println("You are currently in: " + getCurrentRoom().getName());
        System.out.println("To the North of you is: " + getCurrentRoom().getNorthTile());
        System.out.println("To the East of you is: " + getCurrentRoom().getEastTile());
        System.out.println("To the South of you is: " + getCurrentRoom().getSouthTile());
        System.out.println("To the West of you is: " + getCurrentRoom().getWestTile());
    }

    public static void checkInventory(){
        System.out.println("Items you currently have in your inventory: ");
        for (String item: getInventory()) {
            System.out.println(item);
        }
        System.out.println("You currently have " + money + " dollars.");
    }

    public static void checkPokemon(){
        System.out.println("You check your PokeBelt: ");
        for (Pokemon pokemon: getPlayersPokemon()) {
            pokemon.displayOutStatsAndAll();
        }
    }

    public static void clearInventory(){
        inventory.clear();
    }

    public static void buyItem(String item, int price){
        if (getMoney() > price){
            getInventory().add(item);
            subtractMoney(price);
            System.out.println("You bought a " + item + " with " + price + " dollars! " + money + " remains in your wallet!");
        }
        else {
            System.out.println("You only have " + money + " dollars! You can't afford that :<");
        }
    }

    public void useItem(String item){
        Scanner scanner = new Scanner(System.in);
        boolean validPokemon = false;
        String pokemonName = "";
        Pokemon actualPokemon = playersPokemon.get(0);
        while (!validPokemon) {
            System.out.println("Which Pokemon do you want to use " + item + " on?");
            checkPokemon();
            pokemonName = scanner.nextLine();
            for (Pokemon pokeBelt: this.playersPokemon) {
                if (pokemonName.toLowerCase().equals(pokeBelt.getName().toLowerCase())) {
                    actualPokemon = pokeBelt;
                    validPokemon = true;
                }

            }
        }
        if (inventory.contains(item)){
            System.out.println("You used a " + item + " on " + pokemonName + "!");
            if (Item.useItem(item,actualPokemon)) {
                inventory.remove(item);
            }
        }
        else {
            System.out.println("You don't have a " + item + " in your inventory!");
        }
    }

    public static void addMoney(int amount){
        money += amount;
    }

    public static void subtractMoney(int amount){
        money -= amount;
    }

    public static void showHelp(){
        //displays a help menu
        System.out.println("~~~~~ Instructions for playing the game! ~~~~~~");
        System.out.println("To move: go <north,east,south, or west");
        System.out.println("To talk: talk <NPC's name>");
        System.out.println("To interact: interact <interactable object>");
        System.out.println("To check the map: check map");
        System.out.println("To check your inventory and wallet: check bag/inventory");
        System.out.println("To display this help prompt again: get help");
    }

    //calling this method to makes sure the passed direction is possible to move to
    public static boolean validMove(String direction){
        //switch cases for the directions
        switch (direction) {
            case "north":
            case "up":
                //if the direction tiles are not "nothing" (what we hardcoded in the ROOM XML for not having a room) then it's a valid move
                if (!currentRoom.getNorthTile().equals("nothing")) {
                    return true;
                }
                break;
            case "east":
            case "right":
                if (!currentRoom.getEastTile().equals("nothing")) {
                    return true;
                }
                break;
            case "south":
            case "down":
                if (!currentRoom.getSouthTile().equals("nothing")) {
                    return true;
                }
                break;
            case "west":
            case "left":
                if (!currentRoom.getWestTile().equals("nothing")) {
                    return true;
                }
                break;
        }
        //if it never returned true (the direction was not valid)... then this executes
        System.out.println("There's nothing there! You can't go that way!");
        return false;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", inventory=" + inventory +
                ", currentRoom=" + currentRoom +
                '}';
    }
}
