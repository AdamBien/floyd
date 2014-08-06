package com.airhacks.floyd.presentation.cloud.ping;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    Label pingUri;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pingUri.setText(uri);
        System.out.println("Uri: " + uri);
    }

}
