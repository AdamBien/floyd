package com.airhacks.floyd.presentation.scanner;

import com.airhacks.floyd.business.discovery.boundary.PingScanner;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class ScannerPresenter implements Initializable {

    @FXML
    TextField host;
    @FXML
    TextField from;
    @FXML
    TextField to;

    @FXML
    Button scan;

    @FXML
    ProgressIndicator progress;

    @Inject
    PingScanner ps;

    private BooleanProperty scanInProgress;

    private ExecutorService threadPool;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.threadPool = Executors.newCachedThreadPool();
        this.scanInProgress = new SimpleBooleanProperty(false);
        progress.visibleProperty().bind(scanInProgress);
        bindScanValidations();
    }

    void bindScanValidations() {
        this.scan.disableProperty().
                bind(scanInProgress.or(host.textProperty().isEmpty().
                                or(isNotNumber(from.textProperty())).
                                or(isNotNumber(to.textProperty()))));
    }

    public void scan() {
        int portFrom = Integer.parseInt(from.getText());
        int portTo = Integer.parseInt(to.getText());
        scanInProgress.set(true);
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                ps.scanForPings(host.getText(), portFrom, portTo);
                perform(() -> scanInProgress.set(false));
                return null;
            }
        };
        threadPool.submit(task);

    }

    void perform(Runnable run) {
        Platform.runLater(run);
    }

    private ReadOnlyBooleanProperty isNotNumber(StringProperty textProperty) {
        SimpleBooleanProperty property = new SimpleBooleanProperty();
        textProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                String content = textProperty.get();
                Integer.parseInt(content);
                property.setValue(false);
            } catch (NumberFormatException nbr) {
                property.setValue(true);
            }
        });
        return property;
    }

}
