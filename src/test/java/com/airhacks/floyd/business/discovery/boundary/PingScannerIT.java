package com.airhacks.floyd.business.discovery.boundary;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import org.junit.Assume;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class PingScannerIT {

    @Test
    public void initialize() {
        PingScanner cut = new PingScanner();
        cut.initClient();
    }

    @Test
    public void scan() {
        try {
            new URL("http://localhost:8080/ping").openConnection().getInputStream();
        } catch (IOException ex) {
            System.out.println("Ex: " + ex);
            Assume.assumeNoException("Deploy ping on port 8080 first", ex);
        }
        PingScanner cut = new PingScanner();
        cut.initClient();
        List<Integer> activePings = cut.activePings("localhost", 8000, 8080);
        assertThat(activePings, contains(8080));

    }

}
