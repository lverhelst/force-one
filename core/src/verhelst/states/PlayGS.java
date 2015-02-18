package verhelst.states;

import static verhelst.handlers.B2DVars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import verhelst.GameObjects.Breed;
import verhelst.GameObjects.Equipment;
import verhelst.GameObjects.FloatingText;
import verhelst.GameObjects.HUD;
import verhelst.GameObjects.Inventory;
import verhelst.GameObjects.Character;
import verhelst.Misc.Assets;
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

    public enum PLAYSTATE {
        PLAY,
        PAUSE,
        GAMEOVER
    }


    private  World world;
    private Box2DDebugRenderer bdr;

    private OrthographicCamera b2dCam;

    private Body playerBody;
    private Character player;

    //for translating screen coordinates
    private Vector3 touchPoint = new Vector3();
    private int[] xy;

    //input velocity scaler
    private final int INPUT_VELOCITY_SCALER = 5;

    private LinkedList<Rectangle> rectangles;
    private LinkedList<Body> bodies;
    private LinkedList<Body[]> blocksLists;
    private LinkedList<Body[]> baddyLists;
    private static LinkedList<FloatingText> floatTexts;
    private static LinkedList<FloatingText> HUDTexts;
    Iterator<FloatingText> it;

    private HUD hud;

    private Random rng = new Random();

    public static int jumps = 2;

    public static PLAYSTATE playstate;

    public PlayGS(GameStateManager gsm){
        super(gsm);

        rectangles = new LinkedList<Rectangle>();
        bodies = new LinkedList<Body>();
        blocksLists = new LinkedList<Body[]>();
        baddyLists = new LinkedList<Body[]>();
        floatTexts = new LinkedList<FloatingText>();
        HUDTexts = new LinkedList<FloatingText>();

        world = new World(new Vector2(0f,-9.81f), true);
        world.setContactListener(new MyContactListener());
        world.setVelocityThreshold(0.0f);

        bdr = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();

        //create falling box
        bodyDef.position.set(160/PPM,120/PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearVelocity.set(3f, 0f);
        playerBody = world.createBody(bodyDef);


        Breed breed = new Breed("Player", 100, 5);
        player = new Character(playerBody);
        player.setBreed(breed);
        playerBody.setUserData(player);

        CircleShape box = new CircleShape();
        box.setRadius(10f/PPM);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = box;
        fixDef.friction = 0f;
        fixDef.filter.categoryBits = B2DVars.PLAYER_BIT;
        fixDef.filter.maskBits = B2DVars.BLOCK_BIT | B2DVars.PLATFORM_BIT | B2DVars.ENEMY_BIT;
        playerBody.createFixture(fixDef);

        //create side sensor
        PolygonShape pbox = new PolygonShape();
        pbox.setAsBox(10/PPM, 5/PPM);

        fixDef.shape = pbox;
        fixDef.isSensor = true;
        Fixture f = playerBody.createFixture(fixDef);
        f.setUserData("B_SENSOR");


        //box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, PhysRPG.V_WIDTH /PPM, PhysRPG.V_HEIGHT /PPM);


        hud = new HUD(player);

        playstate = PLAYSTATE.PLAY;
        Gdx.input.setInputProcessor(new MyInputAdapter());

    }

    private void spawnExplosion(){

    }

    private void restart(){
        while(rectangles.size() > 0) {
            rectangles.removeLast();
            world.destroyBody(bodies.removeLast());
            if(blocksLists.size() > 0)
              for(Body b : blocksLists.removeLast()){
                 if(b != null) world.destroyBody(b);
              }
            if(baddyLists.size() > 0)
                for(Body b : baddyLists.removeLast()){
                    if(b != null) world.destroyBody(b);
                }
        }
        player.reset();
        playerBody.setTransform(playerBody.getPosition().x, 220 / PPM, 0);
    }


    private int checkPlatforms(Body body){
        if(rectangles.size() < 1)
            return 1;

        if(body.getPosition().x < rectangles.getFirst().getX() + rectangles.getFirst().getWidth()/2)
            return -1;
        if(body.getPosition().x > rectangles.getLast().getX() + rectangles.getLast().getWidth()/2)
            return 1;
        return 0;
    }



    private void generatePlatform(int side) {
        float xLocation, yLocation;
        if (rectangles.size() < 1){
            xLocation = playerBody.getPosition().x;
            yLocation = 100 / PPM;
        }else {
            xLocation = (side == -1 ? rectangles.getFirst() : rectangles.getLast()).getX() + ((side == -1? side : 3 * side) * 160 / PPM);

            yLocation = (100 + (rng.nextInt(6) * 20))/PPM;
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
           // blocksLists.addFirst(generateBlocks(rectangles.getFirst()));

            baddyLists.addFirst(generateBaddies(rectangles.getFirst()));

            if(rectangles.size() > 5) {
                rectangles.removeLast();
                world.destroyBody(bodies.removeLast());
              ///  for(Body b : blocksLists.removeLast()){
               //     if(b != null) world.destroyBody(b);
              //  }
                if(baddyLists.size() > 0)
                    for(Body b : baddyLists.removeLast()){
                        if(b != null) world.destroyBody(b);
                    }
            }
        }
        else {
            rectangles.add(new Rectangle(xLocation - 160 / PPM, yLocation - 5 / PPM, 320 / PPM, 5 / PPM));
            bodies.add(body);

            // blocksLists.add(generateBlocks(rectangles.getLast()));

            baddyLists.add(generateBaddies(rectangles.getLast()));

            if(rectangles.size() > 5) {
                rectangles.removeFirst();
                world.destroyBody(bodies.removeFirst());

             //   for(Body b : blocksLists.removeFirst()){
             //       if(b != null) world.destroyBody(b);
             //   }
                if(baddyLists.size() > 0)
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
        if(rng.nextBoolean())
            numb_blocks = 0;

        System.out.println("Generate " + numb_blocks +  " Baddies");

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
        Breed breed = new Breed("Enemy", 1, 5);

        for(int i = 0; i < numb_blocks; i++){

                //create a box
                bodyDef.position.set(xLocation + (137/PPM) * i + 1,yLocation + (10/PPM));
                body = world.createBody(bodyDef);
                Character c = new Character(body);
                c.setBreed(breed);
                body.setUserData(c);
                body.createFixture(fixDef);
                bd[i] = body;

        }
        return bd;
    }

    public static void addFloatingText(Body body, String str){
        floatTexts.add(new FloatingText(str, body.getPosition().x * PPM, body.getPosition().y * PPM));
    }

    public static void addHUDText(String str){
        HUDTexts.add(new FloatingText(str, Assets.floatingTextFont.getBounds("PLAYER: 100").width, 20));
    }


    private void updateBaddies(float dt){
        for(Body[] b : baddyLists){
            if(b != null){
                for(int i = 0; i < b.length; i++){
                    if(b[i] != null){
                        if((b[i].getUserData() instanceof verhelst.GameObjects.Character)){
                            ((verhelst.GameObjects.Character)(b[i].getUserData())).update(dt);
                        }
                    }
                }
            }
        }
    }

    private void renderBaddies(Batch batch){
        for(Body[] b : baddyLists){
            if(b != null){
                for(int i = 0; i < b.length; i++){
                    if(b[i] != null){
                        if((b[i].getUserData() instanceof verhelst.GameObjects.Character)){
                            ((verhelst.GameObjects.Character)(b[i].getUserData())).render(batch);
                        }
                    }
                }
            }
        }
    }

    private void sweepBaddies(){
        for(Body[] b : baddyLists){
            if(b != null){
                for(int i = 0; i < b.length; i++){
                    if(b[i] != null){
                        if((b[i].getUserData() instanceof verhelst.GameObjects.Character)){
                            if(((verhelst.GameObjects.Character)(b[i].getUserData())).isRemoveable()){
                                //drop loot?
                                if(rng.nextBoolean()){
                                    Inventory.addItem(Equipment.generateRandomEquipment());
                                }else{
                                    //add crafting mat?
                                }
                                world.destroyBody(b[i]);
                                b[i] = null;
                            }
                        }
                    }
                }
            }
        }
    }

    private void sweepFloatingTexts(){
        for (it = floatTexts.iterator(); it.hasNext(); ) {
            FloatingText ft = it.next();
            if (ft.isRemoveable()) {
                it.remove();
            }
        }
        for (it = HUDTexts.iterator(); it.hasNext(); ) {
            FloatingText ft = it.next();
            if (ft.isRemoveable()) {
                it.remove();
            }
        }

    }


    @Override
    public void handleInput() {
        if(MyInput.hasTouch()) {
            xy = MyInput.consumeXY();
            switch(playstate) {
                case PLAY:
                    if (jumps > 0) {
                        jumps = Math.max(0, --jumps);
                        b2dCam.unproject(touchPoint.set(xy[0], xy[1], 0));

                        float xVec = (touchPoint.x - playerBody.getPosition().x);
                        if (xVec < 0f) {
                            xVec = Math.min(-1f, xVec);
                        } else {
                            xVec = Math.max(1f, xVec);
                        }


                        playerBody.setLinearVelocity(Math.min(xVec * INPUT_VELOCITY_SCALER, 3f), playerBody.getLinearVelocity().y);
                        playerBody.applyForceToCenter(0, 250f, false);
                    }
                    break;
                case PAUSE:
                    break;
                case GAMEOVER:
                    restart();
                    playstate = PLAYSTATE.PLAY;
                    break;

            }
        }
    }


    @Override
    public void update(float dt) {
        handleInput();

        player.update(dt);
        updateBaddies(dt);
        for (FloatingText t : floatTexts) {
            t.update(false);
        }
        for (FloatingText t : HUDTexts) {
            t.update(false);
        }

        switch(playstate) {
            case PLAY:
                int i;
                if ((i = checkPlatforms(playerBody)) != 0) {
                    generatePlatform(i);
                }
                if (playerBody.getLinearVelocity().x < 3f) {
                    playerBody.applyForceToCenter(5, 0, false);
                }
                if (playerBody.getPosition().y < -2 || player.isDead()) {
                    //Game Over Man
                    player.explode();
                    playstate = PLAYSTATE.GAMEOVER;
                }
                world.step(dt, 6, 2);


                break;
            case PAUSE:
                break;
            case GAMEOVER:

                break;
        }
        sweepBaddies();
        sweepFloatingTexts();
    }

    @Override
    public void render(float dt) {
        //camera follow box
        b2dCam.position.set(playerBody.getPosition().x, playerBody.getPosition().y, 0);
        b2dCam.update();

        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.position.set(playerBody.getPosition().x * PPM, playerBody.getPosition().y * PPM,0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        player.render(batch);

        renderBaddies(batch);

        for (FloatingText f : floatTexts){
            f.render(batch);
        }


        batch.setProjectionMatrix(hudcam.combined);
        hud.render(batch);
        for (FloatingText f : HUDTexts){
            f.render(batch);
        }
        batch.end();

        bdr.render(world, b2dCam.combined);

    }

    @Override
    public void dispose() {

    }
}
