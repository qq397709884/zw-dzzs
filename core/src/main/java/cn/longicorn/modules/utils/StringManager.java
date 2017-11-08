package cn.longicorn.modules.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.*;

public class StringManager {

    private static final Logger log = LoggerFactory.getLogger(StringManager.class);

    private ResourceBundle bundle;

    private StringManager(String packageName) {
        String bundleName = packageName + ".LocalMessage";
        try {
            bundle = ResourceBundle.getBundle(bundleName);
        } catch (MissingResourceException ex) {
            // Try from the current loader ( that's the case for trusted apps )
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                try {
                    bundle = ResourceBundle.getBundle(bundleName, Locale.getDefault(), cl);
                    return;
                } catch (MissingResourceException ex2) {
                    //ignore
                }
            }
            if (cl == null)
                cl = this.getClass().getClassLoader();

            if (log.isDebugEnabled())
                log.debug("Can't find resource " + bundleName + " " + cl);
            if (cl instanceof URLClassLoader) {
                if (log.isDebugEnabled())
                    log.debug(Arrays.toString(((URLClassLoader) cl).getURLs()));
            }
        }
    }

    /**
     * Get a string from the underlying resource bundle.
     *
     * @param key The resource name
     */
    public String getString(String key) {
        return MessageFormat.format(getStringInternal(key), (Object[]) null);
    }

    protected String getStringInternal(String key) {
        if (key == null) {
            String msg = "key is null";

            throw new NullPointerException(msg);
        }
        String str;

        if (bundle == null)
            return key;
        try {
            str = bundle.getString(key);
        } catch (MissingResourceException mre) {
            str = "Cannot find message associated with key '" + key + "'";
        }

        return str;
    }

    public String getString(String key, Object[] args) {
        String iString;
        String value = getStringInternal(key);

        // this check for the runtime exception is some pre 1.1.6
        // VM's don't do an automatic toString() on the passed in
        // objects and barf out
        try {
            // ensure the arguments are not null so pre 1.2 VM's don't barf
            Object nonNullArgs[] = args;
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    if (nonNullArgs == args)
                        nonNullArgs = args.clone();
                    nonNullArgs[i] = "null";
                }
            }
            iString = MessageFormat.format(value, nonNullArgs);
        } catch (IllegalArgumentException iae) {
            StringBuilder buf = new StringBuilder();
            buf.append(value);
            for (int i = 0; i < args.length; i++) {
                buf.append(" arg[").append(i).append("]=").append(args[i]);
            }
            iString = buf.toString();
        }
        return iString;
    }

    public String getString(String key, Object arg) {
        Object[] args = new Object[]{arg};
        return getString(key, args);
    }

    public String getString(String key, Object arg1, Object arg2) {
        Object[] args = new Object[]{arg1, arg2};
        return getString(key, args);
    }

    public String getString(String key, Object arg1, Object arg2, Object arg3) {
        Object[] args = new Object[]{arg1, arg2, arg3};
        return getString(key, args);
    }

    public String getString(String key, Object arg1, Object arg2, Object arg3, Object arg4) {
        Object[] args = new Object[]{arg1, arg2, arg3, arg4};
        return getString(key, args);
    }

    private static Hashtable<String, StringManager> managers = new Hashtable<String, StringManager>();

    public synchronized static StringManager getManager(String packageName) {
        StringManager mgr = managers.get(packageName);
        if (mgr == null) {
            mgr = new StringManager(packageName);
            managers.put(packageName, mgr);
        }
        return mgr;
    }

    public synchronized static StringManager getManager(Class<?> clazz) {
        return getManager(clazz.getPackage().getName());
    }

}