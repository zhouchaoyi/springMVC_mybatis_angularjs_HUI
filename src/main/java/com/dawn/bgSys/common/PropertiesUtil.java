package com.dawn.bgSys.common;

import com.dawn.bgSys.exception.OperateFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with malone
 * User: P0032597
 * Date: 13-8-20
 * Time: 下午4:13
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private Properties props = new Properties();

    private File file;

    /**
     * 构造函数
     * @param propertiesName 文件名称
     */
    public PropertiesUtil(String propertiesName) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("/properties/"+propertiesName);
            if (url == null) {
                throw new OperateFailureException("读取配置文件出错!");
            }
//            String urlStr =  url.toString();
//            urlStr = urlStr.replace("file:/", "");
//            urlStr = urlStr.replace("/", "\\");
            File file = new File(url.toURI());
            this.propsLoad(file);
        } catch (URISyntaxException e) {
            logger.error("读取配置文件出错!", e);
            throw new RuntimeException("读取配置文件出错!");
        }

    }

    /**
     * 构造函数
     * @param file 文件
     */
    public PropertiesUtil(File file) {
        this.propsLoad(file);
    }

    /**
     * 辅助方法，根据file创建工具对象
     * @param file
     */
    private void propsLoad(File file) {
        FileReader reader = null;
        try {
            this.file = file;
            reader = new FileReader(file);
            props.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("error status", e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("error status", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("error!", e);
            }
        }
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public String getKeyValue(String key) {
        return props.getProperty(key);
    }

    /**
     * 向properties文件写入键值对
     * @param keyName
     * @param keyValue
     */
    public void writeProperties(String keyName, String keyValue) {
        Map<String, String> propertiesMap = new HashMap<String, String>();
        propertiesMap.put(keyName, keyValue);
        batchWriteProperties(propertiesMap);
    }

    /**
     * 批量更新properties的键值对
     * @param propertiesMap
     */
    public void batchWriteProperties(Map<String, String> propertiesMap) {
        FileWriter writer = null;
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            writer = new FileWriter(this.file);
            //            OutputStream fos = new FileOutputStream(profilepath);
            for (String key : propertiesMap.keySet()) {
                this.props.setProperty(key, propertiesMap.get(key));
            }
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            this.props.store(writer, "batch update");
        } catch (IOException e) {
            logger.error("error status", e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("error!", e);
            }
        }
    }

    /**
     * 更新properties文件
     * @param keyName 名称
     * @param keyValue 值
     */
    public void updateProperties(String keyName,String keyValue) {
        Map<String, String> propertiesMap = new HashMap<String, String>();
        propertiesMap.put(keyName, keyValue);
        this.batchUpdateProperties(propertiesMap);
    }

    /**
     * 批量更新键值对
     * @param propertiesMap
     */
    public void batchUpdateProperties(Map<String, String> propertiesMap) {
        BufferedWriter output = null;
        try {
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            //            OutputStream fos = new FileOutputStream(profilepath);
            output = new BufferedWriter(new FileWriter(this.file));
            for (String key : propertiesMap.keySet()) {
                this.props.setProperty(key, propertiesMap.get(key));
            }
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(output, "batch update ");
        } catch (IOException e) {
            logger.error("error status", e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                logger.error("error!", e);
            }

        }
    }

    public static PropertiesUtil getVersionControllUtil()  {
        PropertiesUtil pUtil1 = new PropertiesUtil("project.properties");
        String path = pUtil1.getKeyValue("PROPERTIES_PATH");
        String name = pUtil1.getKeyValue("PROPERTIES_NAME");
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        File realFile = new File(path + name);
        PropertiesUtil pUtil2 = null;
        if (!realFile.exists()) {
            try {
                realFile.createNewFile();
            } catch (IOException e) {
                logger.error( "error", e);
            }

            pUtil2 = new PropertiesUtil(realFile);
            /*pUtil2.writeProperties4File("APP.BASELOCATION", "E:\\UPDATE\\");
            pUtil2.writeProperties4File("APP.FILEDIR", "E:\\UPDATE\\IntelRetail_1.2.0.8_Build(20131018)_PRO_Beta.apk");
            pUtil2.writeProperties4File("APP.VERSION", "1.2.0.8");

            pUtil2.writeProperties4File("APP.UPDATENOTE", "1. 增加了巡店安排功能\n" +
                    "2. 增加了入店培训功能\n" +
                    "3. 增加了商店活动功能\n" +
                    "4. 增加了销售话术功能");
            pUtil2.writeProperties4File("TAR.BASELOCATION", "E:\\SALESWORD\\");
            pUtil2.writeProperties4File("TAR.FILEDIR", "E:\\SALESWORD\\xiaoshouhuashu.zip");
            pUtil2.writeProperties4File("TAR.VERSION", "1.0.0.0");*/
        } else {
            pUtil2 = new PropertiesUtil(realFile);
        }
        return pUtil2;
    }

    /**
     *获取配置文件中的所有key集合
     * @return
     * @throws IOException
     */
    public static Enumeration getPropertyNames(File file) throws IOException {
        Properties p = new Properties();
        p.load(new FileReader(file));
        return p.propertyNames();
    }

}
