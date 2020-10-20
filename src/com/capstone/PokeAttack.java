package com.capstone;

import java.util.Random;

public class PokeAttack {

    //Class Fields ------------------------------------------------------------------------------
    private String attackName;
    private int damage; //base damage set from AttackMoves.txt xml.
    private int currentEnergy;
    private int maxEnergy;

    //Constructors ------------------------------------------------------------------------------

    public PokeAttack(String attackName, int damage, int maxEnergy) {
        this.attackName = attackName;
        this.damage = damage;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
    }

    //Getters and Setters -----------------------------------------------------------------------


    public String getAttackName() {
        return attackName;
    }

    public int getDamage() {
        return damage;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    //Class Methods -------------------------------------------------------------------------

    //This is a displayOut that also calculates potential damage, and also shows remaining energy points.
    void displayOutAttackStats(int pokemonAttackStat){
        int potentialDamage = damage + pokemonAttackStat;
        System.out.println(attackName + ": damage: (" + damage + "-" + potentialDamage + ") energy: [" + currentEnergy + "/" + maxEnergy + "]" );
    }

    //This method is used after an attack is chosen and used. Reduce current energy by 1
    void attackUsed(){
        currentEnergy--;
    }

    //This method calculates the amount of damage to be dealt.
    int attack(int attackStat){
        int potentialDamage = damage + attackStat;

        return (int)(Math.random() * (potentialDamage - damage + 1) + damage);
    }




}
