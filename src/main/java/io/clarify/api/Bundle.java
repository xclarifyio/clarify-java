package io.clarify.api;

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
    
    public Metadata getMetadata() {
        Metadata metadata = new Metadata(this.client);
        // TODO: use the client to make another API call, unless it was embedded already
        return metadata;
    }

    public Track addTrack() {
        // TODO: Implement call and parse response
        Track track = new Track();
        return track;
    }

    public List<Track> listTracks() {
        // TODO: Implement call and parse response
        ArrayList<Track> list = new ArrayList<Track>();
        return list;
    }

    public void deleteTrack(int trackNum) {
        // TODO: Implement call and parse response
    }
    
}
