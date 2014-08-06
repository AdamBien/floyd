package com.airhacks.floyd.presentation.cloud.ping;

import com.airhacks.floyd.business.monitor.boundary.PingService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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

    private XYChart.Series<String, Number> series;
    private long counter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.series = new XYChart.Series<>();
        memoryChart.getData().add(series);
        refresh();
    }

    public void refresh() {
        XYChart.Data<String, Number> point = new XYChart.Data<>();
        point.setXValue(String.valueOf(counter));
        Runnable doneListener = () -> {
            Platform.runLater(() -> series.getData().add(point));
        };
        service.askForMemory("http://" + uri, number.upperBoundProperty()::set, point::setYValue, errorSink::setText, doneListener);
        counter++;

    }

}
