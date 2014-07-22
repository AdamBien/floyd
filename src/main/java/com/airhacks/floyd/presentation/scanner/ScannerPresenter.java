package com.airhacks.floyd.presentation.scanner;

import com.airhacks.floyd.business.discovery.boundary.PingScanner;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
    ListView<String> results;

    @FXML
    Button scan;

    @Inject
    PingScanner ps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.scan.disableProperty().
                bind(host.textProperty().isEmpty().
                        or(isNotNumber(from.textProperty())).
                        or(isNotNumber(to.textProperty())));
    }

    public void scan() {
        int portFrom = Integer.parseInt(from.getText());
        int portTo = Integer.parseInt(to.getText());
        ps.activePings(host.getText(), portFrom, portTo);
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
