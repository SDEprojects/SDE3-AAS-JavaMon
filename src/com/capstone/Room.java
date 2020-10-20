package com.capstone;

import java.util.Collection;

public class Room {

    //Class Fields
    private String name; //The name of the room, this is set from the Rooms.txt XML <name> tag
    private String description; //The room description, set from the Rooms.txt XML <description> tag
    private String npcName; //The npc name , set from <npc> tag in the Rooms.txt XML
    private String interactableItem; //interactableItem, set from <interactable> tag in Rooms.txt XML

    private String northTile; //uses the data from <adjacent_north> in Rooms.txt XML
    private String southTile; //uses the data from <adjacent_south> in Rooms.txt XML
    private String eastTile; //uses the data from <adjacent_east> in Rooms.txt XML
    private String westTile; //uses the data from <adjacent_west> in Rooms.txt XML

    private NPCFactory npcObj; //NPC object here is the npc that is in this room.



    //Constructor(s)

    //This constructor is for rooms that does not have interactableItems or npcs.
    public Room(String roomName, String roomDescription, String adjNorthTile, String adjSouthTile, String adjEastTile, String adjWestTile) {
        name = roomName;
        description = roomDescription;
        northTile = adjNorthTile;
        southTile = adjSouthTile;
        eastTile = adjEastTile;
        westTile = adjWestTile;

    }

    //Constructor that allows for npc and interactable instantiation.
    public Room(String roomName, String roomDescription, String adjNorthTile, String adjSouthTile, String adjEastTile, String adjWestTile, String roomNPC, String roomInteractable , Collection<NPCFactory> dataList) {
        this(roomName, roomDescription, adjNorthTile,adjSouthTile,adjEastTile,adjWestTile);
        npcName = roomNPC;
        interactableItem = roomInteractable;
        processNPC(dataList);
    }

    //Getters and Setters

    public String getName() {
        return name;
    }

    public NPCFactory getNpcObj() {
        return npcObj;
    }

    public String getNorthTile() {
        return northTile;
    }

    public String getSouthTile() {
        return southTile;
    }

    public String getEastTile() {
        return eastTile;
    }

    public String getWestTile() {
        return westTile;
    }

    public String getNpcName() {
        return npcName;
    }

    public String getInteractableItem() {
        return interactableItem;
    }

    //Business Methods

    public String getDescription(){
        return description;
    }

    void processNPC(Collection<NPCFactory> dataList){
        for(NPCFactory npc : dataList){
            if (npc.getName() == npcName){
                npcObj = npc;
            }
        }
    }

    //This method displays room information to the user.
    void displayOutput(){
        //Prints out the name and description of the room.
        System.out.println("Your current location: " + name);
        System.out.println("Location Description: " + description);

        //Check if npcName is null or empty, if not print out the npc name.
        if(npcName != null && !npcName.trim().isEmpty()){
            System.out.println("You see " + npcName);
        } else {
            System.out.println("No one is here.");
        }
        //Check if itemList is empty
        if(interactableItem != null && !interactableItem.trim().isEmpty()){
            System.out.println("You observe the area and see " + interactableItem); //singular item for first iteration
        } else {
            System.out.println("You look around and find nothing of interest here.");
        }

        //check if npc list is empty, if not, print out the list of npcs in the room.
        /*if (npcList.isEmpty()){
            System.out.println("There's no one else here.");
        } else if (!npcList.isEmpty()){
            System.out.println("People in the room: ");
            for(String person : npcList){
                System.out.println(person);
            }
        }
        //check if itemList is empty, if not , print out the list of interactable items.
        if (itemList.isEmpty()){

        }*/
    }

    public String getRoomDetails() {

        StringBuffer sb = new StringBuffer();

        sb.append("Your current location: " + this.getName()).append("\n");

        sb.append("Location Description: " + this.getDescription()).append("\n");

        // Check if npcName is null or empty, if not print out the npc name.
        String npcName = this.getNpcName();
        if (npcName != null && !npcName.trim().isEmpty()) {
            sb.append("You see " + npcName).append("\n");
        } else {
            sb.append("No one is here.").append("\n");
        }
        // Check if itemList is empty
        String interactableItem = this.getInteractableItem();
        if (interactableItem != null && !interactableItem.trim().isEmpty()) {
            sb.append("You observe the area and see " + interactableItem).append("\n");
        } else {
            sb.append("You look around and find nothing of interest here.").append("\n");
        }
        return sb.toString();
    }
}

