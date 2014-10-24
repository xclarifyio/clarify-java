package io.clarify.api;

/**
 * Represents a specific Track within a media Bundle
 *
 */
public class BundleTrack extends ClarifyModel {
    public BundleTrack(ClarifyClient client, ClarifyResponse response) {
        super(client,response);
    }
    
    /**
     * Attempts to navigate the parsed JSON and return the field "id" in the payload
     * @return return the Bundle ID
     * @throws RuntimeException containing the nested exception if the lookup failed
     */
    public String getId() {
        try {
            return (String)response.getJSONResource().get("id");
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

}
