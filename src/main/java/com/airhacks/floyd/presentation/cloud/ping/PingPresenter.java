package com.airhacks.floyd.presentation.cloud.ping;

import com.airhacks.floyd.business.monitor.boundary.PingService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class PingPresenter implements Initializable {

    @Inject
    String uri;

    @FXML
    Label errorSink;

    @Inject
    PingService service;

    @FXML
    NumberAxis number;

    @FXML
    CategoryAxis category;

    @FXML
    BarChart memoryChart;

    private XYChart.Data<String, Number> point;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        point = new XYChart.Data<>();
        point.setXValue("1");
        service.askForMemory("http://" + uri, number.upperBoundProperty()::set, point::setYValue, errorSink::setText);

        point.YValueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(point);
            Platform.runLater(() -> memoryChart.getData().add(series));
        });

    }

}
