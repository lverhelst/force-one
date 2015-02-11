package verhelst.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

import verhelst.handlers.B2DVars;


/**
 * Created by Orion on 2/10/2015.
 */
public class B2DSprite {

    protected Body body;
    //Make into animation
    protected Sprite sprite;


    public B2DSprite(Body body){
        this.body = body;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("head.png")));

    }

    public void render(SpriteBatch batch){
        sprite.setPosition(body.getPosition().x * B2DVars.PPM - sprite.getWidth()/2,body.getPosition().y * B2DVars.PPM - sprite.getHeight()/2.75f );
        sprite.draw(batch);
    }
}
