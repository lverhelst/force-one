package verhelst.GameObjects;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Orion on 2/10/2015.
 */
public class Character extends B2DSprite {

    Breed breed;
    int currentHealth, currentAttack, currentDefence, currentStamina;

    Equipment weapon, helmet, offhand, torso;


    public Character(Body body) {
        super(body);
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
        calculateStats();
    }

    public int takeDamage(int damage){
        System.out.println(getName() + " took damage: " + damage );
        this.currentHealth -= damage;
        return currentHealth;
    }

    public int addHealth(int health){
        this.currentHealth += health;
        return currentHealth;
    }

    public String getName(){
        return breed.getName();
    }

    public boolean isDead(){
        return currentHealth <= 0;
    }

    public int getAttack(){
        return currentAttack;
    }

    public void calculateStats(){
        currentHealth += breed.getInitialHealth() + ((weapon != null ? weapon.getHp() : 0) + (offhand != null ? offhand.getHp() : 0) + (helmet != null ? helmet.getHp() :0) + (torso != null ? torso.getHp() : 0));
        currentDefence += breed.getBaseDefence() + ((weapon != null ? weapon.getDefence() : 0) + (offhand != null ? offhand.getDefence()  : 0) + (helmet != null ? helmet.getDefence()  :0) + (torso != null ? torso.getDefence()  : 0));
        currentAttack += breed.getBaseAttack() + ((weapon != null ? weapon.getDamage() : 0) + (offhand != null ? offhand.getDamage() : 0) + (helmet != null ? helmet.getDamage() :0) + (torso != null ? torso.getDamage() : 0));
        currentStamina += breed.getBaseStamina() + ((weapon != null ? weapon.getStamina() : 0) + (offhand != null ? offhand.getStamina() : 0) + (helmet != null ? helmet.getStamina() :0) + (torso != null ? torso.getStamina() : 0));
    }

    public void equipItem(Equipment item){
        if(item.getType() == EquippableType.WEAPON)
            weapon = item;
        if(item.getType() == EquippableType.OFFHAND)
            offhand = item;
        if(item.getType() == EquippableType.HELMET)
            helmet = item;
        if(item.getType() == EquippableType.TORSO)
            torso = item;
        calculateStats();
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public int getCurrentDefence() {
        return currentDefence;
    }

    public int getCurrentStamina() {
        return currentStamina;
    }

    public Equipment getWeapon() {
        return weapon;
    }

    public Equipment getHelmet() {
        return helmet;
    }

    public Equipment getOffhand() {
        return offhand;
    }

    public Equipment getTorso() {
        return torso;
    }
}
