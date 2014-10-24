package io.clarify.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import java.net.URI;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Content;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;



/**
 * The starting point for the Clarify API Java SDK. The client offers two methods of interacting with the Clarify API:
 * 
 * 1) A high-level API for performing common workflows, including high-level classes to wrap API resource responses
 * 
 * 2) A low-level API for directly interacting with the Clarify REST API. This provides only a thin wrapper around 
 * the actual HTTP request, but will perform the necessary steps for constructing properly-formed HTTP requests and
 * for sending credentials. The low-level API uses Resty, a lightweight wrapper for making HTTP API calls and 
 * processing the response payload using CSS-style JSON selectors. More details are available at 
 * <a href="http://beders.github.io/Resty/Resty/Overview.html">the Resty Overview page</>
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
     * Uses the Retrieve Bundle API to return a specific Bundle by the specific relative href path
     * 
     * @param href a String containing the relative href path of the Bundle to attempt to retrieve, as returned by the Clarify API
     * @return the Bundle retrieved by bundleId
     * @throws IOException if a HTTP 400 error is returned due to a malformed GUID, or if a HTTP 404 not found is returned.
     * The response will contain a JSON payload with a message and error details
     */
    public Bundle findBundleByHref(String href) throws IOException {
        if(href == null) {
            throw new RuntimeException("href cannot be null");
        }
        
        JSONResource jsonResource = 
                json(buildPathFromHref(href));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        Bundle bundle = new Bundle(this, resp);
        return bundle;
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
    
    /**
     * Deletes this bundle by bundleId, all of its related Metadata and Tracks, along with media stored on Clarify systems. 
     * Does not delete any media stored on remote systems.
     * 
     * USE CAUTION AS THIS CALL CANNOT BE UNDONE
     * 
     * @throws IOException if an error occurred during the delete bundle API call
     */
    public boolean deleteBundle(String bundleId) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        
        json(buildPathFromResourcePath("/bundles/"+enc(bundleId)), delete());
        
        return true; // will throw an IOException if it failed
    }

    /**
     * Returns the list of Tracks associated to this media Bundle
     * @param bundleId the GUID of the Bundle to retrieve the Tracks for
     * @return a BundleTrackList with the list of tracks and related details
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleTrackList listTracksForBundle(String bundleId) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles/"+bundleId+"/tracks"));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        BundleTrackList trackList = new BundleTrackList(this, resp);
        return trackList;
    }

    /**
     * Adds a new Track to the Bundle with the given media URI, then fetches the resulting resource (resulting in 2 API calls)
     * @param bundleId the GUID of the Bundle to add the Track to
     * @param uri the URI of the remote media file to add to the Bundle
     * @return a new Track instance containing the details about the new Track
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     * @throws RuntimeException if there is a failure calling the Find Track API
     */
    public BundleTrack addTrackToBundle(String bundleId, URI trackUri) throws IOException {
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles/"+bundleId+"/tracks"), form(data("media_url", trackUri.toString())));
        String trackHref;
        try {
            trackHref = (String)jsonResource.get("_links.self.href");
            return findTrackByHref(trackHref);
        } catch (Exception e) {
            // thrown if not found
            throw new RuntimeException(e);
        } 
    }
    
    /**
     * Returns a specific Track by track number for a media Bundle
     * @param bundleId the GUID of the Bundle to retrieve the Track for
     * @param trackId the GUID of the Track
     * @return the requested Track (throws an IOException if a 404 NOT FOUND is returned)
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleTrack findTrackForBundle(String bundleId, String trackId) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        if(trackId == null) {
            throw new RuntimeException("trackId cannot be null");
        }
        
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles/"+bundleId+"/tracks/"+trackId));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        BundleTrack track = new BundleTrack(this, resp);
        return track;
    }
    
    /**
     * Returns a specific Track by the HREF provided in a JSON response
     * @param href the relative URL to the resource
     * @return the requested Track (throws an IOException if a 404 NOT FOUND is returned)
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleTrack findTrackByHref(String href) throws IOException {
        if(href == null) {
            throw new RuntimeException("href cannot be null");
        }
        
        JSONResource jsonResource = 
                json(buildPathFromHref(href));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        BundleTrack track = new BundleTrack(this, resp);
        return track;
    }
    
    /**
     * Deletes a track from a specific media Bundle.  This will only delete media stored on Clarify systems 
     * and not delete the source media on remote systems.
     * 
     * USE CAUTION AS THIS CALL CANNOT BE UNDONE
     * 
     * @param bundleId the GUID of the Bundle to delete the specific trackNum for
     * @param trackId the GUID of the Track to delete
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public boolean deleteTrack(String bundleId, String trackId) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        
        json(buildPathFromResourcePath("/bundles/"+bundleId+"/tracks/"+trackId), delete());
        
        return true; // will throw an IOException if it failed
    }
    
    /**
     * Returns a Bundle's Metadata class, with details on the bundle and any 
     * attached user data (if available)
     * @param bundleId the GUID of the Bundle to retrieve the Metadata for
     * @return a Metadata instance for the media bundle
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleMetadata findMetadata(String bundleId) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles/"+bundleId+"/metadata"));
        ClarifyResponse resp = new ClarifyResponse(jsonResource);
        BundleMetadata metadata = new BundleMetadata(this, resp);
        return metadata;
    }
    
    /**
     * Updates the user-defined data property of the Bundle's Metadata with the supplied JSON string, 
     * then return a refreshed copy (resulting in 2 API calls)
     * @param bundleId the GUID of the Bundle for updating the Metadata
     * @param json a String containing valid JSON, or null. If null is passed, then the data is reset to a JSON equiv of {}
     * @return a refreshed Metadata instance for the media bundle
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public BundleMetadata updateMetadata(String bundleId, String json) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        if(json == null) {
            json = "{}";
        }
        
        // wrap the request in a JSON payload with a data property that contains the user JSON data to update
        JSONObject payload = null;
        try {
            payload = new JSONObject().put("data", json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JSONResource jsonResource = 
                json(buildPathFromResourcePath("/bundles/"+bundleId+"/metadata"), put(content(payload)));

        // re-retrieve
        return findMetadata(bundleId);
    }
    
    
    /**
     * Delete the metadata of a bundle and set data to {} (empty object.) 
     * This is functionally equivalent to an update metadata request with data set to {}.
     * @return true if the operation succeeded
     * @throws IOException if a failure occurred during the API,  
     * typically a 4xx HTTP error code + JSON payload with the error message and details
     */
    public boolean resetMetadata(String bundleId) throws IOException {
        if(bundleId == null) {
            throw new RuntimeException("bundleId cannot be null");
        }
        
        json(buildPathFromResourcePath("/bundles/"+bundleId+"/metadata"), delete());
        return true;
    }
    
    /**
     * Helper to concatenate the base URI of the Clarify API with a given HREF
     */
    public String buildPathFromHref(String href) {
        return baseUri()+href;
    }
    
    /**
     * Helper to concatenate the base URI of the Clarify API, the version, and a given resource path
     *
     */
    public String buildPathFromResourcePath(String resourcePath) {
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
