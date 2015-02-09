package verhelst.handlers;

import com.badlogic.gdx.Game;

import java.util.Stack;

import verhelst.prpg.PhysRPG;
import verhelst.states.GameState;
import verhelst.states.PlayGS;

/**
 * Created by Orion on 2/7/2015.
 */
public class GameStateManager {

    private Stack<GameState> gameStates;
    private PhysRPG game;

    public static final int PLAY = 1;

    public GameStateManager(PhysRPG game){
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public void update(float dt){
        gameStates.peek().update(dt);
    }

    public void render(float dt){
        gameStates.peek().render(dt);
    }

    public PhysRPG game(){
        return game;
    }

    private GameState getState(int state){
        if(state == PLAY) return new PlayGS(this);
        return null;
    }

    public void setState(int state){
        popState();
        pushState(state);
    }

    public void pushState(int state){
        gameStates.push(getState(state));
    }

    public void popState(){
        gameStates.pop().dispose();
    }
}
