package verhelst.states;

import static verhelst.handlers.B2DVars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;

import verhelst.handlers.GameStateManager;
import verhelst.handlers.MyContactListener;
import verhelst.handlers.MyInput;
import verhelst.handlers.MyInputAdapter;
import verhelst.prpg.PhysRPG;

/**
 * Created by Orion on 2/7/2015.
 */
public class PlayGS extends GameState {

    private World world;
    private Box2DDebugRenderer bdr;


    private OrthographicCamera b2dCam;

    private Body playerBody;

    //for translating screen coordinates
    private Vector3 touchPoint = new Vector3();
    private int[] xy;

    //input velocity scaler
    private final int INPUT_VELOCITY_SCALER = 5;

    private LinkedList<Rectangle> rectangles;

    public PlayGS(GameStateManager gsm){
        super(gsm);

        rectangles = new LinkedList<Rectangle>();

        world = new World(new Vector2(0,-9.81f), true);
        world.setContactListener(new MyContactListener());
        world.setVelocityThreshold(0.0f);

        bdr = new Box2DDebugRenderer();

       /* //create a platform/wall
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(160/PPM, 5/PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);


        //bottom wall
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(160 / PPM, 5 / PPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.restitution = 1f;
        body.createFixture(fixDef);

        //top wall
        bodyDef.position.set(150/PPM, 235/PPM);
        body = world.createBody(bodyDef);

        shape.setAsBox(160/PPM, 5/PPM);
        fixDef.shape = shape;
        fixDef.restitution = 1f;
        body.createFixture(fixDef);


        //side walls left
        bodyDef.position.set(5/PPM, 120/PPM);
        body = world.createBody(bodyDef);

        shape.setAsBox(5/PPM, 110/PPM);
        fixDef.shape = shape;
        fixDef.restitution = 1f;
        body.createFixture(fixDef);

        //side walls right
        bodyDef.position.set(315/PPM, 120/PPM);
        body = world.createBody(bodyDef);

        shape.setAsBox(5/PPM, 110/PPM);
        fixDef.shape = shape;
        fixDef.restitution = 1f;
        body.createFixture(fixDef);
*/
        BodyDef bodyDef = new BodyDef();

        //create falling box
        bodyDef.position.set(160/PPM,120/PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 0.5f;
        playerBody = world.createBody(bodyDef);

        CircleShape cshape = new CircleShape();
        cshape.setRadius(5/PPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = cshape;

        playerBody.createFixture(fixDef);

        //box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, PhysRPG.V_WIDTH /PPM, PhysRPG.V_HEIGHT /PPM);


        Gdx.input.setInputProcessor(new MyInputAdapter());

    }

    private boolean checkRectangles(Body body){
        if(rectangles.size() < 1)
            return false;

        return body.getPosition().x > rectangles.getFirst().getX() && body.getPosition().x < rectangles.getLast().getX() + rectangles.getLast().getWidth();
    }

    private void generateRectangle(Body fromBody){
        float xLocation = fromBody.getPosition().x;
        float yLocation = fromBody.getPosition().y;

        //create a platform/wall
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(xLocation,100/PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        //bottom wall
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(160 / PPM, 5 / PPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.restitution = 1f;
        body.createFixture(fixDef);

        if(rectangles.size() > 0 && xLocation - 160/PPM < rectangles.getFirst().getX()) {
            rectangles.addFirst(new Rectangle(xLocation - 160 / PPM, 100 / PPM - 5 / PPM, 320 / PPM, 5 / PPM));
            //if(rectangles.size() > 10)
            //    rectangles.removeLast();
        }
        else {
            rectangles.add(new Rectangle(xLocation - 160 / PPM, 100 / PPM - 5 / PPM, 320 / PPM, 5 / PPM));
            //if(rectangles.size() > 10)
            //    rectangles.removeFirst();
        }

    }


    @Override
    public void handleInput() {
        if(MyInput.hasTouch()){
            xy = MyInput.consumeXY();
            b2dCam.unproject(touchPoint.set(xy[0],xy[1],0));

            playerBody.setLinearVelocity((touchPoint.x  - playerBody.getPosition().x) * INPUT_VELOCITY_SCALER  , (touchPoint.y - playerBody.getPosition().y) * INPUT_VELOCITY_SCALER);

        }
    }


    @Override
    public void update(float dt) {
        handleInput();
        if(!checkRectangles(playerBody)) {
            System.out.println("Generate Body");
            generateRectangle(playerBody);
        }

        world.step(dt,6,2);
    }

    @Override
    public void render(float dt) {
        //camera follow box
        b2dCam.position.set(playerBody.getPosition().x, playerBody.getPosition().y, 0);
        b2dCam.update();

        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        bdr.render(world, b2dCam.combined);
    }

    @Override
    public void dispose() {

    }
}
