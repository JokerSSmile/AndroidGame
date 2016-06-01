package ru.patrushevoleg.isaac.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Entity {

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
}
