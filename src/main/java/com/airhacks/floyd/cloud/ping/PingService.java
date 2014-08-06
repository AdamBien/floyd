package com.airhacks.floyd.cloud.ping;

/*
 * #%L
 * igniter
 * %%
 * Copyright (C) 2013 Adam Bien
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author adam-bien.com
 */
public class PingService {

    private Client client;
    private static final String START_TIME = "/resources/health/start-time";
    private static final String MEMORY = "/resources/health/current-memory";
    private static final String OS = "/resources/health/os-info";

    @PostConstruct
    public void init() {
        this.client = ClientBuilder.newClient();
        System.out.println("Client created: " + this.client);
    }

    public void askForUptime(String pingUri, Consumer<String> sink, Consumer<String> errorSink, Runnable doneListener) {
        this.client.target(pingUri).path(START_TIME).request().accept(MediaType.TEXT_PLAIN).async().get(new InvocationCallback<String>() {

            @Override
            public void completed(String rspns) {
                sink.accept(rspns);
                doneListener.run();
            }

            @Override
            public void failed(Throwable thrwbl) {
                errorSink.accept(thrwbl.getMessage());
            }
        });
    }

    public void askForMemory(String pingUri, Consumer<Double> availableProperty, Consumer<Double> usedProperty, Consumer<String> errorSink, Runnable doneListener) {
        this.client.target(pingUri).path(MEMORY).request().accept(MediaType.APPLICATION_JSON).async().get(new InvocationCallback<JsonObject>() {

            @Override
            public void completed(JsonObject rspns) {
                System.out.println("Response: " + rspns);
                double available = rspns.getJsonNumber("Available memory in mb").doubleValue();
                double used = rspns.getJsonNumber("Used memory in mb").doubleValue();
                availableProperty.accept(available);
                usedProperty.accept(used);
                doneListener.run();
            }

            @Override
            public void failed(Throwable thrwbl) {
                errorSink.accept(thrwbl.getMessage());
            }
        });
    }

    public void askForOSInfo(String pingUri, Consumer<Double> loadAverage, Consumer<Integer> numberOfCores, Consumer<String> errorSink, Runnable doneListener) {
        this.client.target(pingUri).path(OS).request().accept(MediaType.APPLICATION_JSON).async().get(new InvocationCallback<JsonObject>() {

            @Override
            public void completed(JsonObject rspns) {
                System.out.println("Response: " + rspns);
                double load = rspns.getJsonNumber("System Load Average").doubleValue();
                int cores = rspns.getJsonNumber("Available CPUs").intValue();
                loadAverage.accept(load);
                numberOfCores.accept(cores);
                doneListener.run();
            }

            @Override
            public void failed(Throwable thrwbl) {
                errorSink.accept(thrwbl.getMessage());
            }
        });
    }

}
