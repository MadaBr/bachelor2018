package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ShipActor extends Actor{

    private int FRAMES_COL;
    private int FRAMES_ROW;
    public final int frameWidth,frameHeight;
    private TextureRegion currentFrame;
    private TextureRegion[] textureFrames;
    public Animation<TextureRegion> animation;
    private Vector2 velocity;
    public Vector2 position;
    private float stateTime=0f;
    public int xDestination, yDestination;
    public Vector2 initialPosition;
    public boolean moving;
    private ShotManager shotManager;
    public int lives ;
    public static int SPEED;

    public ShipActor(Sprite sprite, int frames_column, int frames_row){
        Texture shipTexture = sprite.getTexture();
        FRAMES_COL = frames_column;
        FRAMES_ROW=frames_row;

         frameWidth = shipTexture.getWidth()/FRAMES_COL;
         frameHeight = shipTexture.getHeight()/FRAMES_ROW;
        TextureRegion[][] temp = TextureRegion.split(shipTexture,frameWidth,frameHeight);

        textureFrames = new TextureRegion[FRAMES_COL*FRAMES_ROW];
        int index=0;
        for(int i=0; i < FRAMES_ROW; i++){
            for(int j=0; j < FRAMES_COL; j++){
                textureFrames[index++] = temp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.1f,textureFrames);
        velocity= new Vector2(0,0);
        position = new Vector2(sprite.getX(),sprite.getY());
        initialPosition= new Vector2(position.x,position.y);
        lives = 5;

        shotManager= new ShotManager(new Texture(Gdx.files.internal("laserShot.png")));

        this.SPEED = 700;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stateTime+= Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime,true);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(currentFrame,position.x,position.y);
        shotManager.draw((SpriteBatch) batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        maintainBoundaries();
        update(delta);
        if(CollisionManager.flickerTimeout > 0) {
            CollisionManager.flickerTimeout -= delta;
        }
        else{
            this.removeAction(CollisionManager.flicker);
        }
    }

    public void update(float delta){
        if(this.moving == true)
        {
            position.x += GameScreen.directionX * SPEED * delta; //700 nstead of speed
            position.y += GameScreen.directionY * SPEED * delta;
            if((Math.sqrt(Math.pow(position.x-initialPosition.x,2)+Math.pow(position.y-initialPosition.y,2))) >= GameScreen.distance)
            {
                position.x = GameScreen.end.x;
                position.y = GameScreen.end.y;
                initialPosition.x = position.x;
                initialPosition.y = position.y;
                this.moving = false;
            }
        }
        maintainBoundaries();
    }

    public void maintainBoundaries(){
        if(this.getX()<0){
            velocity.x=0;
            this.setX(0);
        }
        if(position.y < Gdx.app.getGraphics().getHeight()-1200){
            velocity.y=0;
            position.y = Gdx.app.getGraphics().getHeight()-1200;
            //this.setY(0);
        }
        if((position.x + frameWidth)>Gdx.app.getGraphics().getWidth()){
            velocity.x=0;
            position.x = Gdx.app.getGraphics().getWidth() - frameWidth;
        }
        if(position.y > Gdx.app.getGraphics().getHeight() - frameHeight/2 ){
            velocity.y=0;
            position.y =  Gdx.app.getGraphics().getHeight() - frameHeight/2;

        }
    }

    public ShotManager getShotManager() {
        return this.shotManager;
    }

    public Rectangle getBoundingBox(){
        return new Rectangle(position.x + frameWidth/2 - 20,position.y + frameHeight/2 - 20,40,40);
    }

    public boolean enemyCollision(Rectangle enemyBox){
        if(Intersector.intersectRectangles(enemyBox,getBoundingBox(),enemyBox)){
            return true;
        }
        else{
            return false;
        }
    }
}
