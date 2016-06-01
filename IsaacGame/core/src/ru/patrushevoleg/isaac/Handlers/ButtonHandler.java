package ru.patrushevoleg.isaac.Handlers;

public class ButtonHandler {

    private boolean isTouched;

    public ButtonHandler(){
        isTouched = false;
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
}
