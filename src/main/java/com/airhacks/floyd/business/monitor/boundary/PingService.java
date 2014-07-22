package com.airhacks.floyd.business.monitor.boundary;

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
import javafx.beans.property.StringProperty;
import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        this.client = ClientBuilder.newClient();
        System.out.println("Client created: " + this.client);
    }

    public void askForUptime(String serverUrl, StringProperty sink, StringProperty errorSink) {
        this.client.target(serverUrl).request().accept(MediaType.TEXT_PLAIN).async().get(new InvocationCallback<String>() {

            @Override
            public void completed(String rspns) {
                sink.set(rspns);
            }

            @Override
            public void failed(Throwable thrwbl) {
                errorSink.set(thrwbl.getMessage());
            }
        });
    }
}
