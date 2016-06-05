package ru.patrushevoleg.isaac.Entities;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemy extends Entity {

    public int room;
    protected Sound deathSound;

    public abstract void getDamage(int dmg);
}
