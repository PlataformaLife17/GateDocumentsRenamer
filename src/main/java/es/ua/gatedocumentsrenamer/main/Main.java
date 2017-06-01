/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ua.gatedocumentsrenamer.main;

import es.upv.xmlutils.XMLUtils;
import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Command;
import io.airlift.airline.Help;
import io.airlift.airline.Option;
import io.airlift.airline.ParseException;
import io.airlift.airline.ParseOptionMissingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author balmarcha
 */
public class Main {

    public static void main(String[] args) throws IOException, Exception {

        CliBuilder<Callable<Void>> builder = Cli.<Callable<Void>>builder("gatedocumentsrenamer")
                .withDescription("Aplicación de consola para renombrar los documentos que se guardan desde GATE")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, RenameGateDocument.class);

        Cli<Callable<Void>> cli = builder.build();

        try {

            Callable command = cli.parse(args);
            command.call();

        } catch (ParseOptionMissingException ex) {

            System.err.println(ex.getMessage() + ". Please, execute 'help' for more information.");

        } catch (ParseException | FileNotFoundException | UnsupportedEncodingException ex) {

            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    @Command(name = "gatedocumentsrenamer", description = "Renombrar documentos de GATE")
    public static class RenameGateDocument implements Callable<Void> {

        /**
         * Path to the directory where the corpus in gate format is.
         */
        @Option(name = {"-d", "--dir"}, description = "El directorio donde se encuentra todos los archivos de gate.", required = true)
        private String dir;

        /**
         * Attribute to rename the file.
         */
        @Option(name = {"-att", "--attribute"}, description = "Atricuto que se añadirá al nombre del documento (GATE_att)")
        private String att = "ID";

        /**
         * The output file in arff format.
         */
        @Option(name = {"-o", "--output"}, description = "Ruta al fichero de salida")
        private String output;

        @Override
        public Void call() throws Exception {

            if (dir != null && output == null) {
                output = dir;
            }
            
            ProcessRename pr = new ProcessRename(dir, output, att);
            pr.renameFile();
            
            return null;
        }
    }
}
