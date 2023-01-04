module com.floridsdorf.jah {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires lombok;

    opens com.floridsdorf.jah to javafx.fxml;
    opens com.floridsdorf.jah.controller.viewControllers to javafx.fxml;
    exports com.floridsdorf.jah;
}