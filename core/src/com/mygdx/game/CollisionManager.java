package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Mada on 3/17/2018.
 */

public class CollisionManager {
    private ShotManager shotManager;
    private EnemyManager enemyManager;
    private ShipActor ship;
    private Map<String,String> translatedPairs = new HashMap<String, String>();
    private Random random = new Random();

    public CollisionManager(ShotManager shotManager, EnemyManager enemyManager, ShipActor ship, Map<String,String> translatedPairs){
        this.shotManager = shotManager;
        this.enemyManager =enemyManager;
        this.ship = ship;
        this.translatedPairs = translatedPairs;
    }

    public void handleCollisions(){

        handleEnemyGotHit();
        handleEnemyGotCollected();
        handleShipGotHit();
    }

    public void handleEnemyGotHit(){

        for(int i=0; i<enemyManager.enemyPool.size();i++) {
            EnemyActor temp = enemyManager.enemyPool.get(i);
            if((temp.isActive) && (!temp.isCollectible)) {
                if (shotManager.shotHitTarget(temp.getBoundingBox())){
                    temp.hitCount++;
                   // temp.gotHit();
                }
            }
        }
    }

    public void handleEnemyGotCollected(){
        for(int i=0; i<enemyManager.enemyPool.size();i++) { //pt ca tiai pus tu de la 3 incolo collectibles in enemypool
            EnemyActor temp = enemyManager.enemyPool.get(i);
            if((temp.isActive) && (temp.isCollectible)) {
                if (ship.enemyCollision(temp.getBoundingBox())){
                    Rectangle r1 = ship.getBoundingBox();
                    Rectangle r2 = temp.getBoundingBox();

                    if(temp.stringValue.getText().toString().equals(translatedPairs.get(HUD.word))){
                        Gdx.app.error("MY TAG","ship: " +r1.getX()+" "+ r1.getY() +" - " + r1.getWidth() + " " + r1.getHeight() + " > " +
                                "enemy: " +r2.getX()+" "+ r2.getY() +" - " + r2.getWidth() + " " + r2.getHeight() );
                        HUD.score += HUD.word.length() * 10;
                        HUD.word = HUD.wordValues.get(random.nextInt(HUD.wordValues.size()));
                        temp.gotHit();
                    }
                }
            }
        }
    }

    public void handleShipGotHit(){

        for(int i=0; i<enemyManager.enemyPool.size(); i++){
            EnemyActor temp = enemyManager.enemyPool.get(i);
            if((temp.isActive) && (!temp.isCollectible)){     //TO DO: if collectable has wrong string
                if (ship.enemyCollision(temp.getBoundingBox())) {
                    if (temp.generateDamageTimeout < 0) {
                        ship.lives--;
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
