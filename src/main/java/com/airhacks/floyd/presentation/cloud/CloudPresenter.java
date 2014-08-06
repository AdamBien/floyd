package com.airhacks.floyd.presentation.cloud;

import com.airhacks.floyd.business.discovery.boundary.PingScanner;
import com.airhacks.floyd.presentation.cloud.ping.PingView;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
public class CloudPresenter implements Initializable {

    @Inject
    PingScanner ps;

    @FXML
    TabPane activePings;

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
        uri.forEach(this::add);
    }

    public void add(String uri) {
        Tab tab = new Tab(uri);
        Map<String, Object> context = new HashMap<>();
        context.put("uri", uri);
        PingView view = new PingView(context::get);
        view.getViewAsync(tab::setContent);
        activePings.getTabs().add(tab);
    }

}
