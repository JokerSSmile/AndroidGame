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
import com.badlogic.gdx.math.Vector3;

import ru.patrushevoleg.isaac.Entities.Bullet;
import ru.patrushevoleg.isaac.Entities.Enemy;
import ru.patrushevoleg.isaac.Entities.Entity;
import ru.patrushevoleg.isaac.Entities.Fly;
import ru.patrushevoleg.isaac.Entities.Isaac;
import ru.patrushevoleg.isaac.Entities.Worm;
import ru.patrushevoleg.isaac.Handlers.ButtonHandler;
import ru.patrushevoleg.isaac.MyGame;
import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;
import ru.patrushevoleg.isaac.UserInterface.TouchPad;
import ru.patrushevoleg.isaac.UserInterface.UiHearts;

public class PlayState extends Screen {

    public static final float TILEMAP_SCALE = 2.75f;

    private TouchPad joystick;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;

    private MapObjects rooms;
    private MapObjects doors;
    private MapObjects solidObjects;
    private MapObject playerBox;
    private ShapeRenderer shapeRenderer;

    private int level = 1;
    private int room = 1;
    private Rectangle manholeRect;

    private boolean isRoomCleared;

    private Isaac player;
    private UiHearts uiHealthBar;
    private ButtonHandler pauseButton;

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

        rooms = map.getLayers().get("rooms").getObjects();
        doors = map.getLayers().get("doors").getObjects();
        solidObjects = map.getLayers().get("solid").getObjects();
        playerBox = map.getLayers().get("player").getObjects().get(0);
        MapObject manhole = map.getLayers().get("manhole").getObjects().get(0);

        uiHealthBar = new UiHearts(resources.getTexture(ResourceManager.uiHeartTexture));
        manholeRect = new Rectangle(((RectangleMapObject) manhole).getRectangle());
        manholeRect.setPosition(manholeRect.getX() * TILEMAP_SCALE, manholeRect.getY() * TILEMAP_SCALE);
        manholeRect.setSize(manholeRect.getWidth() * TILEMAP_SCALE, manholeRect.getHeight() * TILEMAP_SCALE);

        pauseButton = new ButtonHandler(resources.getTexture(ResourceManager.pauseBtnTexture),
                new Vector2(viewport.getScreenWidth() / 2 + 100, viewport.getScreenHeight() / 2 - 30),
                new Vector2(55, 55));

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
            int enemyRoom = Integer.decode(f.getProperties().get("room", String.class));
            enemies.add(new Fly(resources, new Vector2(((RectangleMapObject)f).getRectangle().getX() * TILEMAP_SCALE,
                ((RectangleMapObject)f).getRectangle().getY() * TILEMAP_SCALE), enemyRoom));
        }

        MapObjects wormsOnMap;
        wormsOnMap =  map.getLayers().get("worms").getObjects();
        for (MapObject worm: wormsOnMap){
            int enemyRoom = Integer.decode(worm.getProperties().get("room", String.class));
            enemies.add(new Worm(resources, new Vector2(((RectangleMapObject)worm).getRectangle().getX() * TILEMAP_SCALE,
                    ((RectangleMapObject)worm).getRectangle().getY() * TILEMAP_SCALE), enemyRoom));
        }
    }

    @Override
    public void inputHandler() {

        Vector2 mousePos = new Vector2(Gdx.input.getX(), MyGame.V_HEIGHT - Gdx.input.getY());

        if (pauseButton.getRectangle().contains(mousePos.x, mousePos.y) && Gdx.input.isTouched()){
            pauseButton.onClick();
        }
        else{
            pauseButton.notTouched();
        }

        if (pauseButton.isOnRelease()){
            gsm.push(new PauseState(gsm, resources));
        }
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

        if (player.getRectangle().overlaps(manholeRect)){
            level++;
            onNewLevel();
        }

    }

    private void calculateRoom(){
        for (MapObject r : rooms){
            Rectangle rect = ((RectangleMapObject) r).getRectangle();
            if (player.getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE,
                    rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                room = Integer.parseInt(r.getName());
            }
        }
    }

    private void checkForCleaningRoom(){
        boolean isCleared = true;
        for (Enemy enemy : enemies){
            if (enemy.room == room){
                isCleared = false;
            }
        }

        isRoomCleared = isCleared;
    }

    @Override
    public void update(float dt) {

        inputHandler();
        camera.update();
        renderer.setView(camera);
        calculateRoom();

        player.setVelocity(joystick.getKnobPercentWalk());
        player.update(dt, solidObjects);
        player.collisionWithDoors(doors, camera, isRoomCleared);
        player.shoot(bullets, joystick.getKnobPercentShoot(), resources.getTexture(ResourceManager.bulletTexture));
        uiHealthBar.update(dt, player.getHealth());
        checkForCleaningRoom();

        for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();){
            Enemy entity = it.next();
            entity.update(dt, solidObjects);
            if (entity.liveState == Entity.aliveState.DEAD){
                it.remove();
            }
        }

        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();){
            Bullet bullet = it.next();
            bullet.update(dt, solidObjects);
            if (bullet.liveState == Entity.aliveState.DEAD){
                it.remove();
            }
        }

        checkIntersectionsOfEntities();
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

        shapeRenderer.rect(pauseButton.getRectangle().getX(), pauseButton.getRectangle().getY(),
                pauseButton.getRectangle().getWidth(), pauseButton.getRectangle().getHeight());

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

        pauseButton.render(batch);
        batch.end();
        joystick.render(batch);

        debugRender();



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