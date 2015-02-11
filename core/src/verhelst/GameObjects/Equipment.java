package verhelst.GameObjects;

import java.util.Random;

/**
 * Created by Orion on 2/10/2015.
 */
public class Equipment extends Item {

    private EquippableType type;

    int damage, hp, stamina, defence;

    static Random rng = new Random();

    public static Equipment generateRandomEquipment(){
        Equipment toReturn = new Equipment(EquippableType.values()[rng.nextInt(EquippableType.values().length)]);
        toReturn.setDamage(rng.nextInt(10));
        toReturn.setDefence(rng.nextInt(10));
        toReturn.setHp(rng.nextInt(10));
        toReturn.setStamina(rng.nextInt(10));
        return toReturn;
    }

    public Equipment(EquippableType type){
        this.type =type;
        this.damage = 0;
        this.hp = 0;
        this.stamina = 0;
        this.defence = 0;
    }

    public EquippableType getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }
}
