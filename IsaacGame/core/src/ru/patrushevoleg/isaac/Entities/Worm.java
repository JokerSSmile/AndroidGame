package ru.patrushevoleg.isaac.Entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;

public class Worm extends Enemy{

    private static final float CHANGE_DIR_TIME = 3;
    private static final Vector2 BODY_SIZE = new Vector2(64, 64);
    private static final float TIME_BETWEEN_FRAMES = 0.15f;
    private static final float BASIC_SPEED = 2;
    private static final float ANGRY_SPEED = 5;

    private float stateTime;
    private float lastChangeDirTime;

    private boolean isAngry;

    private Animation animation;
    private Animation walkUp;
    private Animation walkDown;
    private Animation walkLeft;
    private Animation walkRight;
    private Animation attackUp;
    private Animation attackDown;
    private Animation attackLeft;
    private Animation attackRight;

    moveState wormMoveState;

    public Worm(ResourceManager manager, Vector2 startPos){
        this.position = startPos;
        this.texture = manager.getTexture(ResourceManager.wormTexture);
        stateTime = 0;
        lastChangeDirTime = 0;
        isAngry = false;

        rectangle = new Rectangle(position.x, position.y, BODY_SIZE.x, BODY_SIZE.y);

        wormMoveState = moveState.values()[(int)(Math.random()*4 + 1)];

        createAnimations();
        setAnimation();
    }

    private void createAnimations(){
        TextureRegion[][] frames = TextureRegion.split(texture, 64, 64);

        walkUp = new Animation(TIME_BETWEEN_FRAMES, frames[1][0], frames[1][1], frames[1][2], frames[1][3]);
        walkDown = new Animation(TIME_BETWEEN_FRAMES, frames[2][0], frames[2][1], frames[2][2], frames[2][3]);
        walkLeft = new Animation(TIME_BETWEEN_FRAMES, frames[4][0], frames[4][1], frames[4][2], frames[4][3]);
        walkRight = new Animation(TIME_BETWEEN_FRAMES, frames[0][0], frames[0][1], frames[0][2], frames[0][3]);
        attackUp = new Animation(TIME_BETWEEN_FRAMES, frames[3][3]);
        attackDown = new Animation(TIME_BETWEEN_FRAMES, frames[3][0]);
        attackLeft = new Animation(TIME_BETWEEN_FRAMES, frames[3][2]);
        attackRight = new Animation(TIME_BETWEEN_FRAMES, frames[3][1]);
    }

    private void setAnimation(){

        switch (wormMoveState){
            case UP:
                animation = isAngry ? attackUp : walkUp;
                break;
            case DOWN:;
                animation = isAngry ? attackDown : walkDown;
                break;
            case RIGHT:
                animation = isAngry ? attackRight : walkRight;
                break;
            case LEFT:
                animation = isAngry ? attackLeft : walkLeft;
                break;
        }
    }

    @Override
    public void getDamage(int dmg) {
        health -= dmg;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime, true), position.x, position.y);
    }

    @Override
    public void update(float dt, MapObjects collidable) {

        stateTime += dt;

        if (stateTime > lastChangeDirTime + CHANGE_DIR_TIME){
            wormMoveState = moveState.values()[(int)(Math.random()*4 + 1)];
            lastChangeDirTime = stateTime;

            setAnimation();
        }

        switch (wormMoveState){
            case UP:
                position.y += isAngry ? ANGRY_SPEED : BASIC_SPEED;
                break;
            case DOWN:
                position.y -= isAngry ? ANGRY_SPEED : BASIC_SPEED;
                break;
            case RIGHT:
                position.x += isAngry ? ANGRY_SPEED : BASIC_SPEED;
                break;
            case LEFT:
                position.x -= isAngry ? ANGRY_SPEED : BASIC_SPEED;
                break;
        }

        rectangle.setPosition(position.x, position.y);

    }

    @Override
    public void dispose() {

    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }
}
