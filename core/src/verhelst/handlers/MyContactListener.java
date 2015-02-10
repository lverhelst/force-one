package verhelst.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import verhelst.states.PlayGS;

/**
 * Created by Orion on 2/8/2015.
 */
public class MyContactListener  implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();


        if(fA.getBody().getUserData().equals("PLAYER")){
            PlayGS.jumps = 2;
        }
        if(fB.getBody().getUserData().equals("PLAYER")){
            PlayGS.jumps = 2;
        }
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if(fA.getBody().getUserData().equals("PLAYER")&& fA.getBody().getUserData().equals("PLATFORM")){
            if(fA.getBody().getLinearVelocity().y > 0)
                contact.setEnabled(false);
        }
        if(fB.getBody().getUserData().equals("PLAYER") && fA.getBody().getUserData().equals("PLATFORM")){
            if(fB.getBody().getLinearVelocity().y > 0)
                contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
