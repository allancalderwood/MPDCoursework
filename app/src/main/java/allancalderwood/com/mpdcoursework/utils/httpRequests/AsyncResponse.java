/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
 */
package allancalderwood.com.mpdcoursework.utils.httpRequests;

// interface to help with callbacks to the UI once asnyc process is done
public interface AsyncResponse {
    void processFinish(Object output);
}