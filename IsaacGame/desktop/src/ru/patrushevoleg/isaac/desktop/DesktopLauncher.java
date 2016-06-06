package ru.patrushevoleg.isaac.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.patrushevoleg.isaac.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyGame.V_WIDTH;
		config.height = MyGame.V_HEIGHT;
		config.title = MyGame.TITLE;
		config.addIcon("icon.png", Files.FileType.Internal);
		new LwjglApplication(new MyGame(), config);
	}
}
