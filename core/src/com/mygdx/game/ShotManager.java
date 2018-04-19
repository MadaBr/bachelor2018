package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShotManager {
    private Texture shotTexture;
    private List<ShotSprite> firedShots = new ArrayList<ShotSprite>();
    private float shotTimer =0f;
    private static final float TIMER_UP = 0.5f;
    private static final int SHOT_SPEED = 400;

    public ShotManager(Texture shotTexture) {
        this.shotTexture = shotTexture;
    }


    public void draw(SpriteBatch batch){
        for(ShotSprite shot : firedShots){
            batch.draw(shot.getShotTexture(),shot.getX(),shot.getY());
        }
    }

    public void fireShots(float shotXPosition,float shotYPosition){
        if(canFireShot()){
            ShotSprite shot = new ShotSprite(this.shotTexture);
            shot.setPosition(shotXPosition , shotYPosition );
            shot.setShotVelocity(new Vector2(0,SHOT_SPEED));
            firedShots.add(shot);
            shotTimer=0f;
        }

    }

    public boolean canFireShot(){
        return shotTimer > TIMER_UP;
    }

    public void update(float delta){
        Iterator<ShotSprite> shotsIterator = firedShots.iterator();

        while(shotsIterator.hasNext()){
            ShotSprite currentShot = shotsIterator.next();
            currentShot.moveShot();

            if(currentShot.getY() > 1280){
                shotsIterator.remove();
            }
        }

        shotTimer+= delta;
    }

    public boolean shotHitTarget(Rectangle enemyTarget){
        Iterator<ShotSprite> it = firedShots.iterator();
        while(it.hasNext()){
            ShotSprite currentShot = it.next();
            if(Intersector.intersectRectangles(enemyTarget,currentShot.getBoundingBox(),enemyTarget)){
                it.remove();
                return true;
            }
        }
        return false;
    }
}