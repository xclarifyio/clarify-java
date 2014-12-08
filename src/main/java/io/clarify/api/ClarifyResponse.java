package io.clarify.api;

import java.io.IOException;

import us.monoid.web.JSONResource;

/**
 * Wraps all of the details of a low-level Clarify SDK response into a single class that can be used to:
 * 
 * 1) access the response code
 * 2) a parsed version of the content (Using Resty), and 
 * 3) the response content (for your own parsing needs).
 * 
 * This class depends on Resty, the underlying HTTP client used for calling the Clarify REST API. More 
 * details about this library, along with a link to the Javadoc, are available 
 * at http://beders.github.io/Resty/Resty/Overview.html
 */
public class ClarifyResponse {

    /**
     * Initialize a new instance with the given Resty JSONResource returned by the Resty API
     * @param jsonResource a JSONResource instance than can be used to navigate the parsed JSON response
     */
    public ClarifyResponse(JSONResource jsonResource) {
        this.jsonResource = jsonResource;
    }
    
    /**
     * Returns the response code associated with this Clarify API call
     * @return an int containing the response code as a result of the Clarify API call
     */
    public int getResponseCode() {
        try {
            return jsonResource.http().getResponseCode();
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Returns the raw JSON content from the response, allowing the caller to parse the JSON response using
     * their own library
     * @return the JSON response payload as a String
     */
    public String getResponseContent() {
        try {
            return this.jsonResource.toObject().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Returns the underlying Resty JSONResource object, representing the parsed JSON response code
     * 
     * See http://beders.github.io/Resty/Resty/Overview.html for more details on the Resty Java API
     * 
     * @return us.monoid.web.Resty.JSONResource that represents the parsed JSON response
     */
    public JSONResource getJSONResource() {
        return jsonResource;
    }
    
    /**
     * Uses the Resty JSONResponse to perform attempt to get a JSON path from the response payload
     * and return it. The Resty Java API returns an Object type, so you will need to determine the proper
     * type for casting. Note that it is usually a JSONObject when there is a complex JSON structure, a 
     * String, or a primitive JSON type.
     * 
     * @param path is the entire URL path
     * @return The result of the JSON path, usually a Resty us.monoid.json.JSONObject, a String, or a primitive value
     */
    public Object getJSONValue(String path) {
        try {
            return this.jsonResource.get(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private JSONResource jsonResource;
}
