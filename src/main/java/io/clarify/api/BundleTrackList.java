package io.clarify.api;

import java.io.IOException;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;

/**
 * Represents a Track List associated to a specific media Bundle. 
 */
public class BundleTrackList extends ClarifyPaginatedModel {
    public BundleTrackList(ClarifyClient client, ClarifyResponse response) {
        super(client,response);
    }

}
