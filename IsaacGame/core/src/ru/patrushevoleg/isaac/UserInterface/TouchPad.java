package ru.patrushevoleg.isaac.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.patrushevoleg.isaac.MyGame;

public class TouchPad{

    private OrthographicCamera camera;
    private Touchpad touchpad;

    public TouchPad() {
        //Create camera
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 10f*aspectRatio, 10f);

        Viewport viewport = new FitViewport(MyGame.V_WIDTH, MyGame.V_HEIGHT, camera);

        //Create a touchpad skin
        Skin touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("ui/joystick/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("ui/joystick/touchKnob.png"));
        //Create TouchPad Style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(30, 30, 200, 200);

        //Create a Stage and add TouchPad
        Stage stage = new Stage(viewport, new SpriteBatch());
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);

    }

    public Vector2 getKnobPercent(){
        return new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
    }

    public void render(SpriteBatch batch) {
        camera.update();

        //Draw
        batch.begin();
        touchpad.draw(batch, 1);
        batch.end();
    }
}
