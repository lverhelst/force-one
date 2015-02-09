package verhelst.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import verhelst.handlers.GameStateManager;
import verhelst.prpg.PhysRPG;

/**
 * Created by Orion on 2/7/2015.
 */
public abstract class GameState {

    protected GameStateManager gsm;
    protected PhysRPG game;

    protected SpriteBatch batch;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudcam;

    protected GameState(GameStateManager gsm1){
        this.gsm = gsm1;
        game = gsm1.game();
        batch = game.getBatch();
        cam = game.getCam();
        hudcam = game.getHudCam();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(float dt);
    public abstract void dispose();

}
