package verhelst.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import verhelst.Misc.Assets;
import verhelst.states.PlayGS;

/**
 * Created by Orion on 2/10/2015.
 */
public class HUD {

    Character player;

    public HUD(Character player){
        this.player = player;
    }

    public void render(SpriteBatch batch){
        Assets.floatingTextFont.draw(batch, PlayGS.playstate.toString(), 0, 20 + Assets.floatingTextFont.getBounds("A").height * 2 );

        Assets.floatingTextFont.draw(batch, player.getName() + " " + player.getCurrentHealth(), 0, 20);
    }




}
