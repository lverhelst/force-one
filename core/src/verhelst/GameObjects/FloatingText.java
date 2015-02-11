package verhelst.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

import verhelst.Misc.Assets;

/**
 * Created by Orion on 2/10/2015.
 */
public class FloatingText {
    private float x, origX;
    private float y, origY;
    private float angle;
    private float alpha;
    private int value;
    private int update_iterations;
    private CharSequence cs;
    private boolean isRemoveable;
    private Random random;
    private double rand_force;
    private float red;
    private float green;
    private float blue;

    private static final float densS = Gdx.graphics.getDensity() * 2;

    public void setCs(CharSequence cs) {
        this.cs = cs;

    }

    public void setValue(int value) {
        this.value = value;
        this.cs = "" + Math.abs(value);
        //Damage done = red colour
        if (this.value > 0) {
            this.red = 0.7f;
            this.green = 0.15f;
            this.blue = 0.15f;
        }
        //healing done = green colour
        else if (this.value < 0) {
            this.red = 0.15f;
            this.green = 0.7f;
            this.blue = 0.15f;
        }
        //0 done = white color
        else {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
        }
    }

    public void setY(float y) {
        this.y = y;
        this.origY = y;
    }

    public void setX(float x) {
        this.x = x;
        this.origX = x;
    }

    public FloatingText(int amount, float screenx, float screeny) {
        this.value = amount;
        this.x = screenx;
        this.y = screeny;
        this.origX = screenx;
        this.origY = screeny;
        this.update_iterations = 0;
        this.alpha = 1.0f;

        this.isRemoveable = false;
        cs = "" + Math.abs(value);
        this.random = new Random();
        this.rand_force = random.nextGaussian();
        rand_force = (rand_force > 0 ? rand_force * 2 : rand_force / 2);
        //set angle in degrees, convert to radians
        this.angle = (float) Math.toRadians(random.nextInt(120) + 30);//+ 30;

        //Damage done = red colour
        if (this.value > 0) {
            this.red = 0.7f;
            this.green = 0.15f;
            this.blue = 0.15f;
        }
        //healing done = green colour
        else if (this.value < 0) {
            this.red = 0.15f;
            this.green = 0.7f;
            this.blue = 0.15f;
        }
        //0 done = white color
        else {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
        }
    }

    public FloatingText(String value, float screenx, float screeny) {

        this.value = Integer.MAX_VALUE;
        this.x = screenx;
        this.y = screeny;
        this.origX = screenx;
        this.origY = screeny;
        this.update_iterations = 0;
        this.alpha = 1.0f;


        this.isRemoveable = false;
        cs = "" + value;
        this.random = new Random();
        this.rand_force = random.nextGaussian();
        rand_force = (rand_force > 0 ? rand_force * 2 : rand_force / 2);
        //set angle in degrees, convert to radians
        this.angle = (float) Math.toRadians(random.nextInt(120) + 30);//+ 30;
        //White colour
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
    }


    public void update(boolean useZippy) {
        update_iterations++;
        if(useZippy) {
            this.x += Math.cos(this.angle) * densS;  //CAH
            //use the alpha value to pull the number down, this gives a nice curve without complicated computations
            //the * 1.1 is just to adjust the intensity of the (-1 + alpha) portion
            //+ 1/(value == 0? 1: value)
            this.y += (Math.sin(this.angle) + (-1 + this.alpha) + rand_force) * densS; //SOH
        }
        //at an alpha < 0, the random number can be removed from where it is displayed
        //since it will be invisible
        if (this.alpha <= 0.04f) {
            this.isRemoveable = true;
        }else{
            this.alpha -= 0.03f;
        }
    }

    public int getUpdate_iterations() {
        return this.update_iterations;
    }

    public boolean isRemoveable() {
        return isRemoveable;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public CharSequence getCs() {
        return cs;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAngle(int degrees){
        this.angle = (float) Math.toRadians(degrees);
    }

    public FloatingText reset() {

        this.value = Integer.MAX_VALUE;
        this.x = -100;
        this.y = -100;
        this.update_iterations = 0;
        this.alpha = 1.0f;


        this.isRemoveable = false;
        cs = "" + value;
        this.random = new Random();
        this.rand_force = random.nextGaussian();
        rand_force = (rand_force > 0 ? rand_force * 2 : rand_force / 2);
        //set angle in degrees, convert to radians
        this.angle = (float) Math.toRadians(random.nextInt(120) + 30);//+ 30;
        //White colour
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;

        return this;
    }

    public void render(SpriteBatch batch){
        //Assets.floatingTextFont.setColor(red, green, blue, alpha);
        Assets.floatingTextFont.setColor(Color.WHITE);
        Assets.floatingTextFont.draw(batch, cs, x, y);
    }

}
