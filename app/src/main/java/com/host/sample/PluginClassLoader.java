package com.host.sample;

import dalvik.system.DexClassLoader;

/**
 * Created by yangpeng on 2/15/18.
 */

public class PluginClassLoader extends DexClassLoader {
    public PluginClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        if (className.contains("com.host.sample.fake")) {
            className = "com.plugin.sample.MainActivity";
        }
        Class<?> pc;
        try {
            pc = super.loadClass(className, resolve);
            if (pc != null) {
                return pc;
            }
        } catch (ClassNotFoundException e) {
        }

        return null;
    }
}
