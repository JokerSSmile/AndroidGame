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

import ru.patrushevoleg.isaac.Screens.PlayState;

public class Fly extends Entity {

    private static final float TIME_BETWEEN_FRAMES = 0.1f;
    private static final Vector2 BODY_SIZE = new Vector2(17, 14);
    private static final float TILEMAP_SCALE = PlayState.TILEMAP_SCALE;

    private float stateTime;

    private Animation flyAnimation;

    public Fly(Texture flyTexture, Vector2 startPosition){

        this.position = startPosition;
        this.texture = flyTexture;
        velocity = new Vector2(0.3f, 0.3f);
        stateTime = 0;
        size = BODY_SIZE;
        damage = 1;
        health = 1;

        rectangle = new Rectangle(position.x, position.y, size.x * 1.75f, size.y * 1.25f);

        createAnimations();
    }

    private void createAnimations(){
        TextureRegion[][] splited = TextureRegion.split(texture, 17, 14);
        flyAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[0][0], splited[0][1]);
    }

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

        checkCollision(collidable);
        rectangle.setPosition(position);

        stateTime += dt;
        position.x += velocity.x;

    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(flyAnimation.getKeyFrame(stateTime, true), rectangle.getX() - 2, rectangle.getY(), BODY_SIZE.x * 2, BODY_SIZE.y * 2);
    }
}
