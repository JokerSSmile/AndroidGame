package ru.patrushevoleg.isaac.Handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ButtonHandler {

    private boolean isTouched;
    private Texture texture;
    private Vector2 position;
    private Rectangle rectangle;

    public ButtonHandler(Texture texture, Vector2 position, Vector2 size){
        isTouched = false;
        this.texture = texture;
        this.position = position;
        this.rectangle = new Rectangle(position.x * 1.62f, position.y * 1.95f, size.x, size.y);
    }

    public void onClick(){
        isTouched = true;
    }

    public boolean isOnRelease(){
        return isTouched;
    }

    public void notTouched(){
        isTouched = false;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, position.x, position.y);
    }
}
