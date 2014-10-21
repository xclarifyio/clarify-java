package io.clarify.api;

import java.io.IOException;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;

/**
 * Represents a List Bundles Clarify API response type, allowing the caller to obtain the raw list of links
 * and to paginate through the results.  
 * 
 */
public class BundleList extends ClarifyModel {
    public BundleList(ClarifyClient client, ClarifyResponse response) {
        super(client,response);
    }
    
    /**
     * Returns a us.monoid.json.JSONObject for the _links element of the JSON response, allowing further exploration by the caller
     * @return a us.monoid.json.JSONObject for the _links element of the JSON response
     */
    public JSONObject getLinks() {
        return (JSONObject)response.getJSONValue("_links");
    }
    
    /**
     * Returns items within the _links element of the JSON response. Each array element is a us.monoid.json.JSONObject providing
     * access to the element
     * @return a us.monoid.json.JSONArray containing all items under the _links element in the response 
     */
    public JSONArray getLinkItems() {
        return (JSONArray)response.getJSONValue("_links.items");
    }
    
    /**
     * Determine if there is another page available, or if the returned response represents the last page
     * @return true if there is another page available for pagination
     */
    public boolean hasNextPage() {
        return (nextLink() != null);
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
    
    /**
     * 
     * @return the JSONObject for the next link under the _links, or null if not found
     */
    protected JSONObject nextLink() {
        try {
            JSONObject links = (JSONObject)response.getJSONValue("_links");
            return (JSONObject)links.get("next");
        } catch (JSONException e) {
            // a JSONException is thrown if not found
        }
        return null;
    }
}
