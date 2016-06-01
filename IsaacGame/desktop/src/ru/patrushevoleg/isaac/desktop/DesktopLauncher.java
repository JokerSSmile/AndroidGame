package ru.patrushevoleg.isaac.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.patrushevoleg.isaac.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyGame.V_WIDTH;
		config.height = MyGame.V_HEIGHT;
		new LwjglApplication(new MyGame(), config);
	}
}
