package verhelst.states;

import static verhelst.handlers.B2DVars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.LinkedList;
import java.util.Random;

import verhelst.handlers.B2DVars;
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
    private LinkedList<Body> bodies;
    private LinkedList<Body[]> blocksLists;
    private LinkedList<Body[]> baddyLists;
    private Random rng = new Random();

    public static int jumps = 2;

    public PlayGS(GameStateManager gsm){
        super(gsm);

        rectangles = new LinkedList<Rectangle>();
        bodies = new LinkedList<Body>();
        blocksLists = new LinkedList<Body[]>();
        baddyLists = new LinkedList<Body[]>();
        world = new World(new Vector2(0,-9.81f), true);
        world.setContactListener(new MyContactListener());
        world.setVelocityThreshold(0.0f);

        bdr = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();

        //create falling box
        bodyDef.position.set(160/PPM,120/PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 0.5f;
        playerBody = world.createBody(bodyDef);
        playerBody.setUserData("PLAYER");

        CircleShape cshape = new CircleShape();
        cshape.setRadius(5/PPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = cshape;
        fixDef.restitution = 0.9f;
        fixDef.filter.categoryBits = B2DVars.PLAYER_BIT;
        fixDef.filter.maskBits = B2DVars.BLOCK_BIT | B2DVars.PLATFORM_BIT | B2DVars.ENEMY_BIT;
        playerBody.createFixture(fixDef);

        //box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, PhysRPG.V_WIDTH /PPM, PhysRPG.V_HEIGHT /PPM);


        Gdx.input.setInputProcessor(new MyInputAdapter());

    }

    private int checkPlatforms(Body body){
        if(rectangles.size() < 1)
            return 1;

        if(body.getPosition().x < rectangles.getFirst().getX() + rectangles.getFirst().getWidth()/4)
            return -1;
        if(body.getPosition().x > rectangles.getLast().getX() + 3 * rectangles.getLast().getWidth()/4)
            return 1;
        return 0;
    }



    private void generatePlatform(int side) {
        System.out.println("Generating :  " + side);
        float xLocation, yLocation;
        if (rectangles.size() < 1){
            xLocation = playerBody.getPosition().x;
            yLocation = 100 / PPM;
        }else {
            xLocation = (side == -1 ? rectangles.getFirst() : rectangles.getLast()).getX() + ((side == -1? side : 3 * side) * 160 / PPM);

            yLocation = (100 + (rng.nextInt(7) == 0 ? 100 : 0))/PPM;
        }
        //create a platform/wall
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(xLocation,yLocation);

        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        body.setUserData("PLATFORM");
        //bottom wall
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(160 / PPM, 5 / PPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.filter.categoryBits = B2DVars.PLATFORM_BIT;
        fixDef.filter.maskBits = -1;
        body.createFixture(fixDef);


        if(side == -1){
            rectangles.addFirst(new Rectangle(xLocation - 160 / PPM, yLocation- 5 / PPM, 320 / PPM, 5 / PPM));
            bodies.addFirst(body);
            blocksLists.addFirst(generateBlocks(rectangles.getFirst()));
            baddyLists.addFirst(generateBaddies(rectangles.getFirst()));

            if(rectangles.size() > 10) {
                rectangles.removeLast();
                world.destroyBody(bodies.removeLast());
                for(Body b : blocksLists.removeLast()){
                    if(b != null) world.destroyBody(b);
                }
                for(Body b : baddyLists.removeLast()){
                    if(b != null) world.destroyBody(b);
                }
            }
        }
        else {
            rectangles.add(new Rectangle(xLocation - 160 / PPM, yLocation - 5 / PPM, 320 / PPM, 5 / PPM));
            bodies.add(body);

            blocksLists.add(generateBlocks(rectangles.getLast()));
            baddyLists.add(generateBaddies(rectangles.getLast()));

            if(rectangles.size() > 10) {
                rectangles.removeFirst();
                world.destroyBody(bodies.removeFirst());

                for(Body b : blocksLists.removeFirst()){
                    if(b != null) world.destroyBody(b);
                }

                for(Body b : baddyLists.removeFirst()){
                    if(b != null) world.destroyBody(b);
                }

            }
        }
    }

    private Body[] generateBlocks(Rectangle putBlockOnBody){
        int numb_blocks = rng.nextInt(5);
        int stack;

        float xLocation = putBlockOnBody.getX();
        float yLocation = putBlockOnBody.getY() + putBlockOnBody.getHeight();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body;
        //bottom wall
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / PPM, 5 / PPM);
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.filter.categoryBits = B2DVars.BLOCK_BIT;
        fixDef.filter.maskBits = -1; //collide with everything

        Body[] bd = new Body[13];
        int k = 0;
        for(int i = 0; i  < numb_blocks; i++){
            stack = rng.nextInt(3) + 1;
            for (int j = 1; j <= stack; j++) {
                //create a box
                bodyDef.position.set(xLocation + (320/numb_blocks/PPM) * i,yLocation + (10/PPM) * j);
                body = world.createBody(bodyDef);
                body.setUserData("BOX");
                body.createFixture(fixDef);
                bd[++k] = body;
            }
        }
        return bd;
    }

    private Body[] generateBaddies(Rectangle putBlockOnBody){
        int numb_blocks = rng.nextInt(3);


        float xLocation = putBlockOnBody.getX();
        float yLocation = putBlockOnBody.getY() + putBlockOnBody.getHeight();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body;
        //bottom wall
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / PPM, 15 / PPM);
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 40;
        fixDef.filter.categoryBits = B2DVars.ENEMY_BIT;
        fixDef.filter.maskBits = -1; //collide with everything

        Body[] bd = new Body[numb_blocks];

        for(int i = 0; i < numb_blocks; i++){

                //create a box
                bodyDef.position.set(xLocation + (137/PPM) * i + 1,yLocation + (10/PPM));
                body = world.createBody(bodyDef);
                body.setUserData("ENEMY");
                body.createFixture(fixDef);
                bd[i] = body;

        }
        return bd;
    }


    @Override
    public void handleInput() {
        if(MyInput.hasTouch()) {
            xy = MyInput.consumeXY();
            if (jumps > 0){
                jumps = Math.max(0, --jumps);
                b2dCam.unproject(touchPoint.set(xy[0], xy[1], 0));
                playerBody.setLinearVelocity((touchPoint.x - playerBody.getPosition().x) * INPUT_VELOCITY_SCALER, (touchPoint.y - playerBody.getPosition().y) * INPUT_VELOCITY_SCALER);
            }
        }
    }


    @Override
    public void update(float dt) {
        handleInput();
        int i;
        if((i = checkPlatforms(playerBody)) != 0) {
            generatePlatform(i);
        }
        if(playerBody.getPosition().y < -2){
            playerBody.setTransform(playerBody.getPosition().x, 220/PPM, 0);
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
