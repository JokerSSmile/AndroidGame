package ru.patrushevoleg.isaac.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;
import ru.patrushevoleg.isaac.Screens.PlayState;

public class Bullet extends Entity{

    public static final Vector2 BULLET_SIZE = new Vector2(32, 32);
    public static final int RECTANGLE_SHIFT = 5;
    public static final float RECTANGLE_SCALE = 0.7f;
    public static final float TILEMAP_SCALE = PlayState.TILEMAP_SCALE;
    private static final float TIME_BETWEEN_FRAMES = 0.02f;

    private float stateTime;

    private Texture destroyTexture;

    private Animation animation;
    private Animation flyAnimation;
    private Animation destroyAnimation;

    public Bullet(ResourceManager manager, Vector2 startPos, Vector2 velocity){

        this.position = startPos;

        stateTime = 0;

        float angle = (float) (Math.atan2(velocity.y, velocity.x));
        this.velocity = new Vector2(MathUtils.cos(angle) * 2, MathUtils.sin(angle) * 2);

        this.texture = manager.getTexture(ResourceManager.bulletTexture);
        this.destroyTexture = manager.getTexture(ResourceManager.bulletDestroyTexture);
        size = BULLET_SIZE;

        rectangle = new Rectangle(position.x + RECTANGLE_SHIFT, position.y + RECTANGLE_SHIFT,
                size.x * RECTANGLE_SCALE, size.y * RECTANGLE_SCALE);

        createAnimations();
        animation = flyAnimation;
    }

    private void createAnimations(){
        TextureRegion[][] bullet = TextureRegion.split(texture, 32, 32);
        TextureRegion[] destroy1d = ru.patrushevoleg.isaac.ResourcesStorage.Animation.getFramesArray1D(destroyTexture, 4, 4);

        flyAnimation = new Animation(TIME_BETWEEN_FRAMES, bullet[0][0]);
        destroyAnimation = new Animation(TIME_BETWEEN_FRAMES, destroy1d);
    }

    @Override
    public void render(SpriteBatch batch){
        if (liveState == aliveState.ALIVE) {
            batch.draw(animation.getKeyFrame(stateTime, true), position.x, position.y, 32, 32);
        }
        else{
            batch.draw(animation.getKeyFrame(stateTime, true), position.x - 16, position.y - 16);
        }
    }

    @Override
    public Rectangle getRectangle(){
        return rectangle;
    }

    public void onDestroy(){
        liveState = aliveState.DYING;
        animation = destroyAnimation;
        stateTime = 0;
    }

    @Override
    public void update(float dt, MapObjects solid) {
        stateTime += dt;

        if (liveState == aliveState.ALIVE) {
            position.x += velocity.x * 5;
            position.y += velocity.y * 5;
        }

        rectangle.setPosition(position.x + RECTANGLE_SHIFT, position.y + RECTANGLE_SHIFT);

        for (MapObject wall: solid){
            if (wall instanceof RectangleMapObject && liveState == aliveState.ALIVE){
                Rectangle rect = ((RectangleMapObject) wall).getRectangle();
                if (getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                    onDestroy();
                }
            }
        }

        if (liveState == aliveState.DYING){
            if (destroyAnimation.isAnimationFinished(stateTime)){
                liveState = aliveState.DEAD;
            }
        }



    }

    public void dispose(){

        texture.dispose();
        rectangle = null;
        destroyTexture.dispose();
    }
}
