package verhelst.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import verhelst.GameObjects.*;
import verhelst.GameObjects.Character;
import verhelst.prpg.PhysRPG;
import verhelst.states.GameState;
import verhelst.states.PlayGS;

/**
 * Created by Orion on 2/8/2015.
 */
public class MyContactListener  implements ContactListener {


    Fixture fA, fB, fC;
    Character a = null, b = null, c= null;
    int dmgToA, dmgToB;

    public MyContactListener(){

    }

    @Override
    public void beginContact(Contact contact) {
        fA = contact.getFixtureA();
        fB = contact.getFixtureB();


        if(fA.getUserData() instanceof String && fA.getUserData().equals("B_SENSOR")){
            if(fA.getBody().getLinearVelocity().y <= 0)
                fA.getBody().setLinearVelocity(-fA.getBody().getLinearVelocity().x, fA.getBody().getLinearVelocity().y);
        } else
        if(fB.getUserData() instanceof String &&  fB.getUserData().equals("B_SENSOR")){
            if(fB.getBody().getLinearVelocity().y <= 0)
                fB.getBody().setLinearVelocity(-fB.getBody().getLinearVelocity().x, fB.getBody().getLinearVelocity().y);
        }

            if(fA.getBody().getUserData() instanceof Character && ((Character)fA.getBody().getUserData()).getName().equals("Player") ){
                PlayGS.jumps = 2;
            }
            if(fB.getBody().getUserData() instanceof Character && ((Character)fB.getBody().getUserData()).getName().equals("Player") ){
                PlayGS.jumps = 2;
            }

            if(fA.getBody().getUserData() instanceof Character && fB.getBody().getUserData() instanceof Character && ((a = (Character)fA.getBody().getUserData()).getName().equals("Player") | (b = (Character)fB.getBody().getUserData()).getName().equals("Player"))){
                //Swap, keep the order attack is done 'random'
                if(((Character)fB.getBody().getUserData()).getName().equals("Player")) {
                    c = b;
                    b = a;
                    a = c;
                    fC = fB;
                    fB = fA;
                    fA = fB;
                }

                dmgToB=  a.getAttack() + (a.getName().equals("Player") ? (int) Math.abs((fA.getBody().getLinearVelocity().x) + Math.abs(fA.getBody().getLinearVelocity().y))  : 1);
                b.takeDamage(dmgToB);
                if(!b.isDead()) {
                    dmgToA = b.getAttack() + (b.getName().equals("Player") ? (int) Math.abs((fB.getBody().getLinearVelocity().x) + Math.abs(fB.getBody().getLinearVelocity().y)) : 1);
                    a.takeDamage(dmgToA);
                }else{
                    b.explode();
                    //disable collision with player
                    Filter f = fB.getFilterData();
                    f.maskBits = B2DVars.PLATFORM_BIT;
                    fB.setFilterData(f);
                }




                if(a.getName().equals("Player")){
                    PlayGS.addFloatingText(fB.getBody(), "-" + dmgToB);
                    PlayGS.addHUDText("-" + dmgToA);
                }else{
                    PlayGS.addFloatingText(fA.getBody(), "-" + dmgToA);
                    PlayGS.addHUDText("-" + dmgToB);
                }

            }






    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        fA = contact.getFixtureA();
        fB = contact.getFixtureB();

        if(fA.getBody().getUserData() instanceof Character && ((Character)fA.getBody().getUserData()).getName().equals("Player") && fB.getBody().getUserData().equals("PLATFORM")){
            if(fA.getBody().getLinearVelocity().y > 0)
                contact.setEnabled(false);
        }
        if(fB.getBody().getUserData() instanceof Character && ((Character)fB.getBody().getUserData()).getName().equals("Player") && fA.getBody().getUserData().equals("PLATFORM")){
            if(fB.getBody().getLinearVelocity().y > 0)
                contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
