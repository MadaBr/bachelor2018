package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Mada on 3/17/2018.
 */

public class  CollisionManager {
    private final Application app;
    private ShotManager shotManager;
    private EnemyManager enemyManager;
    private ShipActor ship;
    private Map<String,String> translatedPairs = new HashMap<String, String>();
    private Random random = new Random();
    public static SequenceAction flicker = new SequenceAction(Actions.fadeOut(0.25f), Actions.fadeIn(0.25f));
    public static float flickerTimeout = 2.0f;

    public CollisionManager(ShotManager shotManager, EnemyManager enemyManager, ShipActor ship, Map<String,String> translatedPairs, Application app){
        this.app = app;
        this.shotManager = shotManager;
        this.enemyManager =enemyManager;
        this.ship = ship;
        this.translatedPairs = translatedPairs;
    }

    public void handleCollisions(){

        handleEnemyGotHit();
        handleEnemyGotCollected();
        handleShipGotHit();
        if(ship.lives < 0){
            app.setScreen(new MenuScreen(app, true));
        }
    }

    public void handleEnemyGotHit(){

        for(int i=0; i<enemyManager.enemyPool.size();i++) {
            EnemyActor temp = enemyManager.enemyPool.get(i);
            if((temp.isActive) && (!temp.isCollectible)) {
                if (shotManager.shotHitTarget(temp.getBoundingBox())){
                    temp.hitCount++;
                }
            }
        }
    }

    public void handleEnemyGotCollected(){
        for(int i=0; i<enemyManager.enemyPool.size();i++) {
            EnemyActor temp = enemyManager.enemyPool.get(i);
            if((temp.isActive) && (temp.isCollectible)) {
                if (ship.enemyCollision(temp.getBoundingBox())){
                    Rectangle r1 = ship.getBoundingBox();
                    Rectangle r2 = temp.getBoundingBox();

                    if(temp.stringValue.getText().toString().equals(translatedPairs.get(HUD.word))){
                        HUD.actualizeHUDScore();;
                        HUD.actualizeHUDWord();
                        temp.gotHit();
                    }
                }
            }
        }
    }

    public void handleShipGotHit(){

        for(int i=0; i<enemyManager.enemyPool.size(); i++){
            EnemyActor temp = enemyManager.enemyPool.get(i);
            if((temp.isActive) && (!temp.isCollectible)){
                if (ship.enemyCollision(temp.getBoundingBox())) {
                    if (temp.generateDamageTimeout < 0) {
                        ship.lives--;
                        ship.addAction(Actions.repeat(2, flicker));
                        this.flickerTimeout = 2.0f;
                        temp.generateDamageTimeout = 2f;
                    }
                    else{
                        temp.generateDamageTimeout -= Gdx.graphics.getDeltaTime();
                    }
                }
            }
        }
    }
}
