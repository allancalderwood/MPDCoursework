/*
 * NAME: ALLAN CALDERWOOD
 * MATRICULATION NUMBER : S1628544
 */

package allancalderwood.com.mpdcoursework.utils.httpRequests;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/*
    PARAMS 0 is the TrafficScotland URL
 */

public class GetRequest extends AsyncTask<String, String, String[]>{
    private String METHOD = "GET";
    private AsyncResponse delegate = null;

    public GetRequest(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }


    protected String[] doInBackground(String... params){
        // Initiate the URL connection
        HttpsURLConnection connection = null;
        String response[] = {"404","Error"};

        // try to connect with the supplied url i.e. params[0]
        try {
            URL url = new URL(params[0]);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(METHOD);
            connection.connect();

            InputStream is = connection.getInputStream();

            // use buffered reader to read response
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                is.close();
                response[0] = String.valueOf(connection.getResponseCode());
                response[1] = sb.toString();

            }catch (Exception e){
                Log.e("BUFFER ERROR", "Error converting result " + e.toString());
            }

            // disconnect stream and return the response
            connection.disconnect();
            return response;

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }

    // method for callback function
    protected void onPostExecute(String[] result){
        delegate.processFinish(result);
    }

}
