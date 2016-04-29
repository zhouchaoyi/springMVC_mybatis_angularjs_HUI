package com.dawn.bgSys.common;//package com.intel.store.common;
//
//import junit.framework.Assert;
//import org.dom4j.*;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.SAXReader;
//import org.dom4j.io.XMLWriter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * Created with malone
// * User: P0032597
// * Date: 13-8-28
// * Time: 上午10:14
// */
//public class XmlUtil {
//
//    private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
//
//  /*  private  static  XmlUtil instance;
//
//    private XmlUtil() {}
//
//    public static XmlUtil getInstance() {
//        if (instance == null) {
//            return new XmlUtil();
//        }
//        return instance;
//    }*/
//
//    /**
//     * 根据document和xml路径，把document写入xml，并且指定xml编码为UTF-8
//     * @param document 文档
//     * @param xmlName 文件名称
//     */
//    public static void createXmlFile(Document document,String xmlName) {
//        String xmlLocation = getFileLocation(xmlName);
//        OutputFormat format = OutputFormat.createPrettyPrint();
//        format.setEncoding("UTF-8");    // 指定XML编码
//        try {
//            /*Document document = DocumentHelper.createDocument();  //创建文档
//            Element employees=document.addElement("employees");
//            Element employee=employees.addElement("employee");
//            Element name= employee.addElement("name");   //添加子节点
//            name.setText("汉语测试"); //添加Text值；例：<a>abc</a>
//            name.setAttributeValue("sa", "sa"); //添加属性；例：<a item="item"></a>
//            Element sex=employee.addElement("sex");
//            sex.setText("m");
//            Element age=employee.addElement("age");
//            age.setText("29");*/
//
//            XMLWriter writer = new XMLWriter(new FileWriter(xmlLocation), format);
//            writer.write(document);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("error", e);
//        }
//
//    }
//
//    /**
//     * 根据xml文件名称读取xml文档
//     * @param xmlFileName xml文件名称
//     * @return 文档
//     */
//    public static Document parseXmlFile(String xmlFileName) {
//        String fileLocation = getFileLocation(xmlFileName);
//        SAXReader saxReader = new SAXReader();
//        Document document = null;
//        try {
//            document = saxReader.read(new File(fileLocation));
//        } catch (DocumentException e) {
//            logger.error("error", e);
//        }
//        return  document;
//    }
//
//    /**
//     * 根据xml文件名称及节点路径查询符合条件的节点集合
//     * @param xmlFileName xml文件名称
//     * @param elementPath 节点路径名称
//     */
//    public static List<Element> searchElementList(String xmlFileName, String elementPath) {
//        Document document = parseXmlFile(xmlFileName);
//        List<Element> elements = (List<Element>)document.selectNodes(elementPath);
//        return elements;
//    }
//
//    /**
//     * 根据xml文件名称及节点路径查询符合条件的单一节点
//     * @param xmlFileName xml文件名称
//     * @param elementPath 节点路径名称
//     */
//    public static Element searchSingleElement(String xmlFileName, String elementPath) {
//        Document document = parseXmlFile(xmlFileName);
//        Element element = (Element)document.selectSingleNode(elementPath);
//        return element;
//    }
//
//    /**
//     * 更新节点的文本
//     * @param xmlFileName xml文件名称
//     * @param elementPath 节点路径
//     * @param newText 更新后的文本
//     */
//    public static void updateXmlElementText(String xmlFileName, String elementPath, String newText) {
//        Document document = parseXmlFile(xmlFileName);
//        Element element = (Element)document.selectSingleNode(elementPath);
//        Assert.assertNotNull(element);
//        element.setText(newText);
//        createXmlFile(document, xmlFileName);
//    }
//
//    /**
//     * 更新节点属性
//     * @param xmlFileName xml文件名称
//     * @param elementPath 节点路径
//     * @param attrName 被更新的属性名称
//     * @param newAttrValue 更新后属性的值
//     */
//    public static void updateXmlElementAttrValue(String xmlFileName, String elementPath, String attrName, String newAttrValue) {
//        Document document = parseXmlFile(xmlFileName);
//        Element element = (Element)document.selectSingleNode(elementPath);
//        Assert.assertNotNull(element);
//        for(Iterator it = element.attributeIterator(); it.hasNext();){
//            Attribute attribute = (Attribute) it.next();
//            if (attrName.equals(attribute.getName())) {
//                attribute.setText(newAttrValue);
//                break;
//            }
//        }
//        createXmlFile(document, xmlFileName);
//    }
//
//    /**
//     * 更新属性的名称
//     * @param xmlFileName xml文件名称
//     * @param elementPath 节点路径
//     * @param oldAttrName  更新前属性名称
//     * @param newAttrName 更新后属性名称
//     */
//    public static void updateXmlElementAttrName(String xmlFileName, String elementPath, String oldAttrName, String newAttrName) {
//        Document document = parseXmlFile(xmlFileName);
//        Element element = (Element)document.selectSingleNode(elementPath);
//        Assert.assertNotNull(element);
//        for(Iterator it = element.attributeIterator(); it.hasNext();){
//            Attribute attribute = (Attribute) it.next();
//            if (oldAttrName.equals(attribute.getName())) {
//                attribute.setName(newAttrName);
//                break;
//            }
//        }
//        createXmlFile(document, xmlFileName);
//    }
//
//    /**
//     * 删除节点
//     * @param xmlFileName 文件名称
//     * @param elementPath 节点路径
//     */
//    public static void deleteXmlElement(String xmlFileName, String elementPath) {
//        Document document = parseXmlFile(xmlFileName);
//        Element element = (Element)document.selectSingleNode(elementPath);
//        Assert.assertNotNull(element);
//        Element parentElement = element.getParent();
//        if (parentElement == null) {//删除根节点
//            document.remove(element);
//        } else {
//            parentElement.remove(element);
//        }
//        createXmlFile(document, xmlFileName);
//    }
//
//    /**
//     * 获取文件的本地路径
//     * @param xmlFileName 文件名称
//     * @return 本地路径
//     */
//    public static String getFileLocation(String xmlFileName) {
//        URL url = Thread.currentThread().getContextClassLoader().getResource(xmlFileName);
//        Assert.assertNotNull(url);
//        String urlStr =  url.toString();
//        urlStr = urlStr.replace("file:/", "");
//        urlStr = urlStr.replace("/", "\\");
//        return urlStr;
//    }
//
//
//}
