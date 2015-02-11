package verhelst.prpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.xml.validation.ValidatorHandler;

import verhelst.Misc.Assets;
import verhelst.handlers.GameStateManager;

public class PhysRPG extends ApplicationAdapter {

    public static final String TITLE = "PHYSICS RPG";
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 240;
    public static final int SCALE = 2;

    private static final float STEP = 1/60f;
    private float accum;


    private SpriteBatch batch;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;

	@Override
	public void create () {
        Assets ass = new Assets();
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        gsm = new GameStateManager(this);

	}

	@Override
	public void render () {

       // accum += Gdx.graphics.getDeltaTime();
      //  while(accum >= STEP){
        //    accum -= STEP;
            gsm.update(STEP);
            gsm.render(STEP);
       // }

	}

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public OrthographicCamera getHudCam() {
        return hudCam;
    }
}
