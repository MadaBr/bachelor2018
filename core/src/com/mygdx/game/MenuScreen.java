package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen{
    private Application app;

    private GameScreen gameScreen;
    private Stage stage;
    private Skin skin;
    private TextButton playButton, quitButton;
    private TextField chosedCategory;
    private ButtonGroup checkboxGroup;
    private CheckBox beginner,medium,advanced;
    private Window window;
    private  Label difficultyLabel, categoryLabel;
    private Table table;
    public AssetManager assets;
    private Texture background;

    public MenuScreen(final Application app){
        assets = new AssetManager();
        this.app = app;
        this.gameScreen = new GameScreen(app);

        this.stage = new Stage(new ScreenViewport());

        skin=new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));

        playButton = new TextButton("Play",skin,"default");
        quitButton = new TextButton("Quit",skin,"default");
        chosedCategory = new TextField("Animals",skin,"default-transparent");
        chosedCategory.setAlignment(Align.center);


        beginner = new CheckBox("Beginner",skin,"radio");
        advanced = new CheckBox("Advanced",skin,"radio");
        beginner.setScale(5f);
        advanced.setScale(5f);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Walkway_Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.color = new Color(0.119f,0.169f,0.204f,1);
        params.size = 35;

        BitmapFont labelFont = generator.generateFont(params);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = labelFont;

        //difficultyLabel =new Label("Difficulty:",skin,"default");
        // difficultyLabel.setFontScale(2f);
        difficultyLabel =new Label("Difficulty:",labelStyle);
        categoryLabel =new Label("Category:",labelStyle);
       // categoryLabel =new Label("Category:",skin,"default");
       // categoryLabel.setFontScale(2f);

        window=new Window("Menu Window", skin,"default");
        window.setWidth(stage.getWidth()-20);
        window.setHeight(stage.getHeight()-2*100);
        window.setPosition(10f,100);

         table = new Table();
        table.setWidth(window.getWidth());
        table.setHeight(window.getHeight());
        table.align(Align.center|Align.top);
        table.setOrigin(0,window.getHeight()-20);
        table.padTop(10f);

        table.add(playButton).width(400f).height(80f).padBottom(30f).row();
        table.add(quitButton).width(400f).height(80f).padBottom(30f).row();
        table.add(categoryLabel).width(200f).height(50f).padBottom(30f).row();
        table.add(chosedCategory).width(400f).height(50f).padBottom(30f).row();
        table.add(difficultyLabel).padBottom(5f).row();
        table.row();
        table.add(beginner).width(200f).height(50f).grow().padBottom(20f).row();
        table.row();
        table.add(advanced).width(200f).height(50f).grow().padBottom(20f).row();

        window.add(table);
        stage.addActor(window);

        background=new Texture(Gdx.files.internal("starry.jpg"));
        Gdx.input.setInputProcessor(stage);

        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                app.setScreen(gameScreen);
                return true;
            }
        });

        quitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                app.androidResolver.startAndroidActivity();
                return true;
            }
        });

        beginner.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                advanced.setChecked(false);

            }
        });

        advanced.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    beginner.setChecked(false);

            }
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.119f, 0.169f, 0.204f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        app.batch.begin();
        app.batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        app.batch.end();
        stage.draw();
        stage.act(Gdx.graphics.getDeltaTime());

        if(!advanced.isChecked() && !beginner.isChecked()){
            beginner.setChecked(true);
        }

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
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
