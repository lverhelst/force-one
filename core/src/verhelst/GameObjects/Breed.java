package verhelst.GameObjects;

/**
 * Created by Orion on 2/10/2015.
 */
public class Breed {

    private int initialHealth;
    private int baseAttack;
    private int baseDefence;
    private int baseStamina;

    private String name;

    public Breed(String name, int hp, int baseAttack){
        this.name = name;
        this.initialHealth = hp;
        this.baseAttack = baseAttack;
        this.baseDefence = 1;
        this.baseStamina = 100;
    }

    public int getInitialHealth() {
        return initialHealth;
    }

    public String getName() {
        return name;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefence() {
        return baseDefence;
    }

    public int getBaseStamina() {
        return baseStamina;
    }
}
