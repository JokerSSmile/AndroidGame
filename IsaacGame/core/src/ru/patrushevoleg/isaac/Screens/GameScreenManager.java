package ru.patrushevoleg.isaac.Screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameScreenManager {

    private Stack<Screen> states;

    public GameScreenManager(){
        states = new Stack<Screen>();
    }

    public void push(Screen state){
        states.push(state);
    }

    public void pop() {
        states.pop();
    }

    public void set(Screen state){
        states.pop();
        states.push(state);
    }

    public void pause(){
        states.peek().pause();
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch batch){
        states.peek().render(batch);
    }

    public void dispose(){
        states.clear();
    }
}