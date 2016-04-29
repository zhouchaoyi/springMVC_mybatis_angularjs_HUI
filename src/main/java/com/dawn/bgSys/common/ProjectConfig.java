package com.dawn.bgSys.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Created with malone
 * User: P0032597
 * Date: 13-8-20
 * Time: 下午4:13
 */
public class ProjectConfig {

    private static Logger logger = LoggerFactory.getLogger(ProjectConfig.class);

    private static Properties instance;

    private ProjectConfig() {
    }

    private static Properties getInstance() {
        if (instance == null) {
            synchronized (ProjectConfig.class) {
                if (instance == null) {
                    instance = new Properties();
                    try {
//                        instance.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("project.properties"));
                        URL url = Thread.currentThread().getContextClassLoader().getResource("properties"+ File.separator+"project.properties");
                        //logger.info("##url##="+url);
                        //Assert.assertNotNull(url);
                        //String urlStr = url.toString();
                        //urlStr = urlStr.replace("file:/", "/");
                        //urlStr = urlStr.replace("/", "\\");

                        File file = new File(url.toURI());
                        FileReader reader = new FileReader(file);
                        instance.load(reader);
                    } catch (IOException e) {
                        logger.error("ERROR IN ProjectConfig.getInstance()", e);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        logger.error("ERROR IN ProjectConfig.getInstance()", e);
                    }
                }
            }
        }
        return instance;
    }

    public static String get(String key) {
        return getInstance().getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return getInstance().getProperty(key, defaultValue);
    }

    public static String getProperty(String key, Object... args) {
        MessageFormat format = new MessageFormat(get(key));
        return format.format(args);
    }

    public static Integer getInt(String key) {
        return Integer.parseInt(getInstance().getProperty(key, "0"));
    }

    public static Integer getInt(String key, String defaultValue) {
        return Integer.parseInt(getInstance().getProperty(key, defaultValue));
    }

    public static Long getLong(String key) {
        return Long.parseLong(getInstance().getProperty(key));
    }

    public static Boolean getBoolean(String key) {
        return Boolean.parseBoolean(getInstance().getProperty(key));
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return Boolean.parseBoolean(getInstance().getProperty(key, defaultValue.toString()));
    }
}
