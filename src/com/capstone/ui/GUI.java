package com.capstone.ui;

import com.capstone.businessclasses.CombatEngine;
import com.capstone.businessclasses.CustomOutputStream;
import com.capstone.businessclasses.InitXML;
import com.capstone.businessclasses.TextParser;
import com.capstone.domainclasses.Player;
import com.capstone.domainclasses.Pokemon;
import com.capstone.domainclasses.Room;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/*
*The GUI class for the Pokemon Game.
 */

public class GUI {

    private static JFrame window;
    private JPanel titleNamePanel;
    private final Font normalFont = new Font("Times New Roman", Font.PLAIN, 28);

    private String[] choiceDisplayArr = {"Bulbasaur (Grass-Type)", "Charmander (Fire-Type)", "Squirtle (Water-Type)"};
    private String[] choiceActionCommandArr = {"bulbasaur", "charmander", "squirtle"};

    // Creates and Initializes the text area.
    private static JTextArea commonDisplay = new JTextArea(20,25);
    private static JTextArea pokemonDisplay = new JTextArea(20,10);
    private static JTextArea mapDisplay = new JTextArea(20,10);
    private static JTextArea roomDisplay = new JTextArea(6,50);

    private PrintStream roomDisplayOut = new PrintStream(new CustomOutputStream(roomDisplay));
    private PrintStream commonDisplayOut = new PrintStream(new CustomOutputStream(commonDisplay));
    private PrintStream mapDisplayOut = new PrintStream(new CustomOutputStream(mapDisplay));
    PrintStream pokemonDisplayOut = new PrintStream(new CustomOutputStream(pokemonDisplay));

    private String starter; // We can get rid of this by writing better method
    private static JPanel mainPanel;
    private static JScrollPane scroll;
    private static JPanel inputP;
    private static JPanel roomPanel;

    //Pokemon Image Icons
    private ImageIcon jigglypuffIcon;

    //Pokemon Image Label
    private static JLabel pokemonImageLabel;

    //Path of the starting screen image
    private String startPageImagePath = "../images/pokemon.gif";

    //main method.
    public static void main(String[] args) {
        GUI gui = new GUI();
        InitXML.initAttacks(); //must be initialized before pokemon
        InitXML.initPokemon(); //must be initialized before npcs
        InitXML.initNPCs(); //must be initialized before rooms
        InitXML.initRooms();
        InitXML.initItems();
        gui.initFrame();
        gui.chooseStarter();
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
        String jigglypuffPath = "../images/Jigglypuff-Pokemon.png";
        Image jigglypuffImg = transformImage(createImageIcon(jigglypuffPath, ""), 120, 120);
        jigglypuffIcon = new ImageIcon(jigglypuffImg);
    }

    /**
     * Transforms the given icon's image to scaled instance based on the given width and height.
     * @param icon
     * @param width
     * @param height
     * @return
     */

