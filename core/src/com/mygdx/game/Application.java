package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Locale;
import java.util.Map;

import javax.swing.GroupLayout;

public class Application extends Game{

	public static AndroidActivityResolver androidResolver;

	public SpriteBatch batch;
	private Screen menuScreen;
	public  Map<String,String> translatedPairs;


	public Application(AndroidActivityResolver androidResolver,Map<String, String> translatedPairs) {
		this.androidResolver = androidResolver;
	    this.translatedPairs = translatedPairs;
	}

	@Override
	public void create () {
		Gdx.app.error("START", translatedPairs.size()+"");
        batch = new SpriteBatch();
		menuScreen = new MenuScreen(this);
		this.setScreen(menuScreen);
	}

	/*@Override
	public void render () {
		Gdx.gl.glClearColor(0.119f, 0.169f, 0.204f,1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(t,100,100);
		batch.end();

	}*/
	
	@Override
	public void dispose () {
	    menuScreen.dispose();
	}


}
