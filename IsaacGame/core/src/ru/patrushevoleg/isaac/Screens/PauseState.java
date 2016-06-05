package ru.patrushevoleg.isaac.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ru.patrushevoleg.isaac.Handlers.ButtonHandler;
import ru.patrushevoleg.isaac.MyGame;
import ru.patrushevoleg.isaac.ResourcesStorage.ResourceManager;

public class PauseState extends Screen{

    private static final Vector2 RESUME_BTN_POS = new Vector2(-90, -90);
    private static final Vector2 MENU_BTN_POS = new Vector2(-75, -100);

    private Texture background;

    private ButtonHandler resumeButton;
    private ButtonHandler menuButton;

    public PauseState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);
        camera.setToOrtho(false, MyGame.V_WIDTH, MyGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background = resources.getTexture(ResourceManager.halfBlackTexture);
        resumeButton = new ButtonHandler(resources.getTexture(ResourceManager.resumeButtonTexture),
                new Vector2(camera.position.x + camera.viewportWidth / 2 + RESUME_BTN_POS.x,
                        camera.position.y + camera.viewportHeight / 2 + RESUME_BTN_POS.y),
                new Vector2(55, 55));

        menuButton = new ButtonHandler(resources.getTexture(ResourceManager.menuButtonTexture),
                new Vector2(camera.position.x + MENU_BTN_POS.x, camera.position.y + MENU_BTN_POS.y),
                new Vector2(150, 70));
    }

    @Override
    public void inputHandler() {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), MyGame.V_HEIGHT - Gdx.input.getY());

        if (resumeButton.getRectangle().contains(mousePos.x, mousePos.y) && Gdx.input.justTouched()){
            resumeButton.onClick();
        }
        else{
            resumeButton.notTouched();
        }

        if (resumeButton.isOnRelease()){
            gsm.pop();
        }

        if (menuButton.getRectangle().contains(mousePos.x, mousePos.y) && Gdx.input.justTouched()){
            menuButton.onClick();
        }
        else{
            menuButton.notTouched();
        }

        if (menuButton.isOnRelease()){
            gsm.set(new MenuState(gsm, resources));
        }
    }

    @Override
    public void update(float dt) {
        inputHandler();
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        resumeButton.render(batch);
        menuButton.render(batch);
        batch.end();


        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(menuButton.getRectangle().getX(), menuButton.getRectangle().getY(),
                menuButton.getRectangle().getWidth(), menuButton.getRectangle().getHeight());
        shapeRenderer.end();

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
        System.out.println("PAUSE DISPOSED");
    }
}
