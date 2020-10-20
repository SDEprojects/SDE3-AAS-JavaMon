package com.capstone;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

/*The TextParser class parses and validates the user (console) inputs*/
public class TextParser {
//    private String userInput;

//    public void move(String direction, Room room) {
//
//    }

    public String getUserInput() {
        String inputString = "";

        // parse text from the console/keyboard
        try {
            System.out.println("Enter a command: ");

            //say you had a separate class that has a scanner not seen here
            // replace the lines below with input.getText()?
            // scanner can be initialized externally

            //go north


            Scanner scanner = new Scanner(System.in);
            inputString = scanner.nextLine();

            //input validation
            while (!inputValidation(inputString)) {
                System.out.println("===================================");
                System.out.println("Enter a command: ");
                inputString = scanner.nextLine();
            }

        } catch (IllegalArgumentException e) {
            getUserInput();
        }
        return inputString;
    }

    public void checkPlayerCommand(InitXML game, GameEngine gameEngine, Player player1) {
        try {
            String userInput = getUserInput().toLowerCase();
            String userActions = userInput.split(" ")[0];
            String userArgument = userInput.split(" ", 2)[1];

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
            System.out.println("----------------------------");

            // iterates over node list of tag names "action"
            for (int temp = 0; temp < nList.getLength(); temp++) {
                // fetches node item from list by their index position
                Node nNode = nList.item(temp);
                // System.out.println("Node list length is: " + nList.getLength());
                // prints current node name
                // System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // for(int a = 0; a < userActions.length; a++)
                    if (eElement.getElementsByTagName("engage").item(0).getTextContent().contains(userActions)) {
                        playerInteracts(player1,userArgument);
                    }
                    else if (eElement.getElementsByTagName("communicate").item(0).getTextContent().contains(userActions)) {
                        playerTalks(player1,game,userArgument);
                    }
                    else if (eElement.getElementsByTagName("utilize").item(0).getTextContent().contains(userActions)) {
                        player1.useItem(userArgument, gameEngine);
                    }
                    else if (eElement.getElementsByTagName("check").item(0).getTextContent().contains(userActions)) {
                        System.out.println("Player checks");
                        if (eElement.getElementsByTagName("bag").item(0).getTextContent().contains(userArgument)) {
                            player1.checkInventory();
                        }
                        else if (eElement.getElementsByTagName("pokemon").item(0).getTextContent().contains(userArgument)) {
                            player1.checkPokemon();
                        }
                        else if (eElement.getElementsByTagName("map").item(0).getTextContent().contains(userArgument)) {
                            player1.checkMap();
                        }
                        else {
                            System.out.println("You don't have that... you can't check it!");
                            System.out.println("----------------------------");
                        }
                    }
                    else if (eElement.getElementsByTagName("get").item(0).getTextContent().contains(userActions)) {
                        if (eElement.getElementsByTagName("help").item(0).getTextContent().contains(userArgument)) {
                            player1.showHelp();
                        }
                        else {
                            System.out.println("Did you mean to type: get help?");
                            System.out.println("----------------------------");
                        }
                    }
                    else if (eElement.getElementsByTagName("go").item(0).getTextContent().contains(userActions)) {
                        if (eElement.getElementsByTagName("up").item(0).getTextContent().contains(userArgument) && player1.validMove("north")) {
                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getNorthTile()));
                        } else if (eElement.getElementsByTagName("down").item(0).getTextContent().contains(userArgument) && player1.validMove("south")) {
                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getSouthTile()));
                        } else if (eElement.getElementsByTagName("left").item(0).getTextContent().contains(userArgument) && player1.validMove("west")) {
                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getWestTile()));
                        } else if (eElement.getElementsByTagName("right").item(0).getTextContent().contains(userArgument) && player1.validMove("east")) {
                            player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getEastTile()));
                        } else {
                            System.out.println("Invalid direction, please try again :<");
                            System.out.println("----------------------------");
                        }
                        player1.getCurrentRoom().displayOutput();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error :< ");
            System.out.println("----------------------------");
        }
    }
    // change to package private?
    // make public for unit-testing purpose? APIs available to text private methods but may not be best practice
    private boolean inputValidation(String input) {
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

    private void playerInteracts(Player player1, String interactable) {
        if (player1.getCurrentRoom().getInteractableItem().toLowerCase().equals(interactable)) {
            //shop interface! Will probably move somewhere and make it a method so that it's not so CLUNKY
            if (interactable.equals("shop counter")) {
                System.out.println("--------PokeMart--------");
                System.out.println("Potion              $100");
                System.out.println("Super Potion        $500");
                System.out.println("Full Heal          $1000");
                System.out.println("Revive             $2500");
                System.out.println("------------------------");
                System.out.println("To purchase an item: buy <item>!");
                System.out.println("To exit shop: exit shop!");
                boolean exit = false;
                while (!exit) {
                    String shopInput = getUserInput();
                    if (shopInput.split(" ")[0].equals("buy")) {
                        String item = shopInput.split(" ", 2)[1];
                        switch (item) {
                            case "potion":
                                player1.buyItem("potion", 100);
                                break;
                            case "super potion":
                                player1.buyItem("super potion", 500);
                                break;
                            case "full heal":
                                player1.buyItem("full heal", 1000);
                                break;
                            case "revive":
                                player1.buyItem("revive", 2500);
                                break;
                        }
                    } else if (shopInput.equals("exit shop")) {
                        System.out.println("Thank you for your patronage!");
                        exit = true;
                    }
                }
            }
            else {
                System.out.println("You try to interact with " + interactable);
                System.out.println("We need to implement an interactables class <_>");
            }
        }
        else System.out.println("Theres no " + interactable + " here to interact with!");
    }

    public void playerTalks(Player player1, InitXML game, String npc) {
        //simple check to see if the NPC name in the input is actually in the current room
        if (player1.getCurrentRoom().getNpcName().toLowerCase().equals(npc.toLowerCase())) {
            //if they are in the room, display their dialog
            System.out.println('"' + game.npcDialog(npc) + '"');
            //when you talk to the npc, if they have an item, they give it to you!
            Collection<String> npcItems = game.npcItem(npc);
            if (npcItems != null) {
                for (String item: npcItems) {
                    System.out.println(player1.getCurrentRoom().getNpcName() + " gave you a " + item + "!");
                    player1.addInventory(item);
                }
                //sets the NPC's inventory to null so they don't give you the items again
                game.clearNPCInventory(npc);
            }

        }
        //if npc isn't in the room... tell the user that
        else System.out.println("Theres nobody named that here to talk to!");
    }
}