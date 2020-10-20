package com.capstone;


import java.util.Collection;
import java.util.Scanner;

public class GameEngine {

    private GUI gui;

    public GameEngine() {
    }

    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        CombatEngine combatEngine = new CombatEngine();


        InitXML game = new InitXML();

        //The order of initialization is important!
        game.initAttacks(); //must be initialized before pokemon
        game.initPokemon(); //must be initialized before npcs
        game.initNPCs(); //must be initialized before rooms
        game.initRooms();
        game.initItems();

        TextParser parser = new TextParser();


        Player player1 = new Player();
        gameEngine.chooseStarter(game, player1);//This method takes the game(initXML for access to the pokemon list, and player1 for access to their pokemon invite.)

        String roomName = "Oak's Lab";
        Room startingRoom = game.getRoom(roomName);
        player1.setCurrentRoom(startingRoom);

        player1.getCurrentRoom().displayOutput();
        System.out.println("=====================================================");

        //actual loop for gamez
        while (true) {
            //prompt user for input

            parser.checkPlayerCommand(game,gameEngine,player1);
            System.out.println("=====================================================");
            //This is the start of the implementation for the game loop when player interacts with NPC
            String userInput = parser.getUserInput();
            if (userInput.split(" ")[0].toLowerCase().equals("interact")) {
                String npc = userInput.split(" ", 2)[1];


                System.out.println('"' + game.npcDialog(npc) + '"');

                NPCFactory newEncounterNPC = null;

                for (NPCFactory encounterNPC : game.listOfNPCs){
                    if (encounterNPC.getName().equalsIgnoreCase(npc) && !encounterNPC.npcPokemonList.isEmpty()){
                        newEncounterNPC = encounterNPC;
                        System.out.println(npc + " challenges you to a Pokemon Battle!");
                        combatEngine.combatLoopTrainer(player1,newEncounterNPC,gameEngine);
                    }
                }


            }

//            //for checking the map
//            if ((userInput.toLowerCase().equals("check map")) || (userInput.toLowerCase().equals("map"))) {
//                player1.checkMap();
//            }
//
//            //for getting help menu (Fix input parser to allow for help as a single input later)
//            else if ((userInput.toLowerCase().equals("get help")) || (userInput.toLowerCase().equals("help"))) {
//                player1.showHelp();
//            }
//
//            //for checking your inventory
//            else if ((userInput.toLowerCase().equals("check bag")) || (userInput.toLowerCase().equals("check inventory"))) {
//                player1.checkInventory();
//            }
//
//            //for checking item use
//            else if (userInput.split(" ")[0].toLowerCase().equals("use")) {
//                String item = userInput.split(" ",2)[1].toLowerCase();
//                player1.useItem(item,gameEngine);
//            }
//            else if (userInput.equalsIgnoreCase("check Pokemon")) {
//                player1.checkPokemon();
//            }
//
//
//            //for the movement
//            //if the first word of the input before a space is read is "go" then execute here
//            else if (userInput.split(" ")[0].toLowerCase().equals("go")) {
//                //splits the input string into TWO string, split between the FIRST space... and set the direction variable to be the second word
//                String direction = userInput.split(" ",2)[1].toLowerCase();
//
//                //calls the validMove method from the player class to check if it's a valid move
//                if (player1.validMove(direction)) {
//                    //if move is valid, then let user know they moved, set their current room to the new room, and display the output
//                    System.out.println("You moved " + direction);
//                    switch (direction) {
//                        case "north":
//                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getNorthTile()));
//                            break;
//                        case "east":
//                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getEastTile()));
//                            break;
//                        case "south":
//                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getSouthTile()));
//                            break;
//                        case "west":
//                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getWestTile()));
//                            break;
//                    }
//                    player1.getCurrentRoom().displayOutput();
//                }
//                //TO DO: if its not valid, then print this out... there is already a sout in validMoves so this is redundant! Maybe just delete for future sprints
//                else System.out.println("You can't go in that direction!");
//
//
//            }
//            //else if the first word is "talk" then...
//            else if (userInput.split(" ")[0].toLowerCase().equals("talk")) {
//                //set the second string in input after the first space to be equal to the NPC name
//                String npc = userInput.split(" ",2)[1];
//
//
//                //simple check to see if the NPC name in the input is actually in the current room
//                if (player1.getCurrentRoom().getNpcName().toLowerCase().equals(npc.toLowerCase())) {
//                    //if they are in the room, display their dialog
//                    System.out.println('"' + game.npcDialog(npc) + '"');
//
//                    //when you talk to the npc, if they have an item, they give it to you!
//                    Collection<String> npcItems = game.npcItem(npc);
//                    if (npcItems != null) {
//                        for (String item: npcItems) {
//                            System.out.println(player1.getCurrentRoom().getNpcName() + " gave you a " + item + "!");
//                            player1.addInventory(item);
//                        }
//                        //sets the NPC's inventory to null so they don't give you the items again
//                        game.clearNPCInventory(npc);
//                    }
//
//                }
//                //if npc isn't in the room... tell the user that
//                else System.out.println("Theres nobody named that here to talk to!");
//            }
//            //else if the first word is "interact" then...
//            else if (userInput.split(" ")[0].toLowerCase().equals("interact")) {
//                String interactable = userInput.split(" ",2)[1].toLowerCase();
//                if (player1.getCurrentRoom().getInteractableItem().toLowerCase().equals(interactable)) {
//
//                    //shop interface! Will probably move somewhere and make it a method so that it's not so CLUNKY
//                    if (interactable.equals("shop counter")) {
//                        System.out.println("--------PokeMart--------");
//                        System.out.println("Potion              $100");
//                        System.out.println("Super Potion        $500");
//                        System.out.println("Full Heal          $1000");
//                        System.out.println("Revive             $2500");
//                        System.out.println("------------------------");
//                        System.out.println("To purchase an item: buy <item>!");
//                        System.out.println("To exit shop: exit shop!");
//                        boolean exit = false;
//                        while (!exit) {
//                            String shopInput = parser.checkPlayerCommand().toLowerCase();
//                            if (shopInput.split(" ")[0].equals("buy")) {
//                                String item = shopInput.split(" ", 2)[1];
//                                switch (item) {
//                                    case "potion":
//                                        player1.buyItem("potion", 100);
//                                        break;
//                                    case "super potion":
//                                        player1.buyItem("super potion", 500);
//                                        break;
//                                    case "full heal":
//                                        player1.buyItem("full heal", 1000);
//                                        break;
//                                    case "revive":
//                                        player1.buyItem("revive", 2500);
//                                        break;
//                                }
//                            } else if (shopInput.equals("exit shop")) {
//                                System.out.println("Thank you for your patronage!");
//                                exit = true;
//                            }
//                        }
//                    }
//                    else {
//                        System.out.println("You try to interact with " + interactable);
//                        System.out.println("We need to implement an interactables class <_>");
//                    }
//                }
//                else System.out.println("Theres no " + interactable + " here to interact with!");
//
//            }
//            System.out.println("=====================================================");
        }
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
    void chooseStarterGUI(InitXML game, Player player){
        gui.chooseStarter(game, player);
    }

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
