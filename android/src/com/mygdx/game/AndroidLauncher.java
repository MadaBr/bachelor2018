package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.Application;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		Intent data = getIntent();
		String nativeLanguage = data.getStringExtra("nativeLanguage");
		String studyingLanguage = data.getStringExtra("studyingLanguage");
		String category = data.getStringExtra("category");

		AndroidActivityResolver androidResolver = new AndroidResolver(this);
		Log.wtf("DEBUGGING", "4. launcher starting game");
		initialize(new Application(androidResolver,CategoryActivity.translatedPairs, nativeLanguage,studyingLanguage, category), config);
	}
}
