package es.ua.gatesdocumentsrenamer.main;

import es.ua.gatesdocumentsrenamer.ProcessRename;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class HomeController implements Initializable {
    
    @FXML
    private Button info_button;

    @FXML
    private TextField direct_selected;
    
    @FXML
    private TextField feature;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        createTooltip();
    }

    private void createTooltip() {
        
        Image image = new Image(getClass().getResourceAsStream("/img/info_white_icon.png")) {};
        info_button.setGraphic(new ImageView(image));

        Tooltip tt = new Tooltip();
        tt.setText("The final name of file" + "\n" + "will be GATE_feature-value");
        info_button.setTooltip(tt);
    }

    public void selectDirectory() {
        
        Stage stage = new Stage();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Gate's files");
        File selectedDirectory = chooser.showDialog(stage);

        if (selectedDirectory != null) {
            direct_selected.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void rename() {
        
        ProcessRename pr = new ProcessRename(direct_selected.getText(), direct_selected.getText(), feature.getText());
        try {
            
            pr.renameFile();
            modalDialogCorrected();
        }
        catch (IOException | ParserConfigurationException | SAXException ex) {
            
            modalDialogInCorrected();
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exit() {
        
        System.exit(0);
    }
    
    private void modalDialogCorrected() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Rename documents");
        alert.setResult(ButtonType.OK);

        alert.showAndWait();
    }
    
    private void modalDialogInCorrected() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText("There was an error in the rename");
        alert.setResult(ButtonType.OK);

        alert.showAndWait();
    }
}
