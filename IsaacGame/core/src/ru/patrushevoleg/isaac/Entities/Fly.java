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

public class Fly extends Enemy {

    private static final float TIME_BETWEEN_FRAMES_DESTROY = 0.02f;
    private static final float TIME_BETWEEN_FRAMES = 0.1f;
    private static final Vector2 BODY_SIZE = new Vector2(17, 14);
    private static final float TILEMAP_SCALE = PlayState.TILEMAP_SCALE;
    private static final float SPEED = 3;

    private float stateTime;

    private Animation animation;
    private Animation flyAnimation;
    private Animation deathAnimation;

    private Texture destroyTexture;


    public Fly(ResourceManager manager, Vector2 startPosition){

        this.position = startPosition;
        this.texture = manager.getTexture(ResourceManager.flyTexture);
        destroyTexture = manager.getTexture(ResourceManager.enemyDestroyTexture);

        velocity = new Vector2(SPEED, SPEED);
        stateTime = 0;
        size = BODY_SIZE;
        damage = 10;
        health = 10;

        rectangle = new Rectangle(position.x, position.y, size.x * 1.75f, size.y * 1.25f);

        createAnimations();
        animation = flyAnimation;
    }

    private void createAnimations(){
        TextureRegion[][] splited = TextureRegion.split(texture, 17, 14);
        flyAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[0][0], splited[0][1]);


        TextureRegion[] deathFrames = ru.patrushevoleg.isaac.ResourcesStorage.Animation.getFramesArray1D(destroyTexture, 3, 4);
        deathAnimation = new Animation(TIME_BETWEEN_FRAMES_DESTROY, deathFrames);
    }

    @Override
    public Rectangle getRectangle(){
        return rectangle;
    }

    private void checkCollision(MapObjects collidable){
        for (MapObject wall : collidable) {
            Rectangle rect = ((RectangleMapObject) wall).getRectangle();
            if (getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                position.x -= velocity.x * 2;
                velocity.x = -velocity.x;
            }
        }
    }

    public void update(float dt, MapObjects collidable){


        isAlive = health > 0;

        checkCollision(collidable);
        rectangle.setPosition(position);

        stateTime += dt;

        if (liveState == aliveState.ALIVE) {
            position.x += velocity.x;
        }

        if (!isAlive && liveState == aliveState.ALIVE){
            animation = deathAnimation;
            liveState = aliveState.DYING;
            stateTime = 0;
        }


        if (liveState == aliveState.DYING){
            if (deathAnimation.isAnimationFinished(stateTime)){
                liveState = aliveState.DEAD;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch){

        if (liveState == aliveState.ALIVE) {
            batch.draw(animation.getKeyFrame(stateTime, true), rectangle.getX() - 2, rectangle.getY(), BODY_SIZE.x * 2, BODY_SIZE.y * 2);
        }
        else {
            batch.draw(animation.getKeyFrame(stateTime, true), rectangle.getX() - 50, rectangle.getY() - 40);
        }
    }

    @Override
    public void getDamage(int dmg){
            health -= dmg;
    }

    @Override
    public void dispose() {

    }
}
