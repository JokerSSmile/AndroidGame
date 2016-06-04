package ru.patrushevoleg.isaac.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;
import java.util.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.patrushevoleg.isaac.Entities.Bullet;
import ru.patrushevoleg.isaac.Entities.Enemy;
import ru.patrushevoleg.isaac.Entities.Entity;
import ru.patrushevoleg.isaac.Entities.Fly;
import ru.patrushevoleg.isaac.Entities.Isaac;
import ru.patrushevoleg.isaac.Entities.Worm;
import ru.patrushevoleg.isaac.MyGame;
import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;
import ru.patrushevoleg.isaac.UserInterface.TouchPad;
import ru.patrushevoleg.isaac.UserInterface.UiHearts;

public class PlayState extends Screen {

    public static final float TILEMAP_SCALE = 2.75f;

    private TouchPad joystick;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;

    private MapObjects doors;
    private MapObjects solidObjects;
    private MapObject playerBox;
    private ShapeRenderer shapeRenderer;

    private int level = 1;

    private boolean isRoomCleared;

    private Isaac player;
    private UiHearts uiHealthBar;

    private Vector<Enemy> enemies;
    private Vector<Bullet> bullets;

    public PlayState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);

        onNewLevel();

        player = new Isaac(resources, new Vector2(((RectangleMapObject) playerBox).getRectangle().getX() * TILEMAP_SCALE,
                ((RectangleMapObject) playerBox).getRectangle().getY() * TILEMAP_SCALE));
    }

    private void initializeData(){

        camera.position.set(MyGame.V_WIDTH, MyGame.V_HEIGHT, 0);
        camera.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        joystick = new TouchPad(camera);
        bullets = new java.util.Vector<Bullet>();
        enemies = new java.util.Vector<Enemy>();
        shapeRenderer = new ShapeRenderer();
        renderer = new OrthogonalTiledMapRenderer(map, TILEMAP_SCALE);
        doors = map.getLayers().get("doors").getObjects();
        solidObjects = map.getLayers().get("solid").getObjects();
        playerBox = map.getLayers().get("player").getObjects().get(0);
        uiHealthBar = new UiHearts(resources.getTexture(ResourceManager.uiHeartTexture));

        if (level > 1) {
            player.initPosition(new Vector2(((RectangleMapObject) playerBox).getRectangle().getX() * TILEMAP_SCALE,
                    ((RectangleMapObject) playerBox).getRectangle().getY() * TILEMAP_SCALE));
        }
    }

    private void onNewLevel(){
        switch (level){
            case 1:
                map = new TmxMapLoader().load("levels/level1.tmx");
                break;
            case 2:
                map = new TmxMapLoader().load("levels/level2.tmx");
                break;
        }

        initializeData();
        initializeEnemies();
    }


    private void initializeEnemies(){

        MapObjects flyOnMap;
        flyOnMap =  map.getLayers().get("fly").getObjects();
        for (MapObject f: flyOnMap){
            enemies.add(new Fly(resources, new Vector2(((RectangleMapObject)f).getRectangle().getX() * TILEMAP_SCALE,
                    ((RectangleMapObject)f).getRectangle().getY() * TILEMAP_SCALE)));
        }

        MapObjects wormsOnMap;
        wormsOnMap =  map.getLayers().get("worms").getObjects();
        for (MapObject worm: wormsOnMap){
            enemies.add(new Worm(resources, new Vector2(((RectangleMapObject)worm).getRectangle().getX() * TILEMAP_SCALE,
                    ((RectangleMapObject)worm).getRectangle().getY() * TILEMAP_SCALE)));
        }
    }

    @Override
    public void inputHandler() {

    }

    private void checkIntersectionsOfEntities(){

        for (Enemy entity: enemies){
            if (entity.getRectangle().overlaps(player.getRectangle())){
                player.getDamage(entity.damage);
            }

            for (Bullet bullet : bullets){
                if (entity.getRectangle().overlaps(bullet.getRectangle()) && bullet.liveState == Entity.aliveState.ALIVE){
                    entity.getDamage(player.getCurrentDamage());
                    bullet.onDestroy();
                }
            }
        }

    }

    @Override
    public void update(float dt) {
        camera.update();
        renderer.setView(camera);
        player.setVelocity(joystick.getKnobPercentWalk());
        player.update(dt, solidObjects);
        player.collisionWithDoors(doors, camera);
        player.shoot(bullets, joystick.getKnobPercentShoot(), resources.getTexture(ResourceManager.bulletTexture));
        uiHealthBar.update(dt, player.getHealth());

        for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();){
            Enemy entity = it.next();
            entity.update(dt, solidObjects);
            if (entity.liveState == Entity.aliveState.DEAD){
                //entity.dispose();
                it.remove();
            }
        }

        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();){
            Bullet bullet = it.next();
            bullet.update(dt, solidObjects);
            if (bullet.liveState == Entity.aliveState.DEAD){
                //bullet.dispose();
                it.remove();
            }
        }

        checkIntersectionsOfEntities();

        isRoomCleared = true;


    }

    public void debugRender(){

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (MapObject o: solidObjects){
            if (o instanceof RectangleMapObject){
                Rectangle rect = ((RectangleMapObject) o).getRectangle();
                shapeRenderer.rect(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE);
            }
        }

        Rectangle rectP =  player.getRectangle();
        shapeRenderer.rect(rectP.getX(), rectP.getY(), rectP.getWidth(), rectP.getHeight());

        for (Enemy entity : enemies){
            Rectangle rectF =  entity.getRectangle();
            shapeRenderer.rect(rectF.getX(), rectF.getY(), rectF.getWidth(), rectF.getHeight());
        }

        for (Bullet bullet: bullets) {
            Rectangle rectF =  bullet.getRectangle();
            shapeRenderer.rect(rectF.getX(), rectF.getY(), rectF.getWidth(), rectF.getHeight());
        }

        shapeRenderer.end();
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        int[] backgroundLayers = { 0, 1 };
        renderer.render(backgroundLayers);

        if (isRoomCleared){
            int[] openedDoorsLayer = { 2 };
            renderer.render(openedDoorsLayer);
        }
        else{
            int[] closedDoorsLayer = { 3 };
            renderer.render(closedDoorsLayer);
        }

        batch.begin();
        player.render(batch);

        for (Enemy entity : enemies){
            entity.render(batch);
        }

        for (Bullet bullet: bullets) {
            bullet.render(batch);
        }

        uiHealthBar.render(batch, new Vector2(camera.position.x,
                camera.position.y));

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