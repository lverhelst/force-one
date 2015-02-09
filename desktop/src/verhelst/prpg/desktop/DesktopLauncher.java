package verhelst.prpg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import verhelst.prpg.PhysRPG;

public class DesktopLauncher {


	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = PhysRPG.TITLE;
        config.width = PhysRPG.V_WIDTH * PhysRPG.SCALE;
        config.height = PhysRPG.V_HEIGHT * PhysRPG.SCALE;

		new LwjglApplication(new PhysRPG(), config);
	}
}
