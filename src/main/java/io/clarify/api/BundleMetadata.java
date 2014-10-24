package io.clarify.api;

import java.io.IOException;

/**
 * Provides details on the stored Metadata for a specific media Bundle. Metadata may also contain user data within
 * the data field as any JSON payload
 * 
 */
public class BundleMetadata extends ClarifyModel {
    public BundleMetadata(ClarifyClient client, ClarifyResponse response) {
        super(client,response);
        this.client = client;
    }

    /**
     * Delete the metadata of a bundle and set data to {} (empty object.) 
     * This is functionally equivalent to an update metadata request with data set to {}.
     * @return true if the operation succeeded
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public boolean resetData() throws IOException {
        this.client.resetMetadata(getBundleId());
        return true;
    }

    /**
     * Updates the user-defined data property of the Bundle's Metadata with the supplied JSON string, 
     * then return a refreshed copy (resulting in 2 API calls)
     * @param json a String containing valid JSON, or null. If null is passed, then the data is reset to a JSON equiv of {}
     * @return a refreshed Metadata instance for the media bundle
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleMetadata updateData(String json) throws IOException {
        return client.updateMetadata(getBundleId(), json);
    }
    
    /*
     * Helper to return the bundle_id from the JSON payload
     */
    public String getBundleId() {
        return (String)response.getJSONValue("bundle_id");
    }

    protected ClarifyClient client;
}
