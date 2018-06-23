package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HUD {

    public Stage HUDStage;
    public Viewport HUDViewport;

    public SpriteBatch batch;

    public static Integer score;
    public Integer lives;
    public static String word;
    public static List<String> wordValues = new ArrayList<String>();

    public Skin skin;
    public Label scoreLabel, livesLabel, wordLabel, scoreHeader, livesHeader, wordHeader;
    public Image background;

    public Random random;

    public HUD(SpriteBatch batch, ShipActor ship, List<String> wordValues){
        this.batch = batch;
        this.wordValues = wordValues;
        score = 0;
        lives = ship.lives;
        word = "Default";

        batch.enableBlending();
        HUDViewport = new FitViewport(Gdx.app.getGraphics().getWidth(),700, new OrthographicCamera(Gdx.app.getGraphics().getWidth(),700));
        HUDStage = new Stage(HUDViewport,batch);

        skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        Table table  = new Table();

        table.bottom();
        table.setFillParent(true);

        FreeTypeFontGenerator g = new FreeTypeFontGenerator(Gdx.files.internal("Raleway-Regular.ttf"));//"Walkway_Black.ttf"
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.color = new Color(1,1,1,1);
        params.size = 20;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS + Application.NATIVE_LANGUAGE_CHARS;

        BitmapFont labelFont = g.generateFont(params);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = labelFont;

        Texture backgroundTexture = new Texture(Gdx.files.internal("background_HUD_gradient2.png"));
        background = new Image(backgroundTexture);
        background.setSize(Gdx.app.getGraphics().getWidth(),60);

        scoreHeader = new Label("SCORE",labelStyle);
        livesHeader = new Label("LIVES",labelStyle);//skin,"default-white"
        wordHeader = new Label("FIND",labelStyle);
        scoreLabel = new Label(String.format("%06d",score),labelStyle);
        livesLabel = new Label(String.format("%01d",lives),labelStyle);
        wordLabel =  new Label(word,labelStyle);

        table.add(scoreHeader).expandX().padBottom(10);
        table.add(livesHeader).expandX().padBottom(10);
        table.add(wordHeader).expandX().padBottom(10);
        table.row();
        table.add(scoreLabel).expandX().padBottom(5);
        table.add(livesLabel).expandX().padBottom(5);
        table.add(wordLabel).expandX().padBottom(5);

        HUDStage.addActor(background);
        HUDStage.addActor(table);

        word = wordValues.get(0);
        wordLabel.setText(word);
        random = new Random();
    }

    public void updateHUD(ShipActor ship){
        lives = ship.lives;
        livesLabel.setText((String.format("%01d",lives)));

        wordLabel.setText(word);
        scoreLabel.setText(score.toString());
    }

    public static void actualizeHUDWord(){
        word = wordValues.get(new Random().nextInt(wordValues.size()));
    }

    public static void actualizeHUDScore(){
       score += word.length() * 10;
    }
}
