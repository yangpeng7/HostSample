package com.plugin.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends Activity {
    Class<?> threadClazz = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        try {
            threadClazz = Class.forName("com.host.sample.HostClassLoader");
            Method method = threadClazz.getMethod("getContext", Activity.class, Context.class);
            newBase = (Context) method.invoke(null, this, newBase);

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Toast.makeText(this, threadClazz.getClassLoader().toString().toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }

    }
}