    private static Image transformImage(ImageIcon icon, int width, int height) {
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

    public void chooseStarter() {
        JPanel starterPokemonPanel = new JPanel();
        starterPokemonPanel.setLayout(new BoxLayout(starterPokemonPanel, BoxLayout.PAGE_AXIS)); //center the layout here later

        JLabel pokemonImageLabel = getPokemonImageLabel();
        starterPokemonPanel.add(pokemonImageLabel);

        starterPokemonPanel.add(new JLabel("You're in Oak's Room"));
        starterPokemonPanel.add(new JLabel("..."));
        starterPokemonPanel.add(new JLabel("Professor Oak: Hey! You're finally here, I've been waiting for you."));
        starterPokemonPanel.add(new JLabel("I'm going on vacation soon... and the flight I'm going on has a strict 1 Pokemon carry on limit."));
        starterPokemonPanel.add(new JLabel("I'm going to need you to look after one while I'm gone! I'll even let you choose who you want to take!"));
        starterPokemonPanel.add(new JLabel("..."));
        starterPokemonPanel.add(new JLabel("Select your pokemon---Click start"));
        starterPokemonPanel.add(new JLabel("Find location on the right side of the page"));
        starterPokemonPanel.add(new JLabel("Type appropriate directions in the command block on the bottom of the page"));
        starterPokemonPanel.add(new JLabel("GET YOUR BATTLE ON!!"));
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
                Pokemon playersPokemon = InitXML.getPokemon(starter);
                Player.addPokemon(playersPokemon);
                System.out.println("You chose: " + starter);
                System.setOut(pokemonDisplayOut);
                playersPokemon.displayOutStatsAndAll();
                System.setOut(System.out);
                displayOutStatsAndAll();
                setPokemonImageLabel(playersPokemon);
                TextParser.checkPlayerCommand("check map", commonDisplayOut, mapDisplayOut, roomDisplayOut,pokemonDisplayOut, pokemonDisplay);
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
         pokemonImageLabel.setIcon(new ImageIcon(transformImage(
                createImageIcon(pokemon.getImgPath(), ""), 120, 120)));
    }

    private static ImageIcon pokemonBattleImageIcon(Pokemon pokemon){
        return new ImageIcon(transformImage(new ImageIcon(String.valueOf(Path.of("src/images", pokemon.getName() +"-Pokemon.png"))), 120, 120));
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

    public void displayOutStatsAndAll() {

        scroll = new JScrollPane (commonDisplay,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);

        String roomName = "Oak's Lab";
        Room startingRoom = InitXML.getRoom(roomName);
        Player.setCurrentRoom(startingRoom);

        //Display room details
        showRoomDetails();

        //Create input panel
        inputP = new JPanel();
        inputP.setLayout(new BoxLayout(inputP, BoxLayout.PAGE_AXIS));
        JTextField inputTF = new JTextField(20);
        inputP.add(new JLabel("Enter your command: "));
        inputP.add(inputTF);

        JButton submitB = new JButton("Submit");
        inputP.add(submitB);
        window.getRootPane().setDefaultButton(submitB);
        submitB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                commonDisplay.setText("");
                TextParser.checkPlayerCommand(inputTF.getText(), commonDisplayOut, mapDisplayOut, roomDisplayOut,pokemonDisplayOut, pokemonDisplay);
                showRoomDetails();
                TextParser.checkPlayerCommand( "check map", commonDisplayOut, mapDisplayOut, roomDisplayOut,pokemonDisplayOut, pokemonDisplay);
                pokemonDisplay.setText("");
                System.setOut(pokemonDisplayOut);
                Player.getPlayersPokemon().get(0).displayOutStatsAndAll();
                System.setOut(System.out);
                inputTF.setText("");
            }
        });

        // copied over from here first into setDisplay()

        //Create room Panel with room details display
        roomPanel = new JPanel();
        roomPanel.setLayout(new BorderLayout());
        pokemonImageLabel = new JLabel("Welcome to Javamon", jigglypuffIcon, JLabel.LEFT);
        roomPanel.add(pokemonImageLabel);
        roomPanel.add(getBorderedPanel(pokemonImageLabel), BorderLayout.LINE_END);
        roomPanel.add(getBorderedPanel(roomDisplay), BorderLayout.LINE_START);


        //the pokemon Details Panel
        JPanel pokemonPanel = new JPanel();
        pokemonPanel.setLayout(new BoxLayout(pokemonPanel, BoxLayout.PAGE_AXIS));
        pokemonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pokemonImageLabel = new JLabel("Your Pokemon's Stats", JLabel.CENTER);
        pokemonPanel.add(pokemonImageLabel);
        pokemonPanel.add(pokemonDisplay);

        //Create Middle Panel with
        //the pokemon Details Panel,
        //the Output Display Panel and the Bag Panel.

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

          middlePanel.add(getBorderedPanel(pokemonPanel), BorderLayout.WEST);
        // the line below was originally commented out- doesn't work
        // middlePanel.add(getBorderedPanel(commonDisplay), BorderLayout.CENTER);
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

    public static void updateDisplayWithBattlingPokemon(Pokemon pokemon){
        pokemonImageLabel = new JLabel("Welcome to Pokemon", pokemonBattleImageIcon(InitXML.getPokemon("Rattata")), JLabel.LEFT);
        roomPanel.add(pokemonImageLabel);
        roomPanel.add(getBorderedPanel(pokemonImageLabel), BorderLayout.LINE_END);
        // when combatlooptrainer calls this method, the correct pokemon object is being passed in
        // should be able to rule out asynchronous call, right?
        // Tried casting the String imgPath as a URL, but getClass() can't be called from static context
            // as it is above in createImageIcon() 237
        // hard coding imgpath in 211 doesn't work, either
        // changed "main" filepath in line 219 to src/images instead of just images

        // tried the below method call and still didn't display image
       //  window.revalidate();
    }

    /**
     * Adds the given component to a JPanel with border.
     */

    private static JPanel getBorderedPanel(JComponent comp) {
        JPanel p = new JPanel();
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        CompoundBorder compound = BorderFactory.createCompoundBorder(blackLine, emptyBorder);
        p.setBorder(compound);
        //Add component to the panel
        p.add(comp);
        return p;
    }

    /**
     * Show the details of the current room of the given player.
     */
    private void showRoomDetails() {
        roomDisplayOut.flush();
        System.setOut(roomDisplayOut);
        Player.showRoomDetails();
        System.setOut(System.out);
    }
}