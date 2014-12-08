package io.clarify.api;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Bundle extends ClarifyModel {
    public Bundle(ClarifyClient client, ClarifyResponse response) {
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
    
    /**
     * Deletes this bundle and all of its related Metadata and Tracks, along with media stored on Clarify systems. 
     * Does not delete any media stored on remote systems.
     * 
     * USE CAUTION AS THIS CALL CANNOT BE UNDONE
     * @return boolean
     * @throws IOException if an error occurred during the delete bundle API call
     */
    public boolean delete() throws IOException {
        return client.deleteBundle(getId());
    }
    
    /**
     * Adds a new Track to the Bundle with the given media URI
     * @param uri the URI of the remote media file to add to the Bundle
     * @return a new Track instance containing the details about the new Track
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleTrack addTrack(URI uri) throws IOException {
        return client.addTrackToBundle(getId(), uri);
    }

    /**
     * Returns the list of Tracks associated to this media Bundle
     * @return a BundleTrackList with the list of tracks and related details
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleTrackList listTracks() throws IOException {
        return client.listTracksForBundle(getId());
    }

    /**
     * Returns a specific Track by track number for this media Bundle
     * @param trackId the GUID of the Track
     * @return the requested Track (throws an IOException if a 404 NOT FOUND is returned)
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleTrack findTrack(String trackId) throws IOException {
        return client.findTrackForBundle(getId(), trackId);
    }
    
    /**
     * Deletes a track from this bundle.  This will only delete media stored on Clarify systems 
     * and not delete the source media on remote systems.
     * 
     * USE CAUTION AS THIS CALL CANNOT BE UNDONE
     * 
     * @param trackId the GUID of the Track to delete
     * @return boolean
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public boolean deleteTrack(String trackId) throws IOException {
        return client.deleteTrack(getId(), trackId);
    }
    
    /**
     * Returns this media Bundle's Metadata class, with details on the bundle and any 
     * attached user data (if available)
     * @return a Metadata instance for the media bundle
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleMetadata getMetadata() throws IOException {
        return client.findMetadata(getId());
    }
    
}
