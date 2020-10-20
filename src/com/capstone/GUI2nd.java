package com.capstone;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/*
*The GUI class for the Pokemon Game.
 */

public class GUI2nd {

    private JFrame window;
    private JPanel titleNamePanel;
    private final Font normalFont = new Font("Times New Roman", Font.PLAIN, 28);

    private String[] choiceDisplayArr = {"Bulbasaur (Grass-Type)", "Charmander (Fire-Type)", "Squirtle (Water-Type)"};
    private String[] choiceActionCommandArr = {"bulbasaur", "charmander", "squirtle"};

    // Creates and Initializes the text area.
    JTextArea commonDisplay = new JTextArea(20,25);
    JTextArea pokemonDisplay = new JTextArea(20,10);
    JTextArea mapDisplay = new JTextArea(20,10);
    JTextArea roomDisplay = new JTextArea(6,50);

    private PrintStream roomDisplayOut = new PrintStream(new CustomOutputStream(roomDisplay));
    private PrintStream commonDisplayOut = new PrintStream(new CustomOutputStream(commonDisplay));
    private PrintStream mapDisplayOut = new PrintStream(new CustomOutputStream(mapDisplay));
    PrintStream pokemonDisplayOut = new PrintStream(new CustomOutputStream(pokemonDisplay));

    private GameEngine gameEngine = new GameEngine();
    private CombatEngineGui combatEngine = new CombatEngineGui();
    private String starter;
    private Player player1 = new Player();
    private InitXML game = new InitXML();
    private TextParserGUI parser = new TextParserGUI();
    private JPanel mainPanel;

    //Pokemon Image Icons
    private ImageIcon balbasaurIcon;
    private ImageIcon charmanderIcon;
    private ImageIcon squirtleIcon;

    //Pokemon Image Label
    private JLabel pokemonImageLabel;

    //Path of the starting screen image
    private String startPageImagePath = "images/pokemon.gif";

    //main method.
    public static void main(String[] args) {
        GUI2nd gui = new GUI2nd();
        gui.game.initAttacks(); //must be initialized before pokemon
        gui.game.initPokemon(); //must be initialized before npcs
        gui.game.initNPCs(); //must be initialized before rooms
        gui.game.initRooms();
        gui.game.initItems();
        gui.initFrame();
        gui.chooseStarter(gui.game, gui.player1);
    }

