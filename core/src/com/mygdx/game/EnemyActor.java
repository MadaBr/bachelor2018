package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.awt.SplashScreen;

public class EnemyActor extends Actor{

    private int FRAMES_COL;
    private int FRAMES_ROW;

    private Texture enemyTexture;
    private TextureRegion[] textureFrames;
    TextureRegion currentFrame;
    private Animation<TextureRegion> animation;
    public Label stringValue;

    private float stateTime;

    public Vector2 position;
    public static Vector2 velocity;

    private float spawnTimeOut = 0f;
    public int frameWidth,frameHeight;
    public float wordWidth;

    public boolean isActive ;
    public boolean gotHit;
    public int hitCount;

    public boolean isCollectible;
    public  float generateDamageTimeout = -1f;

    public BitmapFont labelFont;

    public EnemyActor(Texture enemyTexture, int column, int row ){
        this.enemyTexture = enemyTexture;
        FRAMES_COL = column;
        FRAMES_ROW = row;

        frameWidth = enemyTexture.getWidth()/FRAMES_COL;
        frameHeight = enemyTexture.getHeight()/FRAMES_ROW;

        TextureRegion[][] temp = TextureRegion.split(enemyTexture,frameWidth,frameHeight);
        textureFrames = new TextureRegion[FRAMES_COL * FRAMES_ROW];
        int index = 0;
        for(int i=0; i < FRAMES_ROW; i++){
            for(int j=0; j< FRAMES_COL; j++){
                textureFrames[index++] = temp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.1f, textureFrames);

        float x = (float)Math.random() * ((720-frameWidth/2) - (frameWidth/2));
        position = new Vector2(x,Gdx.app.getGraphics().getHeight());

        if(Application.studyingLanguage.equals("kor")) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Typo_DodamM.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
            params.color = new Color(0.119f, 0.169f, 0.204f, 1);
            params.size = 25;
            params.characters = Application.STUDYING_LANGUAGE_CHARS;

            labelFont = generator.generateFont(params);
            generator.dispose();
        }
        else{
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
            params.color = new Color(0.119f, 0.169f, 0.204f, 1);
            params.size = 25;
            params.characters = Application.STUDYING_LANGUAGE_CHARS;

            labelFont = generator.generateFont(params);
            generator.dispose();
        }

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = labelFont;

        stringValue=new Label("",labelStyle);
        stringValue.setPosition(position.x + (frameWidth)/2 -20, position.y + (frameHeight)/2 + 5);

        velocity = new Vector2(0,-150);


        isActive = false;
        gotHit = false;
        hitCount = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime,true);
        batch.draw(currentFrame,position.x,position.y);
        if(this.isCollectible) {
            stringValue.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        moveDownward(delta);
        if((!this.isCollectible) && (hitCount > 2)){
            gotHit();
        }
    }

    public void setLabelText(String text){
        this.stringValue.setText(text);
    }

    public void moveDownward(float delta){
            float yMovement = velocity.y * delta;
            position.y +=  yMovement;

            if(this.isCollectible) {
                stringValue.setPosition(position.x + (frameWidth) / 2 - wordWidth/2 , position.y + (frameHeight) / 2 + 5);
            }
    }

    public void spawnEnemy() {
        this.isActive=true;
        this.gotHit = false;
        this.hitCount = 0;
        float x = (float)Math.random() * ((720-frameWidth/2) - (frameWidth/2));
        position.x=x;
        position.y=Gdx.app.getGraphics().getHeight();
        if(this.isCollectible) {
            stringValue.setPosition(position.x + (frameWidth) / 2 -  wordWidth/2, position.y + (frameHeight) / 2 + 5);
        }
    }

    public void gotHit(){
        this.isActive = false;
        this.gotHit = true;
        position.y=Gdx.app.getGraphics().getHeight(); //ca sa nu iti mai dispara shoturile in zona unde a fost distrus
        this.spawnTimeOut = 0f;
    }


    public Rectangle getBoundingBox() {
        if (!this.isCollectible) {
            return new Rectangle(position.x + 20, position.y + 10, position.x + frameWidth - 270, position.y + frameHeight -100);
        }
        else{
            return new Rectangle(position.x + frameWidth/2 - 10, position.y +frameHeight/2 - 20, 20,20);
        }
    }

}
