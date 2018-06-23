package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Mada on 3/16/2018.
 */

public class EnemyManager{
    public Texture enemyTexture;
    public Texture collectibleEnemyTexture;
    public Map<String,String> translatedPairs = new HashMap<String, String>();
    public List<String> enemyvalues = new ArrayList<String>();
    public List<Integer> enemyvaluesFrequency;
    public ArrayList<EnemyActor> enemyPool;
    public  Random random;
    public float spawnTimeOut = 1f;
    public static String DEFAULT_KR_CHARACTERS;
    public GlyphLayout glyphLayout;
    private int collectibleDisplayCounter = 0;

    public EnemyManager(List<String> enemyvalues, Map<String,String> translatedPairs){

        this.glyphLayout = new GlyphLayout();

        this.enemyvalues = enemyvalues;
        this.translatedPairs = translatedPairs;
        enemyvaluesFrequency =  new ArrayList<Integer>(Collections.nCopies(enemyvalues.size(), 0));

        StringBuilder str = new StringBuilder();
        for(int i = 0; i < enemyvalues.size(); i++ ){
            str.append(enemyvalues.get(i));
        }
        DEFAULT_KR_CHARACTERS = str.toString();

        enemyTexture = new Texture(Gdx.files.internal("asteroid_adjusted.png"));
        collectibleEnemyTexture = new Texture(Gdx.files.internal("star_blue.png"));

        this.enemyPool = new ArrayList<EnemyActor>();
        for(int i=0; i<3; i++){
            this.enemyPool.add(new EnemyActor(enemyTexture,4,2));
        }
        for(int i=0; i<5; i++){
            EnemyActor collectible = new EnemyActor(collectibleEnemyTexture,1,1);
            collectible.isCollectible = true;
            this.enemyPool.add(collectible);
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
                       // wordValuePosition = random.nextInt(enemyvalues.size()); //get random word to show
                        wordValuePosition = collectiblePositionToDisplay();
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

    public void actualizePreviousHUDWordFrequency(){
        for(int i=0; i<enemyvaluesFrequency.size();i++){
            if(enemyvaluesFrequency.get(i) == Integer.MAX_VALUE){
                if(!enemyvalues.get(i).equals(this.translatedPairs.get(HUD.word))){
                    enemyvaluesFrequency.set(i,1);
                    return;
                }
            }
        }
    }

    public int collectiblePositionToDisplay(){
        int position;
        int translatedHUDWordPosition = enemyvalues.indexOf(this.translatedPairs.get(HUD.word));//get the position of HUD word inside eneyvalues
        enemyvaluesFrequency.set(translatedHUDWordPosition, Integer.MAX_VALUE);
        actualizePreviousHUDWordFrequency();
       /* if(Collections.frequency(enemyvaluesFrequency,Integer.MAX_VALUE) > 1){
            enemyvaluesFrequency = new ArrayList<Integer>(Collections.nCopies(enemyvalues.size(), 0));
            enemyvaluesFrequency.set(translatedHUDWordPosition, Integer.MAX_VALUE);
        }*/

        if(collectibleDisplayCounter > random.nextInt(4)){
            position = enemyvaluesFrequency.indexOf(Integer.MAX_VALUE);
            collectibleDisplayCounter = 0;
        }
        else{
            if(collectibleDisplayCounter%2 == 0) {
                position = enemyvaluesFrequency.indexOf(Collections.min(enemyvaluesFrequency));
            }
            else{
                position = enemyvaluesFrequency.lastIndexOf(Collections.min(enemyvaluesFrequency));
            }
             enemyvaluesFrequency.set(position,enemyvaluesFrequency.get(position) + 1);
        }
        collectibleDisplayCounter++;

        for(int i =0; i< enemyvaluesFrequency.size();i++){
            Gdx.app.error("EnemyValues: " , enemyvalues.get(i) + " " + enemyvaluesFrequency.get(i));
        }
        return  position;
    }


}
