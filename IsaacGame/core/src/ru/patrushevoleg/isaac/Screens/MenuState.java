package ru.patrushevoleg.isaac.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ru.patrushevoleg.isaac.MyGame;
import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;


public class MenuState extends Screen {

    private final Vector2 SCREEN_CENTER = new Vector2(MyGame.V_WIDTH / 2, MyGame.V_HEIGHT / 2);
    private Texture menuBg = resources.getTexture(ResourceManager.menuBackgroundTexture);

    public MenuState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);
        camera.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void inputHandler() {
        mousePosition = viewport.unproject(mousePosition);

        if (Gdx.input.isTouched())
        {
            gsm.set(new PlayState(gsm, resources));
        }
    }

    @Override
    public void update(float dt) {
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        inputHandler();
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl20.glClearColor(1, 1, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(menuBg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
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