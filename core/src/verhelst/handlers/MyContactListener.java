package verhelst.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
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

        if(fA.getBody().getUserData() instanceof Character && ((Character)fA.getBody().getUserData()).getName().equals("Player") ){
            PlayGS.jumps = 2;
        }
        if(fB.getBody().getUserData() instanceof Character && ((Character)fB.getBody().getUserData()).getName().equals("Player") ){
            PlayGS.jumps = 2;
        }

        if(fA.getBody().getUserData() instanceof Character && fB.getBody().getUserData() instanceof Character && ((a = (Character)fA.getBody().getUserData()).getName().equals("Player") || (b = (Character)fB.getBody().getUserData()).getName().equals("Player"))){
            //Swap, keep the order attack is done 'random'
            if(System.currentTimeMillis() % 2 == 0) {
                c = b;
                b = a;
                a = c;
                fC = fB;
                fB = fA;
                fA = fB;
            }

            dmgToB=  a.getAttack() + (a.getName().equals("Player") ? (int) Math.abs((fA.getBody().getLinearVelocity().x) + Math.abs(fA.getBody().getLinearVelocity().y))  : 1);
            dmgToA = b.getAttack() +  (b.getName().equals("Player") ? (int) Math.abs((fB.getBody().getLinearVelocity().x) + Math.abs(fB.getBody().getLinearVelocity().y)) : 1);

            b.takeDamage(dmgToB);
            a.takeDamage(dmgToA);

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

        if(fA.getBody().getUserData() instanceof Character && ((Character)fA.getBody().getUserData()).getName().equals("Player") && fA.getBody().getUserData().equals("PLATFORM")){
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
