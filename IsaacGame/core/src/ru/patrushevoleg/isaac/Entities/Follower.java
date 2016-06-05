package ru.patrushevoleg.isaac.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;
import ru.patrushevoleg.isaac.Screens.PlayState;

public class Follower extends Enemy{

    private static final float TIME_BETWEEN_FRAMES = 0.1f;
    private static final float BASIC_SPEED = 2;
    private static final float TILEMAP_SCALE = PlayState.TILEMAP_SCALE;

    private Texture headTexture;

    private float stateTime;

    private Animation animation;
    private Animation walkUpDownAnimation;
    private Animation walkRightAnimation;
    private Animation walkLeftAnimation;

    public Follower(ResourceManager resources, Vector2 startPos, int room) {
        this.room = room;
        this.texture = resources.getTexture(ResourceManager.isaacTexture);
        this.position = startPos;
        damage = 15;
        health = 55;
        stateTime = 0;
        rectangle = new Rectangle(startPos.x, startPos.y, 60, 60);
        velocity = new Vector2(0, 0);

        createAnimations();
        animation = walkUpDownAnimation;
    }

    private void createAnimations(){

        TextureRegion[][] splited = TextureRegion.split(texture, 32, 32);
        TextureRegion[][] mirrored = TextureRegion.split(texture, 32, 32);

        for (TextureRegion[] regionRow: mirrored){
            for (TextureRegion regionCol: regionRow) {
                regionCol.flip(true, false);
            }
        }

        walkUpDownAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[0][6], splited[0][7], splited[1][0],
                splited[1][1], splited[1][2], splited[1][3], splited[1][4], splited[1][5],
                splited[1][6], splited[1][7]);

        walkRightAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[2][0], splited[2][1], splited[2][2],
                splited[2][3], splited[2][4], splited[2][5], splited[2][6], splited[2][7],
                splited[3][0], splited[3][1]);

        walkLeftAnimation = new Animation(TIME_BETWEEN_FRAMES, mirrored[2][0], mirrored[2][1], mirrored[2][2],
                mirrored[2][3], mirrored[2][4], mirrored[2][5], mirrored[2][6], mirrored[2][7],
                mirrored[3][0], mirrored[3][1]);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void updateDirection(Vector2 playerPos){
        float diffX = playerPos.x - position.x;
        float diffY = playerPos.y - position.y;

        float angle = (float)Math.atan2(diffY, diffX);
        velocity.x = BASIC_SPEED * (float)Math.cos(angle);
        velocity.y = BASIC_SPEED * (float)Math.cos(angle);
    }

    private void setPosition(){
        position.x += velocity.x;
        position.y += velocity.y;
        rectangle.setPosition(this.position);
    }

    private boolean isCollides(MapObjects collidable){
        for (MapObject wall : collidable) {
            Rectangle rect = ((RectangleMapObject) wall).getRectangle();
            if (getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(float dt, MapObjects collidable) {

        stateTime += dt;
        isAlive = health > 0;

    }

    @Override
    public void dispose() {

    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void getDamage(int dmg) {

    }
}
