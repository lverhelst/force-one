package verhelst.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import verhelst.handlers.Animation;
import verhelst.handlers.B2DVars;


/**
 * Created by Orion on 2/10/2015.
 */
public class B2DSprite {

    protected Body body;
    protected Animation animation;
    float width, height;


    public B2DSprite(Body body){
        this.body = body;
        this.animation = new Animation();// new Sprite(new Texture(Gdx.files.internal("head.png")));
    }

    public void setAnimation(TextureRegion reg, float delay) {
        setAnimation(new TextureRegion[] { reg }, delay);
    }

    public void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }

    public void update(float dt) {
        animation.update(dt);
    }

    public void render(Batch batch){

        batch.draw(animation.getFrame(), body.getPosition().x * B2DVars.PPM - width/2, body.getPosition().y * B2DVars.PPM - height/2);

    }

    public Body getBody() { return body; }
    public Vector2 getPosition() { return body.getPosition(); }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

}
