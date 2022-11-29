module com.floridsdorf.flodorfjah {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.floridsdorf.jah to javafx.fxml;
    exports com.floridsdorf.jah;
}