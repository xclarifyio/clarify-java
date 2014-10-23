package io.clarify.api;

import java.io.IOException;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;

/**
 * Represents a List Bundles Clarify API response type, allowing the caller to obtain the raw list of links
 * and to paginate through the results.  
 * 
 */
public class BundleList extends ClarifyPaginatedModel {
    public BundleList(ClarifyClient client, ClarifyResponse response) {
        super(client,response);
    }
    
    /**
     * Attempt to request the next page using the next link from the current page
     * @return a BundleList representing the next page, or null if there are no other pages
     * @throws JSONException if the href for the next link could not be obtained for some reason
     * @throws IOException if a network failure occurred while fetching the next page
     */
    public BundleList nextPage() throws JSONException, IOException {
        if(!hasNextPage()) {
            return null;
        }
        
        JSONObject nextLink = nextLink();
        if(nextLink != null) {
            String href = (String)nextLink.get("href");
            JSONResource jsonResource = 
                    client.json(client.buildPathFromHref(href));
            ClarifyResponse resp = new ClarifyResponse(jsonResource);
            BundleList list = new BundleList(client, resp);
            return list;
        }
        return null;
    }
}
