package mse.hqevaluator.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestTask extends AsyncTask<URL, Integer, Object> {

    /***
     * Interfaces for users requesting and receiving data
     */
    public interface ResponseCallback {
        public void onRequestSuccess(String response, String taskId, String identifier);
        public void onRequestError(Exception error, String taskId);
    }

    /**
     * Interface for users who wish to get progress updates
     */
    public interface ProgressCallback {
        public void onProgressUpdate(int progress, String taskId);
    }

    private String taskId = null;
    private int connectionTimeoutMillis = 10000;  // 10 seconds
    private ResponseCallback responseCallback;
    private ProgressCallback progressCallback;

    public RestTask(String taskId) {
        this.taskId = taskId;
    }

    public void setResponseCallback(ResponseCallback callback) {
        this.responseCallback = callback;
    }

    public void setProgressCallback(ProgressCallback callback) {
        this.progressCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("RestTask", "RestTask with task id " + this.taskId + " is entering onPreExecute");
    }

    @Override
    protected Object doInBackground(URL... urls) {
        Log.d("RestTask", "RestTask with task id " + this.taskId + " is entering doInBackground");

        int urlCount = urls.length;
        for (int i = 0; i < urlCount; i++) {
            // Establish HTTP connection
            try {
                URL url = urls[i];
                Log.d("RestTask", "Fetching data from " + url.toString());
                HttpURLConnection c = (HttpURLConnection)url.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(this.connectionTimeoutMillis);
                c.setReadTimeout(this.connectionTimeoutMillis);
                c.connect();
                int status = c.getResponseCode();
                StringBuilder sb = new StringBuilder();

                if (status == 200 || status == 201) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));

                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();

                    Log.d("RestTask", "Data received: " + sb.toString());
                    this.responseCallback.onRequestSuccess(sb.toString(), this.taskId, getIdentifier(i));
                }
            } catch (MalformedURLException e) {
                Log.e("RestTask", "Error in doInBackground method", e);
            } catch (IOException e) {
                Log.e("RestTask", "Error in doInBackground method", e);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        Log.d("RestTask", "RestTask with task id " + this.taskId + " is entering onPostExecute");
    }

    private String getIdentifier(int i) {
        switch (i) {
            case 0: return "nuclearPowerPlants";
            case 1: return "motorwayRamps";
            default: return null;
        }
    }
}