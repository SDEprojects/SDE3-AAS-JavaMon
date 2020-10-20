package com.capstone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GUI {

    private ActionListener titleScreenHandler;


    private JFrame window;
    private JPanel titleNamePanel;
    private JPanel startButtonPanel;
    private JTextArea mainTextArea;
    private final Font normalFont = new Font("Times New Roman", Font.PLAIN, 28);

    private String[] choiceDisplayArr = {"Bulbasaur (Grass-Type)", "Charmander (Fire-Type)", "Squirtle (Water-Type)"};
    private String[] choiceActionCommandArr = {"bulbasaur", "charmander", "squirtle"};
    private JComboBox<String> choiceCombo = new JComboBox(choiceDisplayArr);
    private String selectedChoice = null;

    private JTextArea output = new JTextArea();



    private GameEngine gameEngine;
    private String starter;
    private String roomName;
    private Player player1;
    private InitXML game;
    private TextParser parser;


    public static void main(String[] args) {

        new GUI();

    }

    public GUI() {
        this.gameEngine = new GameEngine();

        game = new InitXML();
        game.initNPCs();
        game.initRooms();
        game.initPokemon();
        game.initItems();
        parser = new TextParser();


        player1 = new Player();

//        initFrame
        initFrame();
        chooseStarter(game, player1);
    }

    private void initFrame() {

        // Initializing JFrame Window
        window = new JFrame();
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
//        window.setLayout(null);


        //Initializing Title Name Panel
        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100, 100, 600, 150);
        titleNamePanel.setBackground(Color.BLACK);


        JLabel titleNameLabel = new JLabel("");
        titleNameLabel.setForeground(Color.WHITE);
        titleNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 60));

        titleNamePanel.add(titleNameLabel);

        //Initializing Start Button Panel
        startButtonPanel = new JPanel();
        startButtonPanel.setBounds(300, 400, 200, 100);
        startButtonPanel.setBackground(Color.BLACK);


        JButton startButton = new JButton("START");
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.RED);
        startButton.setFont(normalFont);
        startButton.addActionListener(titleScreenHandler);

        startButtonPanel.add(startButton);

        window.add(titleNamePanel);
//        window.add(startButtonPanel);

        window.setVisible(true);


    }


    private void handleUserInput(String userInput){
        //prompt user for input

//        String userInput = parser.getUserInput();
        System.out.println("=====================================================");

        //for checking the map
        if ((userInput.toLowerCase().equals("check map")) || (userInput.toLowerCase().equals("map"))) {
//            player1.checkMap();
            checkMap(player1);
        }

        //for getting help menu (Fix input parser to allow for help as a single input later)
        else if ((userInput.toLowerCase().equals("get help")) || (userInput.toLowerCase().equals("help"))) {
//            player1.showHelp();
            showHelp(player1);
        }

        //for checking your inventory
        else if ((userInput.toLowerCase().equals("check bag")) || (userInput.toLowerCase().equals("check inventory"))) {
//            player1.checkInventory();
            checkInventory(player1);
        }

        //for checking item use
        else if (userInput.split(" ")[0].toLowerCase().equals("use")) {
            String item = userInput.split(" ",2)[1].toLowerCase();
            player1.useItem(item,gameEngine);
            //PENDING
        }
        else if (userInput.equalsIgnoreCase("check Pokemon")) {
            //player1.checkPokemon();
            checkPokemon(player1);
        }


        //for the movement
        //if the first word of the input before a space is read is "go" then execute here
        else if (userInput.split(" ")[0].toLowerCase().equals("go")) {

            //Pending test

            //splits the input string into TWO string, split between the FIRST space... and set the direction variable to be the second word
            String direction = userInput.split(" ",2)[1].toLowerCase();

            //calls the validMove method from the player class to check if it's a valid move
            if (player1.validMove(direction)) {
                //if move is valid, then let user know they moved, set their current room to the new room, and display the output
//                System.out.println("You moved " + direction);
                output.append("You moved " + direction + "\n");

                switch (direction) {
                    case "north":
                        player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getNorthTile()));
                        break;
                    case "east":
                        player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getEastTile()));
                        break;
                    case "south":
                        player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getSouthTile()));
                        break;
                    case "west":
                        player1.setCurrentRoom(game.getRoom(player1.getCurrentRoom().getWestTile()));
                        break;
                }
