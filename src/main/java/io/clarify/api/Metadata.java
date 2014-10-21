package io.clarify.api;

public class Metadata {

    public Metadata(ClarifyClient client) {
        // save the client for later API calls from within this class
        this.client = client;
    }

    public void reset() {
	// TODO: make the API call to delete the metadata
    }

    private ClarifyClient client;
}
