package com.capstone.domainclasses;

public class Item {
    //fields
    private String name;
    private String effect;
    private String description;
    private int price;

    //ctors
    public Item(String name, String effect, String description, int price) {
        this.name = name;
        this.effect = effect;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public static boolean useItem(String item, Pokemon pokemon) {
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
