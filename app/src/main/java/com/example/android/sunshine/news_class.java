/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.news_forecast_adapter;
import com.example.android.sunshine.utilities.news_forecast_adapter.ForecastAdapterOnClickHandler;
import com.example.android.sunshine.utilities.news_json_utils;
import com.example.android.sunshine.utilities.news_network_utils;

import java.net.URL;

public class news_class extends AppCompatActivity implements news_forecast_adapter.ForecastAdapterOnClickHandler {


    private RecyclerView mRecyclerView;
    private news_forecast_adapter mForecastAdapter;

    private ProgressBar mLoadingIndicator;

    private ImageView imageMy;
    private Context contexti = this;
    private Class newsClass = news_class.class;
    private Class homeClass= MainActivity.class;

    private Class weatherClass=weather_class.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);


        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerview_forecast);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);


        mForecastAdapter = new news_forecast_adapter(getApplicationContext(), (news_forecast_adapter.ForecastAdapterOnClickHandler) this);


        mRecyclerView.setAdapter(mForecastAdapter);
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        imageMy=(ImageView)findViewById(R.id.imageView3);
        BottomNavigationView bottom_nav=findViewById(R.id.bottom_nav);


        bottom_nav.setOnNavigationItemReselectedListener(nav_listner);


        /* Once all of our views are setup, we can load the weather data. */
        loadWeatherData();
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */

   /* public void loadImageFromUrl(String url){


        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageMy, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
*/

    private BottomNavigationView.OnNavigationItemReselectedListener nav_listner=
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.news:

                            break;

                        case R.id.home_it:
                            Intent intentToStartDetailActivity = new Intent(contexti, homeClass);


                            startActivity(intentToStartDetailActivity);
                            break;
                        case R.id.weather:
                             intentToStartDetailActivity = new Intent(contexti, weatherClass);


                            startActivity(intentToStartDetailActivity);
                            break;
                    }
                    return;

                }
            };


    private void loadWeatherData() {
        showWeatherDataView();

        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */








    private void showWeatherDataView() {

        mRecyclerView.setVisibility(View.VISIBLE);
        /* Then, make sure the weather data is visible */

    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);
        /* First, hide the currently visible data */

        /* Then, show the error */

    }






    @Override
    public void onClick(String[] weatherForDay) {


        Context context = this;
        Class destinationClass = news_DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        //intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay);
        intentToStartDetailActivity.putExtra("strings", weatherForDay);

        startActivity(intentToStartDetailActivity);


    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[][]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = news_network_utils.buildUrl(location);

            try {
                String jsonWeatherResponse = news_network_utils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[][] simpleJsonWeatherData = news_json_utils
                        .getSimpleWeatherStringsFromJson(news_class.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();

                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */

                mForecastAdapter.setWeatherData(weatherData);

            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            mForecastAdapter.setWeatherData(null);

            loadWeatherData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}