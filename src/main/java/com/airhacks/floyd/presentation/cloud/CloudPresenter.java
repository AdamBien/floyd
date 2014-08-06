package com.airhacks.floyd.presentation.cloud;

import com.airhacks.floyd.business.discovery.boundary.PingScanner;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class CloudPresenter implements Initializable {

    @Inject
    PingScanner ps;

    @FXML
    ListView activePings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> uriList = ps.getActivePings();
        uriList.addListener((ListChangeListener.Change<? extends String> c) -> {
            while (c.next()) {
                openPingViews((List<String>) c.getAddedSubList());
            }
        });
    }

    void openPingViews(List<String> uri) {
        uri.forEach(activePings.getItems()::add);
    }
}
