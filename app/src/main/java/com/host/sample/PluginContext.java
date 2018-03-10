package com.host.sample;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;

/**
 * Created by yangpeng on 2/16/18.
 */

public class PluginContext extends ContextThemeWrapper {

    private final ClassLoader mNewClassLoader;

    private final Resources mNewResources;

    public PluginContext(Context base, int themeres, ClassLoader cl, Resources r) {
        super(base, themeres);

        mNewClassLoader = cl;
        mNewResources = r;
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mNewClassLoader != null) {
            return mNewClassLoader;
        }
        return super.getClassLoader();
    }

    @Override
    public Resources getResources() {
        if (mNewResources != null) {
            return mNewResources;
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (mNewResources != null) {
            return mNewResources.getAssets();
        }
        return super.getAssets();
    }
}
