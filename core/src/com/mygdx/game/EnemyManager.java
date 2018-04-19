package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Mada on 3/16/2018.
 */

public class EnemyManager{
    public Texture enemyTexture;
    public Texture collectableEnemyTexture;
    public List<String> enemyvalues = new ArrayList<String>();
    public ArrayList<EnemyActor> enemyPool;
    public  Random random;
    public float spawnTimeOut = 1f;
    public static String DEFAULT_KR_CHARACTERS;
    public GlyphLayout glyphLayout;

    public EnemyManager(List<String> enemyvalues){

        this.glyphLayout = new GlyphLayout();

        this.enemyvalues = enemyvalues;
      //  Gdx.app.error("MyTag", enemyvalues.size()+" " + enemyvalues.get(3));

        StringBuilder str = new StringBuilder();
        for(int i = 0; i < enemyvalues.size(); i++ ){
            str.append(enemyvalues.get(i));
        }
        DEFAULT_KR_CHARACTERS = str.toString();

        enemyTexture = new Texture(Gdx.files.internal("asteroid_adjusted.png"));
        collectableEnemyTexture = new Texture(Gdx.files.internal("star_blue.png"));

        this.enemyPool = new ArrayList<EnemyActor>();
        for(int i=0; i<3; i++){
            this.enemyPool.add(new EnemyActor(enemyTexture,4,2));
        }
        for(int i=0; i<5; i++){
            EnemyActor collectable = new EnemyActor(collectableEnemyTexture,1,1);
            collectable.isCollectible = true;
            this.enemyPool.add(collectable);
        }

        random = new Random();
    }


    public void updateStageEnemies(){
        EnemyActor currentEnemy =null;
        Iterator<EnemyActor> iterator = enemyPool.iterator();

        while (iterator.hasNext()){
            currentEnemy = iterator.next();
            if(currentEnemy.isActive==true) {
                if (currentEnemy.position.y + currentEnemy.frameHeight < 200) {
                    currentEnemy.isActive = false;
                    currentEnemy.position.y = Gdx.app.getGraphics().getHeight(); //reset position
                    currentEnemy.remove();
                }
            }
            if(currentEnemy.gotHit == true){
                currentEnemy.remove();
            }
        }

    }

    public void spawnEnemy(Stage gamestage){
        boolean found = false;
        int wordValuePosition=0, i;
        EnemyActor temp = null;

        if(spawnTimeOut < 0) {
            while(!found){
                i = random.nextInt(enemyPool.size());
                if (!enemyPool.get(i).isActive) {
                    temp = enemyPool.get(i);
                    found = true;
                    if(temp.isCollectible){
                        wordValuePosition = random.nextInt(enemyvalues.size());
                        glyphLayout.setText(temp.labelFont,enemyvalues.get(wordValuePosition));
                        temp.wordWidth = glyphLayout.width;

                        temp.setLabelText(enemyvalues.get(wordValuePosition));
                    }
                    temp.spawnEnemy();
                }
            }
            if (temp != null) {
                gamestage.addActor(temp);
                spawnTimeOut = 2f;
            }
        }
        else{
            spawnTimeOut -= Gdx.graphics.getDeltaTime();
        }
    }
}