//                player1.getCurrentRoom().displayOutput();
                setCurrentRoomDetails(player1.getCurrentRoom());
            }
            //TO DO: if its not valid, then print this out... there is already a sout in validMoves so this is redundant! Maybe just delete for future sprints
            else {
                System.out.println("You can't go in that direction!");
                output.append("=====================================================" + "\n");
                output.append("You can't go in that direction!" + "\n");
                output.append("=====================================================" + "\n");
            }

        }
        //else if the first word is "talk" then...
        else if (userInput.split(" ")[0].toLowerCase().equals("talk")) {
            //PENDING

            //set the second string in input after the first space to be equal to the NPC name
            String npc = userInput.split(" ",2)[1];


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
        //else if the first word is "interact" then...
        else if (userInput.split(" ")[0].toLowerCase().equals("interact")) {

            //PENDING

            String interactable = userInput.split(" ",2)[1].toLowerCase();
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
                        String shopInput = parser.getUserInput().toLowerCase();
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
        System.out.println("=====================================================");
    }

    private void setCurrentRoomDetails(Room currentRoom) {
        JPanel roomP = getRoomDetailsPanel(currentRoom);
        window.getContentPane().add(roomP, BorderLayout.NORTH);
        window.revalidate();
    }

    private void checkPokemon(Player player) {
        player.checkPokemon();
        output.setText("");
        output.append("=====================================================" + "\n");
        output.append("You check your PokeBelt: "+ "\n");
        ArrayList<Pokemon> playersPokemon = player.getPlayersPokemon();
        for (Pokemon pokemon: playersPokemon) {
//            pokemon.displayOutStatsAndAll();
            displayOutStatsAndAll(pokemon, player);
        }
        output.append("=====================================================" + "\n");
    }

    private void checkInventory(Player player) {
        Collection<String> inventory = player.getInventory();
        output.setText("");
        output.append("=====================================================" + "\n");
        output.append("Items you currently have in your inventory: " + "\n");
        for (String item: inventory) {
            output.append(item);
        }
        output.append("You currently have " + player.getMoney() + " dollars." + "\n");
        output.append("=====================================================" + "\n");
    }

    private void showHelp(Player player) {
        //displays a help menu
        output.append("=====================================================" + "\n");
        output.append("~~~~~ Instructions for playing the game! ~~~~~~" + "\n");
        output.append("To move: go <north,east,south, or west" + "\n");
        output.append("To talk: talk <NPC's name>" + "\n");
        output.append("To interact: interact <interactable object>" + "\n");
        output.append("To check the map: check map" + "\n");
        output.append("To check your inventory and wallet: check bag/inventory" + "\n");
        output.append("To display this help prompt again: get help" + "\n");
        output.append("=====================================================");
    }


    private void checkMap(Player player) {
        //displays rooms in the 4 cardinal directions and your current room
        Room currentRoom = player.getCurrentRoom();
        output.setText("");
        output.append("=====================================================" + "\n");
        output.append("You are currently in: " + currentRoom.getName() + "\n");
        output.append("To the North of you is: " + currentRoom.getNorthTile() + "\n");
        output.append("To the East of you is: " + currentRoom.getEastTile() + "\n");
        output.append("To the South of you is: " + currentRoom.getSouthTile() + "\n");
        output.append("To the West of you is: " + currentRoom.getWestTile() + "\n");
        output.append("=====================================================");
    }


    public void displayOutStatsAndAll(Pokemon pokemon, Player player){
        JPanel vboxP = new JPanel();
        vboxP.setLayout(new BoxLayout(vboxP, BoxLayout.PAGE_AXIS));
        vboxP.setAlignmentX(Component.CENTER_ALIGNMENT);
        output.setText("");
        output.append("=====================================================" + "\n");
        output.append("You're in OakRoom" + "\n");
        output.append("Pokemon: " + pokemon.getName() + "\n");
        output.append("Pokemon Type: " + pokemon.getType() + "\n");
        output.append("Pokemon Level: " + pokemon.getLevel() + "\n");
        output.append("Pokemon HP: " + "[" + pokemon.getCurrentHealth() + "/" + pokemon.getMaxHealth() + "]"  + "\n");
        output.append("Pokemon Attack: " + pokemon.getAttack() + "\n");
        output.append("=====================================================" + "\n");


        String roomName = "Oak's Lab";
        Room startingRoom = game.getRoom(roomName);
        player1.setCurrentRoom(startingRoom);

//        player1.getCurrentRoom().displayOutput();
        Room currentRoom = player1.getCurrentRoom();
        JPanel roomP = getRoomDetailsPanel(currentRoom);
//        vboxP.add(roomP);
        output.append("=====================================================" + "\n");
        vboxP.add(output);

        JPanel southP = new JPanel();
        southP.setLayout(new BoxLayout(southP, BoxLayout.PAGE_AXIS));
        JTextField inputTF = new JTextField(20);
        southP.add(new JLabel("Enter your choice: "));
        southP.add(inputTF);

        JButton submitB = new JButton("Submit");
        southP.add(submitB);
        submitB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputTF.getText();
                System.out.println("Input: " + input);
                handleUserInput(input);
            }
        });

        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());
        window.getContentPane().add(roomP, BorderLayout.NORTH);
        window.getContentPane().add(vboxP, BorderLayout.CENTER);
        window.getContentPane().add(southP, BorderLayout.SOUTH);
        window.revalidate();


    }
    private JPanel getRoomDetailsPanel(Room room) {
        player1.getCurrentRoom().displayOutput();

        JPanel vboxP = new JPanel();
        vboxP.setLayout(new BoxLayout(vboxP, BoxLayout.PAGE_AXIS));
        vboxP.setAlignmentX(Component.CENTER_ALIGNMENT);

        vboxP.add(new JLabel("Your current location: " +  room.getName()));
        vboxP.add(new JLabel("Location Description: " + room.getDescription()));

        //Check if npcName is null or empty, if not print out the npc name.
        String npcName = room.getNpcName();
        if(npcName != null && !npcName.trim().isEmpty()){
            vboxP.add(new JLabel("You see " + npcName));
        } else {
            vboxP.add(new JLabel("No one is here."));
        }
        //Check if itemList is empty
        String interactableItem = room.getInteractableItem();
        if(interactableItem != null && !interactableItem.trim().isEmpty()){
            vboxP.add(new JLabel("You observe the area and see " + interactableItem)); //singular item for first iteration
        } else {
            vboxP.add(new JLabel("You look around and find nothing of interest here."));
        }

        JPanel p = new JPanel();
        p.add(vboxP);

        return p;
    }


    public void chooseStarter(InitXML game, Player player){
        JPanel vboxP = new JPanel();
        vboxP.setLayout(new BoxLayout(vboxP, BoxLayout.PAGE_AXIS));
        vboxP.add(new JLabel("You're in Professor Oak's Lab"));
        vboxP.add(new JLabel("Professor Oak: Hey! You're finally here, I've been waiting for you.\nI'm going on vacation soon... and the flight I'm going on has a strict 1 Pokemon carry on limit.\nI'm going to need you to look after one while I'm gone! I'll even let you choose who you want to take!\n"));

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        ActionListener radioButtonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                starter = event.getActionCommand();
                System.out.println("actionCommand: " + starter);
            }
        };

        vboxP.add(new JLabel("Choose One:"));
        for (int i = 0; i < choiceDisplayArr.length; i++) {
            JRadioButton radio = new JRadioButton(choiceDisplayArr[i]);
            radio.setActionCommand(choiceActionCommandArr[i]);
            radio.addActionListener(radioButtonListener);
            group.add(radio);
            vboxP.add(radio);
        }

        JButton startButton = new JButton("START");
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.RED);
        startButton.setFont(normalFont);

        vboxP.add(startButton);
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("selected starter: " + starter);
                for(Pokemon pokemon: game.listOfPokemon) {
                    if (pokemon.getName().equalsIgnoreCase(starter)) {
                        player.playersPokemon.add(pokemon);
                        System.out.println("You chose: ");
                        for (Pokemon playersFirstPokemon : player.playersPokemon) {
//	    					playersFirstPokemon.displayOutStatsAndAll();
                            displayOutStatsAndAll(playersFirstPokemon, player);
                        }
                    }
                }
            }
        });


        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());
        window.getContentPane().add(vboxP);
        window.revalidate();
    }


    public void createGameScreen()  {
        titleNamePanel.setVisible(false);
//        startButtonPanel.setVisible(false);

        // Main Text Panel
        JPanel mainTextPanel = new JPanel();
        mainTextPanel.setBounds(100, 100, 600, 250);
        mainTextPanel.setBackground(Color.BLACK);


        mainTextArea = new JTextArea();
        mainTextArea.setBounds(100, 100, 600, 250);
        mainTextArea.setBackground(Color.BLACK);
        mainTextArea.setForeground(Color.WHITE);
        mainTextArea.setFont(normalFont);
        mainTextArea.setLineWrap(true);

        mainTextPanel.add(mainTextArea);

        window.add(mainTextPanel);

    }




    public void setCurrentRoomGUI(String roomName) {

        this.roomName = roomName;
    }


}