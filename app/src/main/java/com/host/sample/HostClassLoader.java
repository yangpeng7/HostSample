package com.host.sample;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;

import dalvik.system.PathClassLoader;

/**
 * Created by yangpeng on 2/15/18.
 */

public class HostClassLoader extends PathClassLoader {

    public static Resources mPkgResources;
    public static PluginContext mPkgContext;
    private final ClassLoader mOrig;
    static PluginClassLoader dexClassLoader;

    public HostClassLoader(ClassLoader origin, ClassLoader parent) {
        super("", "", parent);
        mOrig = origin;

        String path = Environment.getExternalStorageDirectory() + File.separator;
        String filename = "plugin-debug.apk";

        File optimizedDirectoryFile = App.context.getCacheDir();
        dexClassLoader = new PluginClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),
                null, this.getClass().getClassLoader());

        PackageManager pm = App.context.getPackageManager();
        PackageInfo mPackageInfo = pm.getPackageArchiveInfo(path + filename,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS | PackageManager.GET_META_DATA);
        mPackageInfo.applicationInfo.sourceDir = path + filename;
        mPackageInfo.applicationInfo.publicSourceDir = path + filename;
        try {
            mPkgResources = pm.getResourcesForApplication(mPackageInfo.applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Object getContext(Activity activity, Context context) {
        mPkgContext = new PluginContext(context, android.R.style.Theme, activity.getClass().getClassLoader(), mPkgResources);
        return mPkgContext;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {

        Class<?> c;
        c = dexClassLoader.loadClass(className);

        if (c != null) {
            return c;
        }
        try {
            c = mOrig.loadClass(className);
            return c;
        } catch (Throwable e) {
        }
        return super.loadClass(className, resolve);
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        return super.findClass(className);
    }
}
