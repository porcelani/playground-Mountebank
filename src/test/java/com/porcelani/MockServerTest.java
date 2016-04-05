package com.porcelani;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;

public class MockServerTest {
    private static final String MOUNTEBANK_HOST = "localhost";
    private MountebankHandler mountebankHandler;
    private MountebankImposter mountebankImposter;

    @Before
    public void setUp() {
        RestAssured.registerParser("text/plain", Parser.JSON);

        mountebankHandler = new MountebankHandler(MOUNTEBANK_HOST);
        mountebankImposter = new MountebankImposter();
        mountebankImposter.setPort(9080);
        mountebankImposter.setProtocol("http");
    }

    @After
    public void finalize() throws Exception {
        mountebankHandler.deleteImposter(9080);
    }

    @Test
    public void should_teste() throws Exception {
        mountebankHandler.postImposter(mountebankImposter);

        get("http://localhost:9080")
            .then()
            .assertThat()
            .statusCode(200);
    }
}
