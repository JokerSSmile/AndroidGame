package ru.patrushevoleg.isaac.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.patrushevoleg.isaac.MyGame;
import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;

public class PauseState extends Screen{


    public PauseState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);
        camera.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void inputHandler() {

    }

    @Override
    public void update(float dt) {
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        resources.reload();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
