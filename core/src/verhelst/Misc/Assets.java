package verhelst.Misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Orion on 2/10/2015.
 */
public class Assets {

    public static BitmapFont floatingTextFont;
    public static Texture faces;
    public static Texture explosions;

    public static float baseGraphicsDensity = 0.6f;

    public Assets(){
        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(16 * Gdx.graphics.getDensity()/baseGraphicsDensity);
        floatingTextFont = ftfg.generateFont(ftfp);

        faces = new Texture(Gdx.files.internal("player.png"));
        explosions = new Texture(Gdx.files.internal("explosion.png"));
    }
}
