module com.scan2doc.imagetodocassistant {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;

    opens com.scan2doc.imagetodocassistant to javafx.fxml;
    exports com.scan2doc.imagetodocassistant;
}