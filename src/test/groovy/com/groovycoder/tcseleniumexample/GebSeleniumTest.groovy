package com.groovycoder.tcseleniumexample

import geb.junit4.GebTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.images.builder.ImageFromDockerfile

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL
import static org.testcontainers.containers.Network.newNetwork

class GebSeleniumTest extends GebTest {

    @Rule
    public Network network = newNetwork() // shared network for all containers to use DNS

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(RECORD_ALL, new File("build")) // save video recordings
            .withNetwork(network) as BrowserWebDriverContainer

    @Rule
    public GenericContainer webserver = new GenericContainer(
            new ImageFromDockerfile()
                .withDockerfileFromBuilder({ builder ->
                    builder
                        .from("httpd:2.4.33-alpine")
                        .copy("public-html/", "/usr/local/apache2/htdocs/")
                })
                .withFileFromClasspath("public-html", "public-html"))
            .withNetwork(network)
            .withNetworkAliases("app")


    @Before
    void setUp() {
        browser.driver = chrome.webDriver
    }

    @Test
    void seleniumTest() {
        // go to index page
        go "http://app"
        assert title == "Testcontainers Test Page - Index"

        // navigate using link
        $("a").click()
        assert title == "Testcontainers Test Page - Second Page"

        // click on button and verify javascript alert box
        assert withAlert {
            $("input").click()
        } == "Bang!"
    }
}
