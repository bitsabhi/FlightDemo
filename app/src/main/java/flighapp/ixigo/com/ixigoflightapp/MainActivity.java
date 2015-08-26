package flighapp.ixigo.com.ixigoflightapp;

import android.graphics.Typeface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends Activity {

    JSONObject jsonobject;
    JSONArray jsonarrayFlightData;
    ListView listview;
    ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;

    static String FLIGHTNAME = "flightName";
    static String DEPTIME = "depTime";
    static String ARRIVALTIME = "arrTime";
    static String PRICE = "price";
    static String DURATION = "duration";
    static String FROMSTATION = "frmStn";
    static String TOSTATION = "toStn";
    static String FLIGHTCLASS = "flightClass";

    private static String BASEURL = "http://blog.ixigo.com/sampleflightdata.json";

    private Button mDepBtn;
    private Button mArrBtn;
    private Button mPriceBtn;
    private boolean mIsFirstTapPriceSort = true;
    private boolean mIsFirstTapDepartureSort = true;
    private boolean mIsFirstTapArrivalSort = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);

        // Inflate listview if arraylist is saved in savedInstanceState.
        if (savedInstanceState != null) {
            arraylist = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("data");
            listview = (ListView) findViewById(R.id.listview);

            adapter = new ListViewAdapter(MainActivity.this, arraylist);

            listview.setAdapter(adapter);


        } else {
            // Execute DownloadJSON AsyncTask
            new DownloadJSON().execute();
        }

        mDepBtn = (Button) findViewById(R.id.depSort);
        mArrBtn = (Button) findViewById(R.id.arrSort);
        mPriceBtn = (Button) findViewById(R.id.priceSort);

        mDepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Remove the compound drawable of other buttons if present
                mArrBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                mPriceBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);

                // Sort departure in ascending order and toggle between up and down arrow
                if (mIsFirstTapDepartureSort) {
                    mDepBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, android.R.drawable.arrow_up_float, 0);
                    Collections.sort(arraylist, new MapComparator(DEPTIME)) ;
                }

                // Sort departure in descending order
                else {
                    mDepBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, android.R.drawable.arrow_down_float, 0);
                    Collections.sort(arraylist, Collections.reverseOrder(new MapComparator(DEPTIME))) ;
                }

                // Toggles the value of mIsFirstTapDepartureSort
                mIsFirstTapDepartureSort = !mIsFirstTapDepartureSort ;
                adapter.notifyDataSetChanged();

            }
        });

        mArrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Remove the compound drawable of other buttons if present
                mDepBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                mPriceBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                // Sort arrival in ascending order
                if (mIsFirstTapArrivalSort) {
                    mArrBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, android.R.drawable.arrow_up_float, 0);
                    Collections.sort(arraylist, new MapComparator(ARRIVALTIME)) ;
                }

                // Sort arrival in descending order
                else {
                    mArrBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, android.R.drawable.arrow_down_float,0);
                    Collections.sort(arraylist, Collections.reverseOrder(new MapComparator(ARRIVALTIME))) ;
                }

                // Toggles the value of mIsFirstTapArrivalSort
                mIsFirstTapArrivalSort = !mIsFirstTapArrivalSort ;
                adapter.notifyDataSetChanged();



            }
        });

        mPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Remove the compound drawable of other buttons if present
                mArrBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                mDepBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);

                // Sort price in ascending order
                if (mIsFirstTapPriceSort) {
                    mPriceBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, android.R.drawable.arrow_up_float, 0);
                    Collections.sort(arraylist, new MapComparator(PRICE));
                }

                // Sort price in descending order
                else {
                    mPriceBtn.setCompoundDrawablesWithIntrinsicBounds( 0, 0, android.R.drawable.arrow_down_float, 0);
                    Collections.sort(arraylist, Collections.reverseOrder(new MapComparator(PRICE)));
                }

                // Toggles the value of mIsFirstTapPriceSort
                mIsFirstTapPriceSort = !mIsFirstTapPriceSort;
                adapter.notifyDataSetChanged();

            }
        });

    }

    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Welcome to Ixigo");
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(BASEURL);

            try {

                JSONObject airLineMapObj = jsonobject.getJSONObject("airlineMap");

                JSONObject airportMapObj = jsonobject.getJSONObject("airportMap");

                // Locate the array name in JSON
                jsonarrayFlightData = jsonobject.getJSONArray("flightsData");

                for (int i = 0; i < jsonarrayFlightData.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonobjectFlightData = jsonarrayFlightData.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put(FROMSTATION, airportMapObj.getString(jsonobjectFlightData.getString("originCode")));
                    map.put(TOSTATION, airportMapObj.getString(jsonobjectFlightData.getString("destinationCode")));

                    long takeOffTime = Long.valueOf(jsonobjectFlightData.getString("takeoffTime"));
                    long landingTime = Long.valueOf(jsonobjectFlightData.getString("landingTime"));


                    map.put(DEPTIME, jsonobjectFlightData.getString("takeoffTime"));
                    map.put(ARRIVALTIME, jsonobjectFlightData.getString("landingTime"));

                    map.put(PRICE, jsonobjectFlightData.getString("price"));
                    map.put(FLIGHTNAME, airLineMapObj.getString(jsonobjectFlightData.getString("airlineCode")));
                    map.put(FLIGHTCLASS, jsonobjectFlightData.getString("class"));

                    long duration = (landingTime - takeOffTime)/(1000);
                    map.put(DURATION, convertSecondsToHMmSs(duration));
                    // Set the JSON Objects into the array
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(MainActivity.this, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }


    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%02dh %02dm", h,m);
    }

    // Handle Orientation change, saving activity state

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putSerializable("data",arraylist);

    }
}

