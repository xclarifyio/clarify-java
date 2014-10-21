package io.clarify.api;

public class ClarifyModel {
    public ClarifyModel(ClarifyClient client, ClarifyResponse response) {
        this.client = client;
        this.response = response;
    }

    public ClarifyResponse getClarifyResponse() {
        return response;
    }
    
    public ClarifyClient getClarifyClient() {
        return client;
    }

    public String toString() {
        if(response == null) {
            return super.toString();
        } else {
            return response.getResponseContent();
        }
    }
    
    protected ClarifyClient client;
    protected ClarifyResponse response;

}
