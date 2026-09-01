package com.portfolio.web.dto;

import com.portfolio.domain.TestEndpoint;

public class TestEndpointItem {

    private String method;
    private String url;
    private String description;

    public static TestEndpointItem from(TestEndpoint endpoint) {
        TestEndpointItem item = new TestEndpointItem();
        item.method = endpoint.getMethod();
        item.url = endpoint.getUrl();
        item.description = endpoint.getDescription();
        return item;
    }

    public boolean isBlank() {
        return isEmpty(method) && isEmpty(url) && isEmpty(description);
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
