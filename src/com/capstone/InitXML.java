package com.capstone;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class InitXML {
    //fields to keep the list of rooms and npc instances referencable
    public Collection<NPCFactory> listOfNPCs = new ArrayList<>();
    public Collection<Room> listOfRooms = new ArrayList<>();
    public Collection<Pokemon> listOfPokemon = new ArrayList<>();
    public Collection<Item> listOfItems = new ArrayList<>();
    public Collection<PokeAttack> listOfAttacks = new ArrayList<>();

    //basically a getter for the dialog field for the NPC that gets passed to it
    public String npcDialog(String npcName){
        for (NPCFactory NPC : listOfNPCs) {
            if (NPC.getName().toLowerCase().equals(npcName.toLowerCase())) {
                return NPC.getDialog();
            }
        }
        return null;
    }

    public NPCFactory getNPC(String npcName) {
        for (NPCFactory NPC : listOfNPCs) {
            if (NPC.getName().toLowerCase().equals(npcName.toLowerCase())) {
                return NPC;
            }
        }
        return null;
    }

    //used as a getter for the list of items an npc has
    public Collection<String> npcItem(String npcName){
        for (NPCFactory NPC : listOfNPCs) {
            if (NPC.getName().toLowerCase().equals(npcName.toLowerCase())) {
                return NPC.getInventory();
            }
        }
        return null;
    }

    //used to clear the npcs' inventories after we receive their items
    public void clearNPCInventory(String npcName) {
        for (NPCFactory NPC : listOfNPCs) {
            if (NPC.getName().toLowerCase().equals(npcName.toLowerCase())) {
                NPC.clearInventory();
            }
        }
    }

    //getter to return the actual Room object for the room name that gets passed to it
    public Room getRoom(String roomName) {
        for (Room theRoom : listOfRooms) {
            if (theRoom.getName().equals(roomName)) {
                return theRoom;
            }
        }
        return null;
    }

    //initialization method for putting all the NPCs in the XML txt file into the listOfNPCS
    public void initNPCs() {
        try {
            //big formatting block for taking XML from the provided txt doc "NPCs.txt" in data
            File inputFile = new File(String.valueOf(Path.of("data", "NPCs.txt")));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList npcList = doc.getElementsByTagName("npc"); //npcList now contains all the nodes in the file with tag NPC

            for (int temp = 0; temp < npcList.getLength(); temp++) { //iterating over all the nodes in npcList
                Node npc = npcList.item(temp);

                //downcast, getting element we want, then recasting to node, then getting the text and calling the constructor for NPCFactory to add the NPC to the list .... wat... lol
                Element npcEle = (Element) npc;
                String npcName = npcEle.getElementsByTagName("name").item(0).getTextContent();
                String npcDialog = npcEle.getElementsByTagName("dialog").item(0).getTextContent();
                String npcItems = npcEle.getElementsByTagName("item").item(0).getTextContent();
                int npcMoney = Integer.parseInt(npcEle.getElementsByTagName("money").item(0).getTextContent());
                String npcPokemonName = npcEle.getElementsByTagName("pokemon").item(0).getTextContent();
                listOfNPCs.add(new NPCFactory(npcName,npcDialog,npcItems,npcMoney,npcPokemonName,listOfPokemon));
            }
        }
        catch (Exception e) {
            System.out.println("there was an error initializing the NPCs list.");
        }

    }

    //same thing as npc but with items
    public void initItems() {
        try {

            File inputFile = new File(String.valueOf(Path.of("data", "Item.txt")));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList itemList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < itemList.getLength(); temp++) {
                Node item = itemList.item(temp);


                Element itemEle = (Element) item;
                String itemName = itemEle.getElementsByTagName("name").item(0).getTextContent();
                String itemEffect = itemEle.getElementsByTagName("effect").item(0).getTextContent();
                String itemDescription = itemEle.getElementsByTagName("description").item(0).getTextContent();
                int itemPrice = Integer.parseInt(itemEle.getElementsByTagName("price").item(0).getTextContent());
                listOfItems.add(new Item(itemName, itemEffect, itemDescription, itemPrice));

            }
        }
        catch (Exception e) {
            System.out.println("there was an error initializing the NPCs list.");
        }

    }

    //same thing as NPCs, but for Rooms
    public void initRooms() {
        try {
            //big formatting block for taking XML from the provided txt doc "Rooms.txt" in data
            File inputFile = new File(String.valueOf(Path.of("data", "Rooms.txt")));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList roomList = doc.getElementsByTagName("room"); //roomList now contains all the nodes in the file with tag NPC

            for (int temp = 0; temp < roomList.getLength(); temp++) { //iterating over all the nodes in npcList

                Node room = roomList.item(temp);

                //downcast, getting element we want, then recasting to node, then getting the text .... what
                Element roomEle = (Element) room;
                String roomName = roomEle.getElementsByTagName("name").item(0).getTextContent();

                String roomDescription = roomEle.getElementsByTagName("description").item(0).getTextContent();

                //If the string "nothing" is returned from any of the adjacent rooms, that means you cannot navigate to them.
                String roomAdjNorth = roomEle.getElementsByTagName("adjacent_north").item(0).getTextContent();
                String roomAdjSouth = roomEle.getElementsByTagName("adjacent_south").item(0).getTextContent();
                String roomAdjEast = roomEle.getElementsByTagName("adjacent_east").item(0).getTextContent();
                String roomAdjWest = roomEle.getElementsByTagName("adjacent_west").item(0).getTextContent();

                //roomNPC and roomInteractable holds the value from the rooms.txt xml with the npc and interactable tags.
                String roomNPC = roomEle.getElementsByTagName("npc").item(0).getTextContent();
                String roomInteractable = roomEle.getElementsByTagName("interactable").item(0).getTextContent();

                listOfRooms.add(new Room(roomName,roomDescription,roomAdjNorth,roomAdjSouth,roomAdjEast,roomAdjWest, roomNPC, roomInteractable, listOfNPCs));

            }
        }
        catch (Exception e) {
            System.out.println("there was an error initializing the rooms list.");
        }

    }

    public void initPokemon(){
        try {

            //big formatting block for taking XML from the provided txt doc "Rooms.txt" in data
            File inputFile = new File(String.valueOf(Path.of("data", "PokemonExperimental.txt")));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList pokemonList = doc.getElementsByTagName("pokemon"); //roomList now contains all the nodes in the file with tag NPC

            for (int temp = 0; temp < pokemonList.getLength(); temp++) { //iterating over all the nodes in npcList

                Node pokemon = pokemonList.item(temp);

                //downcast, getting element we want, then recasting to node, then getting the text .... what
                Element pokeEle = (Element) pokemon;
                String pokemonName = pokeEle.getElementsByTagName("name").item(0).getTextContent();

                String pokemonType = pokeEle.getElementsByTagName("type").item(0).getTextContent();

                //If the string "nothing" is returned from any of the adjacent rooms, that means you cannot navigate to them.
                //As of now health attack and startingexp are strings. May need a different way of getting values or need to tryparse
                //the strings into int.

                //TODO: implement String to int try parse conversion.
                int pokeHealth = Integer.parseInt(pokeEle.getElementsByTagName("health").item(0).getTextContent());
                int pokeAttack = Integer.parseInt(pokeEle.getElementsByTagName("attack").item(0).getTextContent());
                int startingExp = Integer.parseInt(pokeEle.getElementsByTagName("startingExp").item(0).getTextContent());
                String move1 = pokeEle.getElementsByTagName("move1").item(0).getTextContent();
                String move2 = pokeEle.getElementsByTagName("move2").item(0).getTextContent();


                //roomNPC and roomInteractable holds the value from the rooms.txt xml with the npc and interactable tags.
                //TODO: implement constructor with stats.
                listOfPokemon.add(new Pokemon(pokemonName, pokemonType, pokeHealth, 5, pokeAttack, move1, move2, listOfAttacks,startingExp)); //Pokelevel is hardcoded here.
                //TODO: implement a way of associating level to room/area/or npc. For now it is hard coded to five.

            }
        }
        catch (Exception e) {
            System.out.println("there was an error with initializing the Pokemon list.");
        }

    }
    //Always initAttacks() method before Pokemon.
    //This method initializes a Pokemon attack moves list
    public void initAttacks(){
        try {

            //big formatting block for taking XML from the provided txt doc "Rooms.txt" in data
            File inputFile = new File(String.valueOf(Path.of("data", "AttackMoves.txt")));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList attackMovesList = doc.getElementsByTagName("attack"); //roomList now contains all the nodes in the file with tag NPC

            for (int temp = 0; temp < attackMovesList.getLength(); temp++) { //iterating over all the nodes in npcList

                Node attackMoves = attackMovesList.item(temp);

                //downcast, getting element we want, then recasting to node, then getting the text .... what
                Element attackEle = (Element) attackMoves;

                //Getting the Attack moves data from the AttackMoves.txt xml
                String attackName = attackEle.getElementsByTagName("name").item(0).getTextContent();
                int attackDamage = Integer.parseInt(attackEle.getElementsByTagName("damage").item(0).getTextContent());
                int attackEnergy = Integer.parseInt(attackEle.getElementsByTagName("energy").item(0).getTextContent());


                //Add each move into the listOfAttacks list.
                listOfAttacks.add(new PokeAttack(attackName,attackDamage,attackEnergy));

            }
        }
        catch (Exception e) {
            System.out.println("there was an error with initializing the Attack Moves list.");
        }

    }
}
