package io.clarify.api;

public class Bundle {

    public Bundle(ClarifyClient client) {
	// save the client for later API calls from within this class
	this.client = client;
    }

    public Metadata getMetadata() {
	Metadata metadata = new Metadata(this.client);
	// TODO: use the client to make another API call, unless it was embedded already
	return metadata;
    }

    public void addTrack(Track track) {
    }

    public List<Track> listTracks() {
    }

    public void deleteTrack(int trackNum) {
	// TODO: Implement call and parse response
    }

    private ClarifyClient client;
}
