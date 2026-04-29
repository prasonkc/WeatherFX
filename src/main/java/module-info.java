module app.weather.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.net.http;
    requires org.json;

    opens app.weather to javafx.fxml;
    opens app.weather.controller to javafx.fxml;
    exports app.weather;
}