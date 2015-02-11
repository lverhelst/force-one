package verhelst.Misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Orion on 2/10/2015.
 */
public class Assets {

    public static BitmapFont floatingTextFont;

    public Assets(){
        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(8 * Gdx.graphics.getDensity());
        floatingTextFont = ftfg.generateFont(ftfp);


    }
}
