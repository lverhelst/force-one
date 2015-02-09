package verhelst.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Orion on 2/8/2015.
 */
public class MyInputAdapter extends InputAdapter {

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        MyInput.setTouch(screenX, screenY);
        return false;
    }
}
