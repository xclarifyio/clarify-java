package io.clarify.api;

import java.io.IOException;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;

/**
 * Represents a Search Bundles Clarify API response type, allowing the caller to obtain the matches, terms, and scores
 * from the results, the list of search result items, and paginate through the results.  
 *
 */
public class BundleSearchResults extends ClarifyPaginatedModel {
    public BundleSearchResults(ClarifyClient client, ClarifyResponse response) {
        super(client,response);
    }

    /**
     * Returns the item_results array from the search results that provides term results, scores, and matches
     * @return a us.monoid.json.JSONArray containing all items under the item_results field in the response
     */
    public JSONArray getItemResults() {
        try {
            return (JSONArray)response.getJSONValue("item_results");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Attempt to request the next page using the next link from the current page
     * @return a BundleSearchResults representing the next page, or null if there are no other pages
     * @throws JSONException if the href for the next link could not be obtained for some reason
     * @throws IOException if a network failure occurred while fetching the next page
     */
    public BundleSearchResults nextPage() throws JSONException, IOException {
        if(!hasNextPage()) {
            return null;
        }
        
        JSONObject nextLink = nextLink();
        if(nextLink != null) {
            String href = (String)nextLink.get("href");
            JSONResource jsonResource = 
                    client.json(client.buildPathFromHref(href));
            ClarifyResponse resp = new ClarifyResponse(jsonResource);
            BundleSearchResults results = new BundleSearchResults(client, resp);
            return results;
        }
        return null;
    }

}
