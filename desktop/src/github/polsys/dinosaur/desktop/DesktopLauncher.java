package github.polsys.dinosaur.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import github.polsys.dinosaur.DinosaurGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.width = 1280;
		config.height = 720;
		config.title = "Dinosaur Hoverboard";
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
        config.vSyncEnabled = true;
		new LwjglApplication(new DinosaurGame(), config);
	}
}
