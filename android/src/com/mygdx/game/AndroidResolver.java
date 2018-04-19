package com.mygdx.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mada on 4/1/2018.
 */

public class AndroidResolver extends Activity implements AndroidActivityResolver {
    private Context context;

    public AndroidResolver(Context context){
        this.context = context;
    }

    @Override
    public void startAndroidActivity() {
        Intent intent = new Intent(context,CategoryActivity.class);
        ((Activity)context).startActivity(intent);
    }
}
