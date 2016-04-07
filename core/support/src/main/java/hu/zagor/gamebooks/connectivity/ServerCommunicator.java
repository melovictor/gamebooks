package hu.zagor.gamebooks.connectivity;

import java.io.IOException;
import java.net.URLConnection;

/**
 * Interface to connect to and communicate with a remote server.
 * @author Tamas_Szekeres
 */
public interface ServerCommunicator {

    /**
     * Method to help compiling the post message.
     * @param key the key of the query parameter
     * @param value the value of the query parameter
     * @param previousData the previously compiled data, or null if a new one is being created
     * @return the newly compiled data
     * @throws IOException when there is a problem with the communication
     */
    String compilePostData(String key, Object value, String previousData) throws IOException;

    /**
     * Creates and opens a connection to the server.
     * @param url the target url
     * @return the opened connection
     * @throws IOException when there is a problem with the communication
     */
    URLConnection connect(String url) throws IOException;

    /**
     * Sends a request to the server.
     * @param connection the connection through which the request has to be sent
     * @param data the data to send
     * @throws IOException when there is a problem with the communication
     */
    void sendRequest(URLConnection connection, String data) throws IOException;

    /**
     * Receives the response coming back from the server.
     * @param conenction the connection to use
     * @return the response
     * @throws IOException when there is a problem with the communication
     */
    String receiveResponse(URLConnection conenction) throws IOException;

    /**
     * Method to connect to a URL in an async manner. No data can be passed, and the response is ignored.
     * @param url the URL to connect to
     */
    void connectAsync(String url);
}
