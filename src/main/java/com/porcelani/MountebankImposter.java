package com.porcelani;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MountebankImposter {
    private String name;
    private String protocol;
    private int port;
    List<Object> stubs;
    List<Map<String,Object>> requests;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Object> getStubs() {
        return stubs;
    }

    public void setStubs(List<Object> stubs) {
        this.stubs = stubs;
    }

    public List<Map<String, Object>> getRequests() {
        return requests;
    }

    public void setRequests(List<Map<String, Object>> requests) {
        this.requests = requests;
    }
}