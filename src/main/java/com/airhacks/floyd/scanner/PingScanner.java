package com.airhacks.floyd.scanner;

import java.util.function.Consumer;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
public class PingScanner {

    private Client client;
    private WebTarget pingTarget;
    private ObservableList<String> validUris;

    @PostConstruct
    public void initClient() {
        this.validUris = FXCollections.observableArrayList();
        this.client = ClientBuilder.newClient();
        this.pingTarget = this.client.target("http://{host}:{port}/ping");
    }

    void scanForPings(Consumer<Integer> portConsumer, String host, int portFrom, int portTo) {
        this.validUris.clear();
        IntStream.range(portFrom, portTo).
                parallel().
                filter(p -> test(host, p)).
                forEach(portConsumer::accept);
    }

    public void scanForPings(String host, int portFrom, int portTo) {
        this.scanForPings(p -> add(host, p), host, portFrom, portTo);
    }

    void add(String host, int port) {
        Platform.runLater(() -> validUris.add(buildUri(host, port)));
    }

    public ObservableList<String> getActivePings() {
        return this.validUris;
    }

    public boolean test(String host, int port) {
        try {
            Response response = this.pingTarget.resolveTemplate("host", host).
                    resolveTemplate("port", port).request().get();
            return response.getStatus() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    static String buildUri(String host, int port) {
        return host + ":" + port + "/ping";
    }
}
