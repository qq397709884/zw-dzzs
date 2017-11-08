package cn.longicorn.modules.utils;

import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;

/**
 * Convenience class that performs custom log4j initialization for web environments,
 * allowing for log file paths set to a unified path, with the option to
 * perform automatic refresh checks (for runtime changes in logging configuration).
 *
 * <p>Supports three init parameters at the servlet context level (that is,
 * context-param entries in web.xml):
 *
 * <ul>
 * <li><i>"log4jConfigLocation":</i><br>
 * Location of the log4j config file; either a "classpath:" location (e.g.
 * "classpath:myLog4j.properties"), an absolute file URL (e.g. "file:C:/log4j.properties),
 * or a plain path relative to the web application root directory (e.g.
 * "/WEB-INF/log4j.properties"). If not specified, default log4j initialization
 * will apply ("log4j.properties" or "log4j.xml" in the class path; see the
 * log4j documentation for details).
 * <li><i>"log4jRefreshInterval":</i><br>
 * Interval between config file refresh checks, in milliseconds. If not specified,
 * no refresh checks will happen, which avoids starting log4j's watchdog thread.
 * <li><i>"applicationProperties":</i><br>
 * Location of the application properties file, default is "application.properties" in
 * the classpath.
 * <li><i>"log4jRootKey":</i></li><br>
 * The key of the log data root entry in the application properties file, default is
 * "sys.logRoot".
 * </ul>
 *
 * <p>Note: <code>initLogging</code> should be called before any other Spring activity
 * (when using log4j), for proper initialization before any Spring logging attempts.
 *
 * <p>Log4j's watchdog thread will asynchronously check whether the timestamp
 * of the config file has changed, using the given interval between checks.
 * A refresh interval of 1000 milliseconds (one second), which allows to
 * do on-demand log level changes with immediate effect, is not unfeasible.

 * <p><b>WARNING:</b> Log4j's watchdog thread does not terminate until VM shutdown;
 * in particular, it does not terminate on LogManager shutdown. Therefore, it is
 * recommended to <i>not</i> use config file refreshing in a production J2EE
 * environment; the watchdog thread would not stop on application shutdown there.
 *
 * <p>By default, this configurer automatically sets the log root system property
 * as definition in the application properties file(sys.logRoot),
 * for "${sys.logRoot}" substitutions within log file locations in the log4j config file,
 * allowing for log file paths relative to the log root directory.
 *
 * <p><code>log4j.appender.myfile.File=${sys.logRoot}/demo.log</code>
 *
 * <p>Alternatively, specify a unique context-param "log4jRootKey" per web application.
 * For example, with "log4jRootKey = "oap.logRoot":
 *
 * <p><code>log4j.appender.myfile.File=${oap.logRoot}/demo.log</code>
 *
 * <p><b>WARNING:</b> Some containers (like Tomcat) do <i>not</i> keep system properties
 * separate per web app. 
 *
 * @author Juergen Hoeller
 * @author zhuchanglin
 * @see org.springframework.util.Log4jConfigurer
 */
public abstract class Log4jWebConfigurer {

    /** Parameter specifying the location of the log4j config file */
    public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";

    /** Parameter specifying the refresh interval for checking the log4j config file */
    public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";

    /** Parameter specifying the application's properties file name, Default is application.properties */
    public static final String APP_INI_FILE = "applicationProperties";

    /** Parameter specifying the property key in application.properties file */
    public static final String LOG_ROOT_PARAM = "log4jRootKey";

    /**
     * Initialize log4j, including setting the log root system property.
     * @param servletContext the current ServletContext
     */
    public static void initLogging(ServletContext servletContext) {
        setLogRootAsSystemProperty(servletContext);

        // Only perform custom log4j initialization in case of a config file.
        String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        if (location != null) {
            // Perform actual log4j initialization; else rely on log4j's default initialization.
            try {
                // Return a URL (e.g. "classpath:" or "file:") as-is;
                // consider a plain file path as relative to the web application root directory.
                if (!ResourceUtils.isUrl(location)) {
                    // Resolve system property placeholders before resolving real path.
                    location = SystemPropertyUtils.resolvePlaceholders(location);
                    location = WebUtils.getRealPath(servletContext, location);
                }

                // Write log message to server log.
                servletContext.log("Initializing log4j from [" + location + "]");

                // Check whether refresh interval was specified.
                String intervalString = servletContext.getInitParameter(REFRESH_INTERVAL_PARAM);
                if (intervalString != null) {
                    // Initialize with refresh interval, i.e. with log4j's watchdog thread,
                    // checking the file in the background.
                    try {
                        long refreshInterval = Long.parseLong(intervalString);
                        Log4jConfigurer.initLogging(location, refreshInterval);
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: "
                                + ex.getMessage());
                    }
                } else {
                    // Initialize without refresh check, i.e. without log4j's watchdog thread.
                    Log4jConfigurer.initLogging(location);
                }
            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
            }
        }
    }

    /**
     * Shut down log4j, properly releasing all file locks
     * and resetting the web app root system property.
     * @param servletContext the current ServletContext
     * @see WebUtils#removeWebAppRootSystemProperty
     */
    public static void shutdownLogging(ServletContext servletContext) {
        servletContext.log("Shutting down log4j");
        try {
            Log4jConfigurer.shutdownLogging();
        } finally {
            System.getProperties().remove(getlogRootKey(servletContext));
        }
    }

    private static String getlogRootKey(ServletContext servletContext) {
        String logRootKey = servletContext.getInitParameter(LOG_ROOT_PARAM);
        return logRootKey == null ? "sys.logRoot" : logRootKey;
    }

    private static void setLogRootAsSystemProperty(ServletContext servletContext) throws IllegalStateException {
        String appIniFileName = servletContext.getInitParameter(APP_INI_FILE);
        if (appIniFileName == null) {
            appIniFileName = "application.properties";
        }
        String logRootKey = getlogRootKey(servletContext);
        String oldValue = System.getProperty(logRootKey);

        PropertiesLoader p = new PropertiesLoader(appIniFileName);
        String logRootPath = p.getProperty(logRootKey);

        if (oldValue != null && !StringUtils.pathEquals(oldValue, logRootPath)) {
            throw new IllegalStateException("Log root system property already set to different value: '"
                    + logRootKey + "' = [" + oldValue + "] instead of [" + logRootPath + "] - "
                    + "Choose unique values for the 'log4jRootKey' context-param in your web.xml files!");
        }
        System.setProperty(logRootKey, logRootPath);
        servletContext.log("Set log4j root path system property: '" + logRootKey + "' = [" + logRootPath + "]");
    }

}
