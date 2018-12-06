package com.example.joelercoaster.mumbrelio;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class ForecastTask extends AsyncTask<String, Void, String> {

    private final String coords;
    private WeakReference<TextView> tvTemp;
    private WeakReference<TextView> tvSummary;
    private WeakReference<GraphView> gvWeather;

    public ForecastTask(TextView textView, TextView textView1, GraphView graphView, String coords){
        this.tvTemp = new WeakReference<>(textView);
        this.tvSummary = new WeakReference<>(textView1);
        this.gvWeather = new WeakReference<>(graphView);
        this.coords = coords;
    }

//    // Leaky field - Task is linked to view, but what happens if view is destroyed while task is in motion??
//    private TextView sender;

//    public ForecastTask(TextView receiver){
//        this.sender = receiver;
//    }

    @Override
    protected String doInBackground(String... urls) {

        //api.darksky.net/forecast/[key]/[latitude],[longitude]
        try {
            URL url = new URL("https://api.darksky.net/forecast/"+ BuildConfig.DarkSky + "/" + coords + "?exclude=[daily,alerts,flags]" );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } catch(IOException e){
                return "486: Something has gone pear-shaped";
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            return "486: Something has gone pear-shaped";
        } catch (IOException e) {
            return "486: Something has gone pear-shaped";
        }
    }

    @Override
    protected void onPostExecute(String response) {
        //Log.d("Response", response);
        try {
            JSONObject object = new JSONObject(response);
            JSONObject currentlyObject = object.getJSONObject("currently");
            JSONObject hourlyObject = object.getJSONObject("hourly");
            JSONArray hourlyData = hourlyObject.getJSONArray("data");

            String currentSummary = currentlyObject.getString("summary");
            String apparentTemp = currentlyObject.getString("apparentTemperature");

            ArrayList<String> upcomingTemps = new ArrayList<>();
            //TODO - make 4 a magic number
            if ( hourlyData != null && hourlyData.length() >= 4 ) {
                for (int i=0;i<4;i++) {
                    JSONObject hour = hourlyData.getJSONObject(i);
                    String hourlyApparentTemp = hour.getString("apparentTemperature");
                    upcomingTemps.add(hourlyApparentTemp);
                }

                DataPoint[] dp = new DataPoint[4];
                for(int i=0;i<upcomingTemps.size();i++) {
                    dp[i] = new DataPoint(new Double(i), Double.parseDouble(upcomingTemps.get(i)));
                }

                LineGraphSeries<DataPoint> hours = new LineGraphSeries<>(dp);

                Calendar cal = Calendar.getInstance();

                //TODO - should 60 be a magic number too?
                float minutes = cal.get(Calendar.MINUTE);
                float axis = minutes/60;

                Log.d("Minutes", "" + minutes);
                Log.d("Axis", "" + axis);

                PointsGraphSeries<DataPoint> current = new PointsGraphSeries<>(new DataPoint[] {
                    new DataPoint(axis, new Double(apparentTemp))
                });

                //TODO - oh hallo mn-1 (or other axis adjustments)
                gvWeather.get().getViewport().setMaxX(3);
                gvWeather.get().getViewport().setXAxisBoundsManual(true);
                gvWeather.get().addSeries(hours);
                gvWeather.get().addSeries(current);

                //TODO - Functionalize this axis noise, if possible

                int hourOne = cal.get(Calendar.HOUR_OF_DAY);
                cal.add(Calendar.HOUR_OF_DAY, 1);
                int hourTwo = cal.get(Calendar.HOUR_OF_DAY);
                cal.add(Calendar.HOUR_OF_DAY, 1);
                int hourThree = cal.get(Calendar.HOUR_OF_DAY);
                cal.add(Calendar.HOUR_OF_DAY, 1);
                int hourFour = cal.get(Calendar.HOUR_OF_DAY);

                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gvWeather.get());
                staticLabelsFormatter.setHorizontalLabels(new String[] {
                        Integer.toString(hourOne),
                        Integer.toString(hourTwo),
                        Integer.toString(hourThree),
                        Integer.toString(hourFour)
                });

                gvWeather.get().getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            }

            tvSummary.get().setText(currentSummary);
            tvTemp.get().setText(apparentTemp);
        } catch (JSONException e) {
        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "486: Something has gone pear-shaped";
        }
    }
}