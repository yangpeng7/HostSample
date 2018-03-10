package com.host.sample;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * Created by yangpeng on 2/15/18.
 */

public class App extends Application {

    public static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;
        patch(this);
    }

    public static boolean patch(Application application) {
        try {
            Context oBase = application.getBaseContext();
            Object oPackageInfo = readField(oBase, "mPackageInfo");
            ClassLoader oClassLoader = (ClassLoader) readField(oPackageInfo, "mClassLoader");
            ClassLoader cl = new HostClassLoader(oClassLoader, oClassLoader.getParent());
            writeField(oPackageInfo, "mClassLoader", cl);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void setAccessible(AccessibleObject ao, boolean value) {
        if (ao.isAccessible() != value) {
            ao.setAccessible(value);
        }
    }

    public static Object readField(Object target, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        return readField(target.getClass(), target, fieldName);
    }

    public static Object readField(Class<?> c, Object target, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field f = getField(c, fieldName);

        return readField(f, target);
    }

    public static Object readField(final Field field, final Object target) throws IllegalAccessException {
        return field.get(target);
    }

    public static void writeField(Object target, String fName, Object value) throws NoSuchFieldException, IllegalAccessException {
        writeField(target.getClass(), target, fName, value);
    }

    public static void writeField(Class<?> c, Object object, String fName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(c, fName);
        writeField(f, object, value);
    }

    public static void writeField(final Field field, final Object target, final Object value) throws IllegalAccessException {
        field.set(target, value);
    }

    public static Field getField(Class<?> cls, String fieldName) {
        for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
            try {
                final Field field = acls.getDeclaredField(fieldName);
                setAccessible(field, true);
                return field;
            } catch (final NoSuchFieldException ex) {
            }
        }
        return null;
    }
}
