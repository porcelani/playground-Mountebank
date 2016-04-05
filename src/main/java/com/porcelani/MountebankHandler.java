package com.porcelani;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;


public class MountebankHandler {


    private static final String MOUNTEBACK_SCHEME = "http";

    private static final String MOUNTEBACK_IMPOSTERS_PATH = "/imposters";

    private static final String MOUNTEBANK_TEST_PATH = "/test";

    private static final int MOUNTEBACK_PORT = 2525;

    private CloseableHttpClient mountebackHttpClient;

    private ObjectMapper mapper;

    private String mountebankHost = null;

    public MountebankHandler(String mountebankHost) {
        this.mountebankHost = mountebankHost;
        mountebackHttpClient = HttpClients.createDefault();
        mapper = new ObjectMapper();
    }

    public void deleteImposter(int port) throws Exception {
        URI uri = prepareImposterURIWithPort(port);
        HttpDelete httpDelete = new HttpDelete(uri);
        mountebackHttpClient.execute(httpDelete);
    }

    public MountebankImposter getImposter(int port) throws Exception {
        URI uri = prepareImposterURIWithPort(port);
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse response = mountebackHttpClient.execute(httpGet);
        MountebankImposter imposter = parseImposterFromResponse(response);

        return imposter;
    }

    private MountebankImposter parseImposterFromResponse(HttpResponse response)
        throws IOException, JsonParseException, JsonMappingException {
        MountebankImposter imposter = mapper.readValue(EntityUtils.toString(response.getEntity()),
            MountebankImposter.class);
        return imposter;
    }

    private URI prepareImposterURIWithPort(int port) {
        try {
            URI uri = new URIBuilder().setScheme(MOUNTEBACK_SCHEME).setHost(mountebankHost)
                .setPath(MOUNTEBACK_IMPOSTERS_PATH + "/" + port).setPort(MOUNTEBACK_PORT).build();
            return uri;

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public URI prepareImposterTestURI(int port) {
        try {
            URI uri = new URIBuilder().setScheme(MOUNTEBACK_SCHEME).setHost(mountebankHost)
                .setPath(MOUNTEBANK_TEST_PATH + "/" + port).setPort(port).build();
            return uri;

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void postImposter(MountebankImposter imposter) throws Exception {
        URI uri = prepareImposterURI();
        HttpEntity imposterEntity = preparePostEntity(imposter);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(imposterEntity);
        mountebackHttpClient.execute(httpPost);
    }

    private HttpEntity preparePostEntity(MountebankImposter imposter)
        throws UnsupportedEncodingException, JsonProcessingException {
        StringEntity imposterEntity = new StringEntity(mapper.writeValueAsString(imposter));
        imposterEntity.setContentType("application/json");
        return imposterEntity;
    }

    public MountebankImposter parseImposterFromConfigFile(String imposterDefinitionFilename)
        throws JsonParseException, JsonMappingException, IOException {
        return this.mapper.readValue(ClassLoader.getSystemResource(imposterDefinitionFilename),
            MountebankImposter.class);
    }

    public URI prepareImposterURI() {
        try {
            URI uri = new URIBuilder().setScheme(MOUNTEBACK_SCHEME).setHost(mountebankHost)
                .setPath(MOUNTEBACK_IMPOSTERS_PATH).setPort(MOUNTEBACK_PORT).build();
            return uri;
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }

    }

    public void close() throws IOException {
        if (mountebackHttpClient != null) {
            mountebackHttpClient.close();
        }
    }

}