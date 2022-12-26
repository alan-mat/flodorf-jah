module com.floridsdorf.flodorfjah {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.floridsdorf.jah to javafx.fxml;
    opens com.floridsdorf.jah.controller.viewControllers to javafx.fxml;
    exports com.floridsdorf.jah;
}