package com.scan2doc.imagetodocassistant;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.scan2doc.processor.WordIntegration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button processButton;

    @FXML
    private Button selectImagesButton;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtFecha;

    @FXML
    private VBox imageContainer;

    @FXML
    private TextArea txtConclusion;

    List<File> selectedFiles;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        processButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                WordIntegration wordIntegration = new WordIntegration();

                String conclusions = txtConclusion.getText();
                String patientData = "Nombre: " + txtNombre.getText() + "\n"
                                     + "Id: " + txtId.getText() + "\n"
                                     + "Edad: " + txtEdad.getText() + "\n"
                                     + "Fecha: " + txtFecha.getText();

                String outputPath = txtNombre.getText() + "-";
                try {
                    outputPath += getDateWithoutTimeUsingFormat() + ".docx";
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                wordIntegration.createWordDocument(outputPath, selectedFiles, conclusions, patientData);

            }
        });

        selectImagesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Images");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
                );

                selectedFiles = fileChooser.showOpenMultipleDialog(selectImagesButton.getScene().getWindow());
                if (selectedFiles != null && !selectedFiles.isEmpty()) {
                    displayImages(selectedFiles);
                }
            }
        });
    }
    private void displayImages(List<File> imageFiles) {
        imageContainer.getChildren().clear();
        for (File file : imageFiles) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);  // Or your desired width
            imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);
        }
    }

    public static String getDateWithoutTimeUsingFormat()
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd-MM-yyyy");
        return formatter.format(new Date());
    }
}