/**
 * 
 */
package io.clarify.api;

import java.util.Map;
import java.util.ArrayList;
import java.net.URI;

/**
 * The starting point for the Clarify API Java SDK. The client offers two methods of interacting with the Clarify API:
 * 
 * 1) A high-level API for performing common workflows, including high-level classes to represent API resources
 * 
 * 2) A low-level API for directly interacting with the Clarify REST API directly. This provides only a thin wrapper around 
 * the actual HTTP request, but will perform the necessary steps for constructing properly-formed HTTP requests and
 * for sending credentials.
 * 
 * @author james
 *
 */
public class ClarifyClient {
    public static String CLARIFY_BASE_URI = "https://api.clarify.io";
    public static String DEFAULT_VERSION = "v1";

    public ClarifyClient(String appKey) {
	this.appKey = appKey;
    }

    public Bundle createBundle(String name, URI mediaURI) {
	// TODO: Implement call and parse response
	Bundle bundle = new Bundle(this);
	return bundle;
    }

    public BundleList listBundles() {
	// TODO: Implement call and parse response
	BundleList list = null;
	return list;
    }

    public BundleSearchResults searchBundles(String query) {
	// TODO: Implement call and parse response
	BundleSearchResults results = null;
	return results;
    }

    public Bundle findBundle(String bundleId) {
	// TODO: Implement call and parse response
	Bundle bundle = new Bundle(this);
	return bundle;
    }

    public List<Track> listTracks(String bundleId) {
	// TODO: Implement call and parse response
	ArrayList list = new ArrayList<Track>();
	return list;
    }

    public void deleteTrack(String bundleId, int trackNum) {
	// TODO: Implement call and parse response
    }


    /**
     * Perform a low-level GET request on the Clarify API. 
     *
     * Applies the authorization header with the provided application key and proper API version path
     *
     * @param path the resource path for the GET request (e.g. provide "bundles" if you want to GET /v1/bundles)
     * @param params the key/value pairs for any GET parameters to include with the request
     * @return ClarifyResponse containing the response status and the parsed JSON response code, where appropriate
     */
    public ClarifyResponse get(String path, Map<String,String> params) {
	ClarifyResponse resp = new ClarifyResponse();

	// TODO: Implement HTTP GET
 
	return resp;
    }

    /**
     * Perform a low-level POST request on the Clarify API. 
     *
     * Applies the authorization header with the provided application key and proper API version path
     *
     * @param path the resource path for the POST request (e.g. provide "bundles" if you want to POST /v1/bundles)
     * @param args the key/value pairs for any POST parameters to include with the request
     * @return ClarifyResponse containing the response status and the parsed JSON response code, where appropriate
     */
    public ClarifyResponse post(String path, Map args) {
	ClarifyResponse resp = new ClarifyResponse();

	// TODO: Implement HTTP POST
 
	return resp;
    }

    /**
     * Perform a low-level PUT request on the Clarify API. 
     *
     * Applies the authorization header with the provided application key and proper API version path
     *
     * @param path the resource path for the PUT request (e.g. provide "bundles/12345" if you want to PUT /v1/bundles/12345)
     * @param args the key/value pairs for any PUT parameters to include with the request
     * @return ClarifyResponse containing the response status and the parsed JSON response code, where appropriate
     */
    public ClarifyResponse put(String path, Map args) {
	ClarifyResponse resp = new ClarifyResponse();

	// TODO: Implement HTTP PUT
 
	return resp;
    }

    /**
     * Perform a low-level DELETE request on the Clarify API. 
     *
     * Applies the authorization header with the provided application key and proper API version path
     *
     * @param path the resource path for the DELETE request (e.g. provide "bundles/12345" if you want to DELETE /v1/bundles/12345)
     * @return ClarifyResponse containing the response status and the parsed JSON response code, where appropriate
     */
    public ClarifyResponse delete(String path) {
	ClarifyResponse resp = new ClarifyResponse();

	// TODO: Implement HTTP DELETE
	
	return resp;
    }
 
    /**
     *
     *
     */
    protected String buildPath(String path) {
	return version()+path;
    }

    /**
     *
     *
     */
    protected String baseUri() {
	return ClarifyClient.CLARIFY_BASE_URI;
    }

    /**
     *
     *
     */
    protected String version() {
	return DEFAULT_VERSION;
    }

    private String appKey;
}
