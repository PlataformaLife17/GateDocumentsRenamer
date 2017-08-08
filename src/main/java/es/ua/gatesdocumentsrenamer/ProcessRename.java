/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ua.gatesdocumentsrenamer;

import es.upv.xmlutils.XMLUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author balmarcha
 */
public class ProcessRename {

    private final String dirInput;
    private final String dirOutput;
    private final String attribute;
    private final boolean rename;

    public ProcessRename(String dirInput, String dirOutput, String attribute) {

        this.dirInput = dirInput;
        this.dirOutput = dirOutput;
        this.attribute = attribute;
        
        this.rename = (this.dirInput.equals(this.dirOutput));
    }

    public void renameFile() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String nombreDocumento;

        File directory = new File(dirInput);
        if (directory.isDirectory()) {

            for (File f : directory.listFiles()) {

                String attDocument = getAttibuteValue(f);
                nombreDocumento = "GATE_" + attDocument;
                saveFile(f, nombreDocumento);
            }
            
            if(directory.list().length == 0)
                directory.delete();
        }
    }
    //no hay nada más gallego que los pazos
    
    private String getAttibuteValue(File f) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {

        String id = "";
        boolean encontrado = false;

        Document doc = XMLUtils.loadXML(f.getAbsolutePath());

        Element root = doc.getDocumentElement();
        Element gdf = (Element) root.getElementsByTagName("GateDocumentFeatures").item(0);
        NodeList features = XMLUtils.getElementsNamed(gdf, "Feature");

        for (int i = 0; i < features.getLength() && !encontrado; i++) {

            Element feature = (Element) features.item(i);
            Element featureName = (Element) feature.getElementsByTagName("Name").item(0);

            if (attribute.equals(featureName.getTextContent())) {
                Element value = (Element) feature.getElementsByTagName("Value").item(0);
                id = value.getTextContent();
                encontrado = true;
            }
        }

        return id;
    }

    private void saveFile(File f, String nombreDocumento) {
        File finalDirectory = new File(dirOutput);
        if (!finalDirectory.exists()) {

            finalDirectory.mkdirs();
        }

        File file = new File(dirOutput + "/" + nombreDocumento);
        
        if(rename)
        {
            f.renameTo(file);
        }
    }

}
