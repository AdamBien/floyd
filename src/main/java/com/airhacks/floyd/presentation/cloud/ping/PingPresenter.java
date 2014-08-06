package com.airhacks.floyd.presentation.cloud.ping;

import com.airhacks.floyd.business.monitor.boundary.PingService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
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
    NumberAxis memoryNumberAxis;

    @FXML
    NumberAxis loadNumberAxis;

    @FXML
    BarChart loadAverageChart;

    @FXML
    BarChart memoryChart;

    private XYChart.Series<String, Number> memorySeries;
    private XYChart.Series<String, Number> loadSeries;
    private long counter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.memorySeries = new XYChart.Series<>();
        this.loadSeries = new XYChart.Series<>();
        memoryChart.getData().add(memorySeries);
        loadAverageChart.getData().add(loadSeries);
        refresh();
    }

    public void refresh() {
        this.refreshMemory();
        this.refreshLoadAverage();
        counter++;
    }

    public void refreshLoadAverage() {
        XYChart.Data<String, Number> point = new XYChart.Data<>();
        point.setXValue(String.valueOf(counter));
        Runnable doneListener = () -> {
            Platform.runLater(() -> loadSeries.getData().add(point));
        };
        service.askForOSInfo("http://" + uri, point::setYValue, errorSink::setText, doneListener);

    }

    public void refreshMemory() {
        XYChart.Data<String, Number> point = new XYChart.Data<>();
        point.setXValue(String.valueOf(counter));
        Runnable doneListener = () -> {
            Platform.runLater(() -> memorySeries.getData().add(point));
        };
        service.askForMemory("http://" + uri, memoryNumberAxis.upperBoundProperty()::set, point::setYValue, errorSink::setText, doneListener);

    }

}
