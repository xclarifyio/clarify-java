# Introduction

The clarify-java library allows you to use the Clarify API from within Java. It handles all of the HTTP requests and response codes for you.

The library itself is MIT-licensed so if you find anything which is incomplete, unclear, or even wrong, feel free to contribute to the Github project and make the community better.

## More Information

While what follows is documentation on the Java library specifically, when in doubt, check out our API documentation at https://developer.clarify.io

## Prerequisites

* Java JDK-1.6 or higher
* Apache Maven 3 or higher
* Please refer http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html for any help in Maven.
* If you are using Eclipse, you can import the project by following these steps: http://books.sonatype.com/m2eclipse-book/reference/creating-sect-importing-projects.html

## To build sdk:

* Run mvn install to build the sdk jar

## Maven Installation

Create a new maven application.
Add dependency to sdk in your application's pom.xml as below.
```
<dependency>
 <groupId>io.clarify.api</groupId>
 <artifactId>clarify-api-sdk</artifactId>
 <version>1.0.0</version>
</dependency>
```

## Testing your configuration


```
import io.clarify.api.*;
import java.net.URI;

public class App {
  public static void main(String[] args) throws Exception {
    String appKey = "0123456789abcdef";

    // Construct the API client
    ClarifyClient client = new ClarifyClient(appKey);

    // Create your first bundle using an example audio file
    String name = "Harvard Sentences";
    URI mediaUrl =  URI.create(“https://s3-us-west-2.amazonaws.com/op3nvoice/harvard-sentences-1.wav”);
    Bundle bundle = client.createBundle(name, mediaUrl);

    System.out.println(bundle.id());
  }
}
```

## Listing your media
```
import io.clarify.api.*;
import java.net.URI;

public class App {
  public static void main(String[] args) throws Exception {
    String appKey = "0123456789abcdef";

    // Construct the API client
    ClarifyClient client = null;
    client = new ClarifyClient(appKey);

    // Start listing all of your media
    BundleList bundleList = client.listBundles();

    JSONArray linkItems = bundleList.getLinkItems();
    for(int i=0;i<linkItems.length();i++) {
        JSONObject item = (JSONObject)linkItems.get(i);
        System.out.println("href="+item.get("href"));
    }

    // ask for the next page, if we have one
    if (bundleList.hasNextPage()) {
        bundleList = bundleList.nextPage();
        // ...
    }
  }
}
```


## Searching your media
```
import io.clarify.api.*;
import java.net.URI;

public class App {
  public static void main(String[] args) throws Exception {
    String appKey = "0123456789abcdef";

    // Construct the API client
    ClarifyClient client = new ClarifyClient(appKey);

    // Search your media by query string
    String query = "monkeys";
    BundleSearchResults bundleSearchResults = client.searchBundles(query);
    JSONArray itemResults = results.getItemResults();
    for(int i=0;i<itemResults.length();i++) {
        JSONObject item = (JSONObject)itemResults.get(i);
        System.out.println("score="+item.get("score"));
    }


    // ask for the next page, if we have one
    if (bundleSearchResults.hasNextPage()) {
        bundleSearchResults = bundleSearchResults.nextPage();
        // ...
    }
  }
}
```

## Deleting your media
```
import io.clarify.api.*;
import java.net.URI;

public class App {
  public static void main(String[] args) throws Exception {
    String appKey = "0123456789abcdef";

    // Construct the API client
    ClarifyClient client = null;
    client = new ClarifyClient(appKey);

    // Search your media by query string
    String bundleId = "abc1234";
    Bundle bundle = client.findBundle(bundleId);
    if bundle != null { 
        boolean success = bundle.delete();
    }
  }
}
```

## Storing your Media
```
// Create your first bundle using an example audio file
String name = "Harvard Sentences";
URI mediaUrl =  URI.create(“https://s3-us-west-2.amazonaws.com/op3nvoice/harvard-sentences-1.wav”);

HashMap<String,String> args = new HashMap(); 
// populate with additional name/value arguments for the API call
// args.put(“metadata”,”{}”);

Bundle bundle = client.createBundle(name, mediaUrl, args);
System.out.println(bundle.id());
```

## Get the Metadata for a Bundle
```
// Obtain the metadata in one API call by using the embed parameter
String bundleId = "abc1234";
HashMap<String,String> args = new HashMap(); 
args.put(“embed”,”metadata”);
Bundle bundle = client.findBundle(bundleId, args);
if bundle != null { 
    System.out.println(bundle.getMetadata());
}

or

// Obtain the metadata by bundle ID in one API call
String bundleId = "abc1234";
Metadata metadata = client.findMetadata(bundleId);
```

## Update the Metadata for a Bundle
```
String bundleId = "abc1234";
HashMap newValues = new HashMap();
// fill newValues with the values we now wish the metadata to have…
// ...

// Update the metadata in two API calls: 1) Retrieve the bundle with the bundle’s metadata, 2) call the update Metadata API
HashMap<String,String> args = new HashMap(); 
args.put(“embed”,”metadata”);
Bundle bundle = client.findBundle(bundleId, args);

if bundle != null { 
    BundleMetadata metadata = bundle.getMetadata();
    metadata.update(newValues);
}

or

BundleMetadata metadata = client.findMetadata(bundleId);
metadata.update(newValues);

Reset the Metadata (delete)
String bundleId = "abc1234";
Bundle bundle = client.findBundle(bundleId);
if bundle != null { 
    BundleMetadata metadata = bundle.metadata();
    if metadata != null {
      boolean success = metadata.reset();
    }
}
```

## Add a Track to a bundle
```
String bundleId = "abc1234";
Bundle bundle = client.findBundle(bundleId);
if(bundle != null) { 
    URI trackUrl =  URI.create(“https://s3-us-west-2.amazonaws.com/op3nvoice/harvard-sentences-1.wav”);
    Track track = bundle.addTrack(trackUrl);


}

List of Tracks for a bundle
String bundleId = "abc1234";

// Note: makes two API calls
Bundle bundle = client.findBundle(bundleId);
if bundle != null { 
    BundleTrackList tracks = bundle.listTracksForBundle();
}

or

// Note: makes only one API call
BundleTrackList tracks = client.listTracksForBundle(bundleId)

Get a track
String bundleId = "abc1234";
String trackId = “wxyz9876”;
Bundle bundle = client.findBundle(bundleId);
if bundle != null { 
   Track track = bundle.findTrack(trackId);
}
```

## Delete a track
```
String bundleId = "abc1234";
String trackId = “wxyz9876”;

// Note: makes two API calls
Bundle bundle = client.findBundle(bundleId);
if bundle != null { 
   boolean success = bundle.deleteTrack(trackId);
}

or

// Note: makes only one API call
client.deleteTrack(bundleId, trackId)
```

## Direct-Access Client API
Note: the direct-access client API uses the Resty API directly. See the Resty documentation (http://beders.github.io/Resty/Resty/Overview.html) and Javadoc (http://beders.github.io/Resty/Resty/API_Docs.html) for more details on how to use it. 

Example:

```
public class App {
  public static void main(String[] args) throws Exception {
    String appKey = "0123456789abcdef";

    // Construct the API client
    ClarifyClient client = new ClarifyClient(appKey);

    // All API calls to the Resty API will include your app key automatically in the header
    JSONResource jsonResource = 
                client.json(client.buildPathFromResource("/bundles"));

    // … use the standard Resty API to interact with the parsed JSON response directly
  }
}
```
