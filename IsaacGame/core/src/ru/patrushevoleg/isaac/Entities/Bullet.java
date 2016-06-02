package ru.patrushevoleg.isaac.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity{

    private static final Vector2 BULLET_SIZE = new Vector2(32, 32);

    public Bullet(Texture texture, Vector2 startPos, Vector2 velocity){
        position = startPos;
        this.velocity = velocity;
        this.texture = texture;
        size = BULLET_SIZE;
        //System.out.println(this.velocity.x);


        rectangle = new Rectangle(position.x, position.y, size.x * 1.75f, size.y * 1.25f);
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(texture, position.x, position.y);
    }

    public Rectangle getRectangle(){
        return rectangle;
    }


    @Override
    public void update(float dt, MapObjects collidable) {
        position.x += velocity.x * 10;
        position.y += velocity.y * 10;

        rectangle.setPosition(position);
    }
}
