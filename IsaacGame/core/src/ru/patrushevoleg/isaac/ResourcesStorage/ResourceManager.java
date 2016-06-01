package ru.patrushevoleg.isaac.ResourcesStorage;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TideMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import ru.patrushevoleg.isaac.Entities.Isaac;

public class ResourceManager {

    private static final int playerBodyFrameWidth = (int)Isaac.BODY_SIZE.x;
    private static final int playerBodyFrameHeight = playerBodyFrameWidth;


    private static final String imgPath = "images/";
    private static final String soundPath = "sounds/";
    private static final String musicPath = "music/";
    private static final String fontsPath = "fonts/";
    private static final String levelsPath = "levels/";

    public static final String isaacTexture = imgPath + "body1.png";
    public static final String backgoundTexture = imgPath + "background.png";
    public static final String menuBackgroundTexture = imgPath + "menu_background.png";
    public static final String uiHeartTexture = imgPath + "ui_hearts.png";
    public static final String tearDestroyEnemyTexture = imgPath + "tear_destroy_enemy.png";
    public static final String tearDestroyPlayerTexture = imgPath + "tear_destroy_player.png";
    public static final String loadingTexture = imgPath + "loading.png";
    public static final String mapLevel1 = levelsPath + "level1.tmx";

    public static TiledMap level1;

    private static AssetManager manager;

    public void load(){

        manager = new AssetManager();
        loadMaps();
        loadTextures();

        while(!manager.update())
        {
            System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }
    }

    private void loadTextures(){

        manager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        manager.load(isaacTexture, Texture.class);
        manager.load(backgoundTexture, Texture.class);
        manager.load(menuBackgroundTexture, Texture.class);
        manager.load(uiHeartTexture, Texture.class);
        manager.load(tearDestroyEnemyTexture, Texture.class);
        manager.load(tearDestroyPlayerTexture, Texture.class);
        manager.load(loadingTexture, Texture.class);
    }

    private void loadMaps(){
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(mapLevel1, TiledMap.class);
    }


    public boolean isLoaded(){
        return (manager.getProgress() >= 1);
    }

    public void reload(){
        manager.finishLoading();
    }

    public Texture getTexture(String name){
        return manager.get(name);
    }

    public TiledMap getLevel(String name){
        return manager.get(name);
    }

    public void dispose(){
        manager.dispose();
        manager = null;
    }
}