    //initialize the frame components
    private void initFrame() {

        setDisplayNonEditable();
        createPokemonTypeImages();

        // Initializing JFrame Window
        window = new JFrame();
        window.setSize(1200, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setResizable(false);
        window.getContentPane().setBackground(Color.BLACK);


        //Initializing Title Name Panel
        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100, 100, 600, 150);
        titleNamePanel.setBackground(Color.BLACK);

        JLabel titleNameLabel = new JLabel("");
        titleNameLabel.setForeground(Color.WHITE);
        titleNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 60));

        titleNamePanel.add(titleNameLabel);

        window.add(titleNamePanel);
        window.setVisible(true);
    }

    /**
     * Create images for various Pokemon types.
     */
    private void createPokemonTypeImages() {
        String balbasaurPath = "images/Balbasaur-Pokemon.png";
        String charmanderPath = "images/Charmander-Pokemon.png";
        String squirtlePath = "images/Squirtle-Pokemon.png";

        Image balbasaurImg = transformImage(createImageIcon(balbasaurPath, ""), 120, 120);
        Image charmanderImg = transformImage(createImageIcon(charmanderPath, ""), 120, 120);
        Image squirtleImg = transformImage(createImageIcon(squirtlePath, ""), 120, 120);

        balbasaurIcon = new ImageIcon(balbasaurImg);
        charmanderIcon = new ImageIcon(charmanderImg);
        squirtleIcon = new ImageIcon(squirtleImg);
    }

    /**
     * Transforms the given icon's image to scaled instance based on the given width and height.
     * @param icon
     * @param width
     * @param height
     * @return
     */

    private Image transformImage(ImageIcon icon, int width, int height) {
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return newimg;
    }

    //Make all the display text area non editable.
    private void setDisplayNonEditable() {
        roomDisplay.setEditable(false);
        commonDisplay.setEditable(false);
        mapDisplay.setEditable(false);
        pokemonDisplay.setEditable(false);
    }
    /**
     * Select the Pokemon type.
     */

    public void chooseStarter(InitXML game, Player player) {
        JPanel starterPokemonPanel = new JPanel();
        starterPokemonPanel.setLayout(new BoxLayout(starterPokemonPanel, BoxLayout.PAGE_AXIS)); //center the layout here later

        JLabel pokemonImageLabel = getPokemonImageLabel();
        starterPokemonPanel.add(pokemonImageLabel);

        starterPokemonPanel.add(new JLabel("You're in OakRoom"));
        starterPokemonPanel.add(new JLabel("..."));
        starterPokemonPanel.add(new JLabel("Professor Oak: Hey! You're finally here, I've been waiting for you."));
        starterPokemonPanel.add(new JLabel("I'm going on vacation soon... and the flight I'm going on has a strict 1 Pokemon carry on limit."));
        starterPokemonPanel.add(new JLabel("I'm going to need you to look after one while I'm gone! I'll even let you choose who you want to take!"));
        starterPokemonPanel.add(new JLabel("..."));

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        ActionListener radioButtonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                starter = event.getActionCommand();
                System.out.println("actionCommand: " + starter);
            }
        };

        starterPokemonPanel.add(new JLabel("..."));
        starterPokemonPanel.add(new JLabel("Choose One:"));
        for (int i = 0; i < choiceDisplayArr.length; i++) {
            JRadioButton radio = new JRadioButton(choiceDisplayArr[i]);
            radio.setActionCommand(choiceActionCommandArr[i]);
            radio.addActionListener(radioButtonListener);
            group.add(radio);
            starterPokemonPanel.add(radio);
        }

        JButton startButton = new JButton("START");
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.RED);
        startButton.setFont(normalFont);

        starterPokemonPanel.add(startButton);
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("selected starter: " + starter);
                for (Pokemon pokemon : game.listOfPokemon) {
                    if (pokemon.getName().equalsIgnoreCase(starter)) {
                        player.playersPokemon.add(pokemon);
                        System.out.println("You chose: " + starter);
                        for (Pokemon playersFirstPokemon : player.playersPokemon) {
                            System.setOut(pokemonDisplayOut);
                            playersFirstPokemon.displayOutStatsAndAll();
                            System.setOut(System.out);

                            displayOutStatsAndAll(playersFirstPokemon, player);
                            setPokemonImageLabel(playersFirstPokemon);
                            parser.checkPlayerCommand(game, gameEngine,combatEngine, player1, "check map", commonDisplayOut, mapDisplayOut, roomDisplayOut,pokemonDisplayOut, pokemonDisplay);

                        }
                    }
                }
            }

        });

        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());
        window.getContentPane().add(getBorderedPanel(starterPokemonPanel), BorderLayout.CENTER);
        window.revalidate();
    }

    /*
     * Changes the pokemon image label based on the given pokemon's name
     */

    protected void setPokemonImageLabel(Pokemon pokemon) {
        switch(pokemon.getName()) {
            case "Balbasaur":
                pokemonImageLabel.setIcon(balbasaurIcon);
                break;
            case "Charmander":
                pokemonImageLabel.setIcon(charmanderIcon);
                break;
            case "Squirtle":
                pokemonImageLabel.setIcon(squirtleIcon);
                break;
        }
    }

    /**
     * Create Pokemon image label
     */

    private JLabel getPokemonImageLabel() {
        Image img = transformImage(createImageIcon(startPageImagePath, ""), 120, 120);
        ImageIcon icon = new ImageIcon(img);  // transform it back
        JLabel imageLabel = new JLabel("", icon, JLabel.CENTER);
        return imageLabel;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     */
    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Display details of the player.
     */

    public void displayOutStatsAndAll(Pokemon pokemon, Player player) {

        JScrollPane scroll = new JScrollPane (commonDisplay,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);

        String roomName = "Oak's Lab";
        Room startingRoom = game.getRoom(roomName);
        player1.setCurrentRoom(startingRoom);

        //Display room details
        showRoomDetails(player);

        //Create input panel
        JPanel inputP = new JPanel();
        inputP.setLayout(new BoxLayout(inputP, BoxLayout.PAGE_AXIS));
        JTextField inputTF = new JTextField(20);
        inputP.add(new JLabel("Enter your command: "));
        inputP.add(inputTF);

        JButton submitB = new JButton("Submit");
        inputP.add(submitB);
        submitB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                commonDisplay.setText("");
                parser.checkPlayerCommand(game, gameEngine,combatEngine, player1, inputTF.getText(), commonDisplayOut, mapDisplayOut, roomDisplayOut,pokemonDisplayOut, pokemonDisplay);
                showRoomDetails(player);
                parser.checkPlayerCommand(game, gameEngine,combatEngine, player1, "check map", commonDisplayOut, mapDisplayOut, roomDisplayOut,pokemonDisplayOut, pokemonDisplay);
                pokemonDisplay.setText("");
                System.setOut(pokemonDisplayOut);
                player1.getPlayersPokemon().get(0).displayOutStatsAndAll();
                System.setOut(System.out);

                inputTF.setText("");
            }

        });

        //Create room Panel with room details display
        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BorderLayout());
        roomPanel.add(getBorderedPanel(roomDisplay), BorderLayout.CENTER);

        //the pokemon Details Panel
        JPanel pokemonPanel = new JPanel();
        pokemonPanel.setLayout(new BoxLayout(pokemonPanel, BoxLayout.PAGE_AXIS));
        pokemonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pokemonImageLabel = new JLabel("", balbasaurIcon, JLabel.CENTER);
        pokemonPanel.add(pokemonImageLabel);
        pokemonPanel.add(pokemonDisplay);

        //Create Middle Panel with
        //the pokemon Details Panel,
        //the Output Display Panel and the Bag Panel.

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

        middlePanel.add(getBorderedPanel(pokemonPanel), BorderLayout.WEST);
        //middlePanel.add(getBorderedPanel(commonDisplay), BorderLayout.CENTER);
        middlePanel.add(getBorderedPanel(scroll), BorderLayout.CENTER);
        middlePanel.add(getBorderedPanel(mapDisplay), BorderLayout.EAST);

        //Setup Main Panel with
        //RoomDetails Panel, the Middle Panel and the Input Panel.
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //Add RoomDetails Panel
        mainPanel.add(roomPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(getBorderedPanel(inputP), BorderLayout.SOUTH);

        //Add the mainPanel to the window.
        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());
        window.getContentPane().add(mainPanel);

        //Revalidate to make sure that the newly added components are shown.
        window.revalidate();
    }

    /**
     * Adds the given component to a JPanel with border.
     */

    private JPanel getBorderedPanel(JComponent comp) {
        JPanel p = new JPanel();
        Border blueLine = BorderFactory.createLineBorder(Color.blue);
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        CompoundBorder compound = BorderFactory.createCompoundBorder(blueLine, emptyBorder);
        p.setBorder(compound);
        //Add component to the panel
        p.add(comp);
        return p;
    }

    /**
     * Show the details of the current room of the given player.
     */
    private void showRoomDetails(Player player) {
        roomDisplayOut.flush();
        System.setOut(roomDisplayOut);
        player.showRoomDetails();
        System.setOut(System.out);
    }
}