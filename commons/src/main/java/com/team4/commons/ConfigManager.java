package com.team4.commons;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class ConfigManager {

    private static Hashtable<String, String> configurationTable = null;

    private ConfigManager() {
        configurationTable = new Hashtable<>();
        String configurationFile = "configuration.xml";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configurationFile);
        try {
            Document configuration = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            NodeList properties = configuration.getElementsByTagName("property");
            for(int i = 0; i < properties.getLength(); i++) {
                Element property = (Element) properties.item(i);
                String config = property.getAttribute("name");
                String value = property.getAttribute("value");
                configurationTable.put(config, value);
            }
        } catch(IOException ioe) {
            throw new RobotException("Cannot read configuration file.");
        } catch(ParserConfigurationException pe) {
            throw new RobotException("ERROR: DocumentBuilder cannot be created.");
        } catch(SAXException se) {
            throw new RobotException("Parse error in configuration file.");
        } catch (IllegalArgumentException iae) {
            throw new RobotException("ERROR: Check if the system configuration file " + configurationFile + " exists.");
        }
    }

    public static void initializeSystemConfiguration() {
        if(configurationTable == null) {
            synchronized (ConfigManager.class) {
                if(configurationTable == null) {
                    new ConfigManager();
                }
            }
        }
    }

    public static String getConfiguration(String config) {
        initializeSystemConfiguration();
        return configurationTable.get(config);
    }

}
