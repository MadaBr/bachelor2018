package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GameScreen implements Screen {
    private Application app;

    private Camera gameCamera;
    private Stage gameStage;

    private ShipActor ship;
    private  EnemyManager enemyManager;
    private CollisionManager collisionManager;
    private HUD hud;
    public static float directionX, directionY, distance;
    public static Vector2 end;

    public GameScreen(final Application app) {
        this.app = app;

        gameCamera = new OrthographicCamera();
        gameStage = new Stage(new FitViewport(Gdx.app.getGraphics().getWidth(),Gdx.app.getGraphics().getHeight()-30));
        gameCamera.position.set(gameStage.getWidth(),gameStage.getHeight(),0);

        final Sprite shipSprite = new Sprite(new Texture(Gdx.files.internal("spaceship_sprite.png")));
        shipSprite.setPosition(Gdx.graphics.getWidth() / 2 - shipSprite.getWidth() / 4 / 2, 0);
        ship = new ShipActor(shipSprite, 4, 2);

        List<String> enemyValues = new ArrayList<String>();
        for(String s: app.translatedPairs.values()){
            enemyValues.add(s);
        }
        enemyManager = new EnemyManager(enemyValues);

        gameStage.addActor(ship);

        collisionManager = new CollisionManager(ship.getShotManager(),enemyManager, ship, app.translatedPairs);

        List<String> wordValues = new ArrayList<String>();
        for(String s: app.translatedPairs.keySet()){
            wordValues.add(s);
        }
        hud = new HUD(app.batch, ship, wordValues);


        Gdx.input.setInputProcessor(gameStage);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.119f, 0.169f, 0.204f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        app.batch.setProjectionMatrix(hud.HUDStage.getCamera().combined);

        enemyManager.spawnEnemy(gameStage);
        enemyManager.updateStageEnemies();

        gameStage.draw();


        hud.HUDStage.draw();

        handleTouchInput();

        gameStage.act(delta);

        ship.getShotManager().update(delta);

        collisionManager.handleCollisions();
        hud.updateHUD(ship);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameStage.dispose();
    }

    public void handleTouchInput() {
        if (Gdx.input.isTouched()) {

                if(ship.getBoundingBox().contains(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY())){
                    ship.getShotManager().fireShots(ship.position.x+ship.frameWidth/2,
                                                    ship.position.y+ship.frameHeight+20);
                }

                    if (ship.moving == true) {
                        ship.initialPosition.x = ship.position.x;
                        ship.initialPosition.y = ship.position.y;
                    }
                   // end = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()); - uncentered
                    end = new Vector2(Gdx.input.getX() - ship.frameWidth/2, Gdx.graphics.getHeight() - Gdx.input.getY()-ship.frameHeight/2);
                    distance = Vector2.dst(ship.initialPosition.x, ship.initialPosition.y, end.x, end.y);
                    if ((int) distance != 0) {
                        directionX = (end.x - ship.initialPosition.x) / distance;
                        directionY = (end.y - ship.initialPosition.y) / distance;
                        ship.moving = true;
                    }

        }
    }
}

