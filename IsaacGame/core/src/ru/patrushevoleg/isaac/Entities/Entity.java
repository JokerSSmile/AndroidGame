package ru.patrushevoleg.isaac.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    enum moveState{
        NONE,
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    public Vector2 position;
    public Vector2 size;
    public Texture texture;
    public Vector2 velocity;
    public int damage;
    public int health;
    public Rectangle rectangle;

    public abstract void render(SpriteBatch batch);
    public abstract void update(float dt, MapObjects collidable);
}
