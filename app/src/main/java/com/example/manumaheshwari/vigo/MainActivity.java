package com.example.manumaheshwari.vigo;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get pending rides JSON
    private static String url = "http://128.199.206.145/vigo/v1/displayalldrivers";

    // JSON Node names
    private static final String TAG_SOURCE = "source";
    private static final String TAG_DESTINATION = "destination";
    private static final String TAG_DRIVER_ID = "driver_id";
    private static final String TAG_NAME = "name";

    // pending rides JSONArray
    JSONArray pendingRides = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> pendingRidesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pendingRidesList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Calling async task to get json
        new GetRides().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetRides extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("contractor_id", "1"));



            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, nameValuePairs);

            Log.d("Response: "  , "> " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    pendingRides = jsonObj.getJSONArray("driver");

                    // looping through All Contacts
                    for (int i = 0; i < pendingRides.length(); i++) {
                        JSONObject c = pendingRides.getJSONObject(i);

                        String source = c.getString(TAG_NAME);
                        String destination = c.getString(TAG_DRIVER_ID);

                        // tmp hashmap for single contact
                        HashMap<String, String> pendingRide = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        pendingRide.put(TAG_NAME, source);
                        pendingRide.put(TAG_DRIVER_ID, destination);


                        // adding pending ride to pending ride list
                        pendingRidesList.add(pendingRide);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, pendingRidesList,
                    R.layout.list_view, new String[] { TAG_NAME, TAG_DRIVER_ID,}, new int[] { R.id.source,R.id.destination});

            setListAdapter(adapter);
        }

    }
}
