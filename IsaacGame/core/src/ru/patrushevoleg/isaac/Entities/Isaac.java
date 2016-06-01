package ru.patrushevoleg.isaac.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.patrushevoleg.isaac.MyGame;
import ru.patrushevoleg.isaac.Screens.PlayState;

public class Isaac extends Entity{

    public static final Vector2 BODY_SIZE = new Vector2(36, 29);
    private static final int BASIC_VELOCITY = 3;
    private static final float TIME_BETWEEN_FRAMES = 0.1f;
    private static final float TILEMAP_SCALE = PlayState.TILEMAP_SCALE;

    private Texture headTexture;

    private float velocityBonus;
    private float stateTime;
    private Vector2 oldPosition;

    private Rectangle rectangle;

    private moveState playerMoveState = moveState.NONE;

    private Animation animation;
    private Animation walkUpDownAnimation;
    private Animation walkRightAnimation;
    private Animation walkLeftAnimation;
    private Animation standAnimation;

    public Isaac (Texture isaacTexture, Vector2 startPosition){

        this.texture = isaacTexture;
        position = startPosition;
        oldPosition = new Vector2(position);
        velocity = new Vector2();
        velocityBonus = 1;
        stateTime = 0;
        size = BODY_SIZE;

        rectangle = new Rectangle(position.x, position.y, size.x * 1.75f, size.y * 1.25f);

        createAnimations();
        animation = standAnimation;

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

        standAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[1][0]);
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public void inputHandler(Vector2 velocity){

        this.velocity.x = velocity.x * BASIC_VELOCITY * velocityBonus;
        this.velocity.y = velocity.y * BASIC_VELOCITY * velocityBonus;

        if (velocity.y > 0.8){
            playerMoveState = moveState.UP;
        }
        else if (velocity.y < -0.8){
            playerMoveState = moveState.DOWN;
        }
        else if (velocity.x > 0.5){
            playerMoveState = moveState.RIGHT;
        }
        else if (velocity.x < -0.5){
            playerMoveState = moveState.LEFT;
        }
        else{
            playerMoveState = moveState.NONE;
            this.velocity.x = 0;
            this.velocity.y = 0;
        }
    }

    public boolean checkCollision(MapObjects collidable){
        for (MapObject wall : collidable) {
            Rectangle rect = ((RectangleMapObject) wall).getRectangle();
            if (getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                System.out.println("collides");
                //position = oldPosition;
                //oldPosition = position;
                return true;
            }
        }
        return false;
    }

    private void setAnimation(){
        switch (playerMoveState){
            case UP:
                animation = walkUpDownAnimation;
                break;
            case DOWN:
                animation = walkUpDownAnimation;
                break;
            case RIGHT:
                animation = walkRightAnimation;
                break;
            case LEFT:
                animation = walkLeftAnimation;
                break;
            case NONE:
                animation = standAnimation;
                break;
        }
    }

    private void setPosition(){
        position.x += this.velocity.x;
        position.y += this.velocity.y;
    }

    public void update(float dt, Vector2 velocity, MapObjects collidable){

        stateTime += dt;
        inputHandler(velocity);

        oldPosition = position;
        if (!checkCollision(collidable)) {
            setPosition();
            //rectangle.setPosition(position.x + 20, position.y + 20);
            if (checkCollision(collidable)){
                position = oldPosition;
                //rectangle.setPosition(position.x + 20, position.y + 20);
            }
        }

        setAnimation();
    }


    public void render(SpriteBatch batch){
        batch.draw(animation.getKeyFrame(stateTime, true), position.x, position.y, BODY_SIZE.x * 3, BODY_SIZE.y * 3);
    }

}
