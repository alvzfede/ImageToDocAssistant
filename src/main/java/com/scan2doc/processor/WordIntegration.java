package com.scan2doc.processor;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WordIntegration {

    public void createWordDocument(String outputPath, List<File> images, String conclusions, String patientData) {
        try (XWPFDocument document = new XWPFDocument()) {

            // Agregar datos del paciente
            XWPFParagraph patientParagraph = document.createParagraph();
            XWPFRun patientRun = patientParagraph.createRun();
            patientRun.setText(patientData);

            // Agregar imágenes redimensionadas
            for (File imageFile : images) {
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    int imageType = getImageType(imageFile.getName());
                    XWPFParagraph paragraph = document.createParagraph();
                    XWPFRun run = paragraph.createRun();
                    run.addPicture(fis, imageType, imageFile.getName(), Units.toEMU(200), Units.toEMU(200));  // Tamaño de la imagen
                }
            }

            // Agregar conclusiones
            XWPFParagraph conclusionsParagraph = document.createParagraph();
            XWPFRun conclusionsRun = conclusionsParagraph.createRun();
            conclusionsRun.setText(conclusions);

            // Guardar el documento
            try (FileOutputStream out = new FileOutputStream(new File(outputPath))) {
                document.write(out);
            }

        } catch (IOException | org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para determinar el tipo de imagen
    private int getImageType(String imageName) throws IllegalArgumentException {
        if (imageName.endsWith(".emf")) return XWPFDocument.PICTURE_TYPE_EMF;
        else if (imageName.endsWith(".wmf")) return XWPFDocument.PICTURE_TYPE_WMF;
        else if (imageName.endsWith(".pict")) return XWPFDocument.PICTURE_TYPE_PICT;
        else if (imageName.endsWith(".jpeg") || imageName.endsWith(".jpg")) return XWPFDocument.PICTURE_TYPE_JPEG;
        else if (imageName.endsWith(".png")) return XWPFDocument.PICTURE_TYPE_PNG;
        else if (imageName.endsWith(".dib")) return XWPFDocument.PICTURE_TYPE_DIB;
        else if (imageName.endsWith(".gif")) return XWPFDocument.PICTURE_TYPE_GIF;
        else if (imageName.endsWith(".tiff")) return XWPFDocument.PICTURE_TYPE_TIFF;
        else if (imageName.endsWith(".eps")) return XWPFDocument.PICTURE_TYPE_EPS;
        else if (imageName.endsWith(".bmp")) return XWPFDocument.PICTURE_TYPE_BMP;
        else if (imageName.endsWith(".wpg")) return XWPFDocument.PICTURE_TYPE_WPG;
        else {
            throw new IllegalArgumentException("Unsupported image type: " + imageName);
        }
    }
}
