package io.clarify.api;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

/**
 * Base class for handling pagination within the Clarify Java SDK. Since the nextPage() method should be 
 * typed to the specific return class, no abstract method is provided for nextPage(). Instead, 
 * subclasses should provide the appropriate nextPage() implementation that returns 
 * the corresponding class type expected. 
 *
 */
public class ClarifyPaginatedModel extends ClarifyModel {

    public ClarifyPaginatedModel(ClarifyClient client, ClarifyResponse response) {
        super(client, response);
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