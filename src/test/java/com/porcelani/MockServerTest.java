package com.porcelani;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.post;

public class MockServerTest {
    public static final String IMPOSTER_FILENAME = "simpleConfig.json";
    private MountebankHandler mountebankHandler;

    @Before
    public void setUp() {
        RestAssured.registerParser("text/plain", Parser.JSON);
        mountebankHandler = new MountebankHandler();
    }

    @Test
    public void should_teste() throws Exception {
        MountebankImposter  mountebankImposter = new MountebankImposter();
        mountebankImposter.setPort(9080);
        mountebankImposter.setProtocol("http");

        mountebankHandler.postImposter(mountebankImposter);

        get("http://localhost:9080")
            .then()
            .assertThat()
            .statusCode(200);

        mountebankHandler.deleteImposter(9080);
    }

    @Test
    public void should_test_with_json_configuration() throws Exception {
        MountebankImposter mountebankImposter = mountebankHandler.parseImposterFromConfigFile(IMPOSTER_FILENAME);

        mountebankHandler.postImposter(mountebankImposter);

        post("http://localhost:4545")
            .then()
            .assertThat()
            .statusCode(400);

        mountebankHandler.deleteImposter(4545);
    }
}
