package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Mada on 3/16/2018.
 */

public class ShotSprite {
    private Texture shotTexture;
    private Vector2 shotVelocity;
    public Vector2 position;

    public ShotSprite(Texture shotTexture){

        this.shotTexture = shotTexture;
        position = new Vector2(0,0);
    }

    public void setPosition(float x, float y){
        position.x = x;
        position.y = y;
        //din coord x scazi inca puti ai x sa ajunga sa fie mijlocul spriteului
    }

    public void setShotVelocity( Vector2 velocity){
        this.shotVelocity= velocity;
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public Texture getShotTexture() {
        return shotTexture;
    }

    public void moveShot(){
        float shotYMovement = shotVelocity.y * Gdx.graphics.getDeltaTime();
        position.y += shotYMovement;
    }

    public Rectangle getBoundingBox(){
        return new Rectangle(position.x,position.y,shotTexture.getWidth(),shotTexture.getHeight());
    }
}