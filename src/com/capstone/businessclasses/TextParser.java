package com.capstone.businessclasses;

import com.capstone.domainclasses.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/*The TextParser class parses and validates the user (console) inputs*/
public class TextParser {

    private TextParser(){}

    public static void checkPlayerCommand(String userInput, PrintStream commonDisplayOut, PrintStream mapDisplayOut, PrintStream roomDisplayOut, PrintStream pokeDisplayOut, JTextArea pokeDisplay) {
    System.setOut(commonDisplayOut);
    try {
        if (inputValidation(userInput)) {

            String userActions = trimUnnecessaryWords(userInput).split(" ")[0].trim();
            String userArgument = trimUnnecessaryWords(userInput).split(" ", 2)[1].trim();

            File inputFile = new File("data", "keyWords.txt");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            // prints the root element of the file which is "keyWords" using getNodeName()
            // System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                    /* Insert if statement for "action item by checking if the first word corresponds to items in
                    action group - need to initialize reference"*/
            // creates and populates a list of nodes tag items by the tag name "action"
            NodeList nList = doc.getElementsByTagName("action");
            //System.out.println("----------------------------");

            // iterates over node list of tag names "action"
            for (int i = 0; i < nList.getLength(); i++) {
                // fetches node item from list by their index position
                Node nNode = nList.item(i);
                // System.out.println("Node list length is: " + nList.getLength());
                // prints current node name
                // System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // for(int a = 0; a < userActions.length; a++)
                    if (eElement.getElementsByTagName("buy").item(0).getTextContent().contains(userActions)) {
                        if (Player.getCurrentRoom().getInteractableItem().toLowerCase().equals("shop counter")) {
                            switch (userArgument) {
                                case "potion":
                                    Player.buyItem("potion", 100);
                                    break;
                                case "super potion":
                                    Player.buyItem("super potion", 500);
                                    break;
                                case "full heal":
                                    Player.buyItem("full heal", 1000);
                                    break;
                                case "revive":
                                    Player.buyItem("revive", 2500);
                                    break;
                                default:
                                    System.out.println("No such item here to buy!");
                            }
                        }
                        else {
                            System.out.println("There's no shop here! You can't buy anything!");
                        }
                    }
                    else if (eElement.getElementsByTagName("engage").item(0).getTextContent().contains(userActions)) {
                        String npcName = Player.getCurrentRoom().getNpcName();
                        NPC npcActual = InitXML.getNPC(npcName);
                        playerInteracts(npcActual, userArgument,commonDisplayOut, pokeDisplayOut, pokeDisplay);
                    } else if (eElement.getElementsByTagName("communicate").item(0).getTextContent().contains(userActions)) {
                        playerTalks(userArgument);
                    } else if (eElement.getElementsByTagName("utilize").item(0).getTextContent().contains(userActions)) {
                        if (userInput.split(" ").length <= 2) {
                            System.out.println("Please include which Pokemon you want to use it on");
                        }
                        else {
                            String pokemon = userArgument.substring(userArgument.lastIndexOf(" ") + 1);
                            String item = userArgument.substring(0,userArgument.lastIndexOf(" "));
                            useItem(item,pokemon);
                        }
                    } else if (eElement.getElementsByTagName("check").item(0).getTextContent().contains(userActions)) {
                        if (eElement.getElementsByTagName("bag").item(0).getTextContent().contains(userArgument)) {
                            Player.checkInventory();
                        } else if (eElement.getElementsByTagName("pokemon").item(0).getTextContent().contains(userArgument)) {
                            Player.checkPokemon();
                        } else if (eElement.getElementsByTagName("map").item(0).getTextContent().contains(userArgument)) {
                            mapDisplayOut.flush();
                            System.setOut(mapDisplayOut);
                            Player.checkMap();
                            System.setOut(commonDisplayOut);
                        } else {
                            System.out.println("You don't have that... you can't check it!");
                            //System.out.println("----------------------------");
                        }
                    } else if (eElement.getElementsByTagName("get").item(0).getTextContent().contains(userActions)) {
                        if (eElement.getElementsByTagName("help").item(0).getTextContent().contains(userArgument)) {
                            Player.showHelp();
                        } else if (InitXML.getListOfPokemon().contains(InitXML.getPokemon(userArgument))) {
                            Player.addPokemon(InitXML.getPokemon(userArgument));
                            System.out.println("You've caught " +  InitXML.getPokemon(userArgument).getName() + "!!");
                        } else {
                            System.out.println("Did you mean to type: get help?");
                            //System.out.println("----------------------------");
                        }
                    } else if (eElement.getElementsByTagName("go").item(0).getTextContent().contains(userActions)) {
                        if (eElement.getElementsByTagName("up").item(0).getTextContent().contains(userArgument) && Player.validMove("north")) {
                            System.out.println("You go North");
                            //System.out.println("----------------------------");
                            System.setOut(roomDisplayOut);
                            Player.setCurrentRoom(InitXML.getRoom(Player.getCurrentRoom().getNorthTile()));
                            Player.showRoomDetails();
                        } else if (eElement.getElementsByTagName("down").item(0).getTextContent().contains(userArgument) && Player.validMove("south")) {
                            System.out.println("You go South");
                            //System.out.println("----------------------------");
                            System.setOut(roomDisplayOut);
                            Player.setCurrentRoom(InitXML.getRoom(Player.getCurrentRoom().getSouthTile()));
                            Player.showRoomDetails();
                        } else if (eElement.getElementsByTagName("left").item(0).getTextContent().contains(userArgument) && Player.validMove("west")) {
                            System.out.println("You go West");
                            //System.out.println("----------------------------");
                            System.setOut(roomDisplayOut);
                            Player.setCurrentRoom(InitXML.getRoom(Player.getCurrentRoom().getWestTile()));
                            Player.showRoomDetails();
                        } else if (eElement.getElementsByTagName("right").item(0).getTextContent().contains(userArgument) && Player.validMove("east")) {
                            System.out.println("You go East");
                            //System.out.println("----------------------------");
                            System.setOut(roomDisplayOut);
                            Player.setCurrentRoom(InitXML.getRoom(Player.getCurrentRoom().getEastTile()));
                            Player.showRoomDetails();
                        } else {
//                                	System.setOut(commonDisplayOut);
                            System.out.println("Invalid direction, please try again :<");
                            //System.out.println("----------------------------");
                        }
                        System.setOut(System.out);
                    }
                }
            }
        }
        else {
            System.out.println("Invalid input.");
            System.out.println("----------------------------");
        }

    } catch (Exception e) {
        System.out.println("There was an error in the text parser");
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
    }
    System.setOut(System.out);
}

    private static String trimUnnecessaryWords(String input) throws IOException {
        // Reads in the contents WordsToIgnore.txt in the data folder
        Path ignoreFile = Path.of("data", "WordsToIgnore.txt");
        String wordsToIgnore = Files.readString(ignoreFile);
        // Creates an arraylist of words from the above file using a regEx to separate by newline characters
        ArrayList<String> ignoreList = new ArrayList<String>(Arrays.asList(wordsToIgnore.split("\\r?\\n")));
        // Splits the user's input at spaces and removes a word if it matches any from the above list
        for(String word : input.split(" ")) {
            if(ignoreList.contains(word))  {
                input = input.replace(word, "");
            }
        }
        // Returns the passed command with any specified prepositions/articles from the file removed
        return input;
    }
    // change to package private?
    // make public for unit-testing purpose? APIs available to text private methods but may not be best practice
    private static boolean inputValidation(String input) {
        if (input.isEmpty()) {
            System.out.println("You have not entered any text");
            return false;
            //throw new IllegalArgumentException("You have not entered a move");
        } else if (input.split(" ").length < 2) {
            System.out.println("You have entered an invalid move, what can you do with only one word?");
            return false;
        } else if (input.matches("[-+]?[0-9]*\\.?[0-9]+")) {
            System.out.println("You have entered an invalid move");
            return false;
        }
        return true;
    }

    private static void playerInteracts(NPC npc, String interactable, PrintStream commonDisplayOut, PrintStream pokeDisplayOut, JTextArea pokeDisplay) {
        //for the shop interface
        if (Player.getCurrentRoom().getInteractableItem().toLowerCase().equals(interactable) && interactable.toLowerCase().equals("shop counter")) {
            //shop interface! Will probably move somewhere and make it a method so that it's not so CLUNKY
            if (interactable.equals("shop counter")) {
                System.out.println("--------PokeMart--------");
                System.out.println("Potion              $100");
                System.out.println("Super Potion        $500");
                System.out.println("Full Heal          $1000");
                System.out.println("Revive             $2500");
                System.out.println("------------------------");
                System.out.println("To purchase an item: buy <item>!");
            }
            else {
                System.out.println("You try to interact with " + interactable);
                System.out.println("We need to implement an interactables class <_>");
            }
        }
        //for the combat with other trainers
        else if (interactable.equalsIgnoreCase(npc.getName())) {
            System.out.println('"' + npc.getDialog() + '"');
            if (!npc.npcPokemonList.isEmpty()) {
                System.out.println(npc.getName() + " challenges you to a Pokemon Battle!");
                CombatEngine.combatLoopTrainer(npc,commonDisplayOut, pokeDisplayOut, pokeDisplay);
            }
            else {
                System.out.println(npc.getName() + " doesn't have a Pokemon to battle with.");
            }
        }
        //for pokecenter healz
        else if (Player.getCurrentRoom().getInteractableItem().toLowerCase().equals(interactable) && interactable.toLowerCase().equals("healing station")) {
            for (Pokemon pokemon:Player.getPlayersPokemon()) {
                pokemon.setCurrentHealth(pokemon.getMaxHealth());
            }
            System.out.println("All your Pokemon are healed to full HP! Thank you for visiting!");
        }
        //for tall grass
        else if (Player.getCurrentRoom().getInteractableItem().toLowerCase().equals(interactable) && interactable.toLowerCase().equals("tall grass")) {
            System.out.println("You've encountered a wild " + Player.getCurrentRoom().getWildPokemon().get(0).getName() + ".");
 //          for (Pokemon pokemon : Player.getCurrentRoom().getWildPokemon()) {
 //              System.out.println(pokemon.getName());
 //          }
        }


        else System.out.println("Theres no " + interactable + " here to interact with!");
    }

    private static void playerTalks(String npc) {
        //simple check to see if the NPC name in the input is actually in the current room
        if (Player.getCurrentRoom().getNpcName().toLowerCase().equals(npc.toLowerCase())) {
            //if they are in the room, display their dialog
            System.out.println('"' + InitXML.npcDialog(npc) + '"');
            //when you talk to the npc, if they have an item, they give it to you!
            Collection<String> npcItems = InitXML.npcItem(npc);
            if (npcItems != null) {
                for (String item: npcItems) {
                    System.out.println(Player.getCurrentRoom().getNpcName() + " gave you a " + item + "!");
                    Player.addInventory(item);
                }
                //sets the NPC's inventory to null so they don't give you the items again
                InitXML.clearNPCInventory(npc);
            }

        }
        //if npc isn't in the room... tell the user that
        else System.out.println("Theres nobody named that here to talk to!");
    }
    private static void useItem(String item, String pokemon){
        //TODO IF THERE EVER IS MORE THAN ONE POKEMON... CHANGE THIS TO A FOR instead of .get(0)
        if (pokemon.toLowerCase().equals(Player.getPlayersPokemon().get(0).getName().toLowerCase())) {
            if (Player.getInventory().contains(item)){
                Pokemon actualPokemon = Player.getPlayersPokemon().get(0);
                System.out.println("You used a " + item + " on " + pokemon + "!");
                if (Item.useItem(item,actualPokemon)) {
                    Player.getInventory().remove(item);
                }
            }
            else {
                System.out.println("You don't have a " + item + " in your inventory!");
            }
        }
        else {
            System.out.println("You don't own that Pokemon!");
        }
    }
}