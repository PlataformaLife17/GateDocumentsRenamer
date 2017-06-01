/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ua.gatedocumentsrenamer.main;

import es.upv.xmlutils.XMLUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private String dirInput;
    private String dirOutput;
    private String attribute;

    public ProcessRename(String dirInput, String dirOutput, String attribute) {

        this.dirInput = dirInput;
        this.dirOutput = dirOutput;
        this.attribute = attribute;
    }

    public void renameFile() {
        String nombreDocumento;

        File directory = new File(dirInput);
        if (directory.isDirectory()) {

            for (File f : directory.listFiles()) {

                try {
                    String attDocument = getAttibuteValue(f);

                    nombreDocumento = "GATE_" + attDocument;

                    saveFile(f, nombreDocumento);

                } catch (IOException | ParserConfigurationException | SAXException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

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
        f.renameTo(file);
    }

}
