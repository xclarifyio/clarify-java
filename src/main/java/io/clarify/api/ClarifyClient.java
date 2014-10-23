/**
 * 
 */
package io.clarify.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.net.URI;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;



/**
 * The starting point for the Clarify API Java SDK. The client offers two methods of interacting with the Clarify API:
 * 
 * 1) A high-level API for performing common workflows, including high-level classes to represent API resources
 * 
 * 2) A low-level API for directly interacting with the Clarify REST API directly. This provides only a thin wrapper around 
 * the actual HTTP request, but will perform the necessary steps for constructing properly-formed HTTP requests and
 * for sending credentials.
 *
 */ 
public class ClarifyClient extends Resty {
    public static String CLARIFY_BASE_URI = "https://api.clarify.io";
    public static String DEFAULT_VERSION = "v1";

    public ClarifyClient(String appKey) {
        super();
        this.appKey = appKey;
        withHeader("Authorization","Bearer "+appKey);
        withHeader("User-Agent","clarify-java-sdk-0.0.1");
    }

    /**
     * Creates a new Clarify Bundle using the Create Bundle REST API. This is a simple wrapper around
     * the API for common bundle create needs and therefore only offers a limited number of the possible API parameters
     *  
     * @param name a string containing the name of the API bundle
     * @param mediaURI a URI containing a valid URL where the media for this Bundle resides
     * @return
     * @throws IOException
     */
    public Bundle createBundle(String name, URI mediaURI) throws IOException {
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles"), form(data("name", name), data("media_url", mediaURI.toString())));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        Bundle bundle = new Bundle(this, resp);
        return bundle;
    }

    /**
     * Retrieves the first page from the List Bundles REST API. The returned BundleList can then be used to 
     * paginate through the results. Note that each call to bundleList.nextPage() will result in another API call
     * over the network. 
     * 
     * @return a BundleList instance containing the first page of results
     * @throws IOException
     */
    public BundleList listBundles() throws IOException {
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles"));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        BundleList list = new BundleList(this, resp);
        return list;
    }

    /**
     * Searches through the bundles for the specific query string provided. The result can be used to examine
     * the matched terms, locations within the media file where the terms reside, and paginate through the 
     * results. 
     * 
     * @param query a raw string (automatically URL encoded) containing the query string to search for within the bundles
     * @return a BundleSearchResults instance for examining the results and paginating through the search results
     * @throws IOException on a non-success HTTP response containing the JSON payload with the message and any error details  
     */
    public BundleSearchResults searchBundles(String query) throws IOException {
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/search?query="+enc(query)));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        BundleSearchResults results = new BundleSearchResults(this, resp);
        return results;
    }

    /**
     * Uses the Retrieve Bundle API to return a specific Bundle by the specific bundleId
     * 
     * @param bundleId a String containing the GUID of the Bundle to attempt to retrieve
     * @return the Bundle retrieved by bundleId
     * @throws IOException if a HTTP 400 error is returned due to a malformed GUID, or if a HTTP 404 not found is returned.
     * The response will contain a JSON payload with a message and error details
     */
    public Bundle findBundle(String bundleId) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles/"+bundleId));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        Bundle bundle = new Bundle(this, resp);
        return bundle;
    }

    public void deleteBundle(String bundleId) {
        // TODO: Implement call and parse response
        // TODO: Implement in Bundle.java
    }

    public List<Track> listTracks(String bundleId) {
        // TODO: Implement call and parse response
        ArrayList<Track> list = new ArrayList<Track>();
        return list;

        // TODO: Implement in Bundle.java
    }

    public Track addTrackToBundle(String bundleId, URI trackUri) {
        // TODO: Implement
        
        // TODO: Implement in Bundle.java
        return null; 
    }
    
    public Track findTrackForBundle(String bundleId, int trackNum) {
        // TODO: Implement
        
        // TODO: Implement in Bundle.java
        return null; 
    }
    
    public void deleteTrack(String bundleId, int trackNum) {
        // TODO: Implement call and parse response

        // TODO: Implement in Bundle.java
    }
    
    public Metadata findMetadata(String bundleId) {
        // TODO: Implement
        
        // TODO: Implement in Bundle.java
        return null; 
        
    }
    
    public Metadata updateMetadata(String bundleId, Map<String,String> map) {
        // TODO: Implement
        
        // TODO: Implement in Bundle.java
        return null; 
        
    }
    
    
    public void resetMetadata(String bundleId) {
        // TODO: Implement
        
        // TODO: Implement in Bundle.java
    }
    
    /**
     *
     *
     */
    protected String buildPathFromHref(String href) {
        return baseUri()+href;
    }
    
    /**
     *
     *
     */
    protected String buildPathFromResourcePath(String resourcePath) {
        return baseUri()+"/"+version()+resourcePath;
    }

    /**
     * Returns the base URI for the Clarify API
     */
    protected String baseUri() {
        return CLARIFY_BASE_URI;
    }

    /**
     * Returns the default API version
     */
    protected String version() {
        return DEFAULT_VERSION;
    }

    private String appKey;
    private Resty resty;
    
 }
