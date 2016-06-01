package ru.patrushevoleg.isaac.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.patrushevoleg.isaac.Entities.Isaac;
import ru.patrushevoleg.isaac.MyGame;
import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;
import ru.patrushevoleg.isaac.UserInterface.TouchPad;

public class PlayState extends Screen {

    public static final float TILEMAP_SCALE = 2.75f;
    private static final Vector2 SCREEN_CENTER = new Vector2(MyGame.V_WIDTH / 2, MyGame.V_HEIGHT / 2);
    private Texture background = resources.getTexture(ResourceManager.backgoundTexture);

    private OrthogonalTiledMapRenderer renderer;
    private TouchPad joystick;
    private TiledMap map;

    private MapObjects solidObjects;
    private MapObject playerBox;
    private ShapeRenderer shapeRenderer;

    private int level = 1;

    private Isaac player;

    public PlayState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);
        camera.position.set(MyGame.V_WIDTH, MyGame.V_HEIGHT, 0);
        camera.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        joystick = new TouchPad();

        shapeRenderer = new ShapeRenderer();

        switch (level){
            case 1:
                map = new TmxMapLoader().load("levels/level1.tmx");
                break;
        }

        renderer = new OrthogonalTiledMapRenderer(map, TILEMAP_SCALE);

        solidObjects = map.getLayers().get("solid").getObjects();
        playerBox = map.getLayers().get("player").getObjects().get(0);


        player = new Isaac(resources.getTexture(ResourceManager.isaacTexture), new Vector2(((RectangleMapObject)playerBox).getRectangle().getX() * TILEMAP_SCALE,
                ((RectangleMapObject)playerBox).getRectangle().getY() * TILEMAP_SCALE));
    }

    @Override
    public void inputHandler() {

    }

    @Override
    public void update(float dt) {
        camera.update();
        renderer.setView(camera);
        player.update(dt, joystick.getKnobPercent(), solidObjects);

    }

    public void debugRender(){

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (MapObject o: solidObjects){
            if (o instanceof RectangleMapObject){
                Rectangle rect = ((RectangleMapObject) o).getRectangle();
                shapeRenderer.rect(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE);
            }
        }

        Rectangle rectP =  player.getRectangle();
        shapeRenderer.rect(rectP.getX(), rectP.getY(), rectP.getWidth(), rectP.getHeight());

        shapeRenderer.end();
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        renderer.render();

        batch.begin();
        player.render(batch);
        batch.end();

        debugRender();

        joystick.render(batch);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        resources.reload();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}