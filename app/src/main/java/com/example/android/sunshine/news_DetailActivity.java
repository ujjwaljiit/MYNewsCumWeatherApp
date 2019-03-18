package com.example.android.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class news_DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private String mForecast[];
    private TextView mWeatherDisplay;
    private ImageView newsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_detail);

        mWeatherDisplay = (TextView) findViewById(R.id.tv_weather_data);
        newsImage=(ImageView)findViewById(R.id.imageView3);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            /*if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mForecast = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mWeatherDisplay.setText(mForecast);
            }*/

            mForecast = intentThatStartedThisActivity.getStringArrayExtra("strings");

            Picasso.with(this).load(mForecast[1]).placeholder(R.drawable.news)
                    .error(R.drawable.news)
                    .into(newsImage, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });

            mWeatherDisplay.setText((mForecast[0]+" -\n "+mForecast[2]));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.news_sharemenu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id) {
            case R.id.action_share:

                String mimeType = "text/plain";


                String title = "Learning How to Share";


                ShareCompat.IntentBuilder

                        .from(this)
                        .setType(mimeType)
                        .setChooserTitle(title)
                        .setText(mForecast[0] + "- \n" + mForecast[3])
                        .startChooser();
            break;

            case R.id.share_site:

                Uri webpage = Uri.parse(mForecast[3]);

                /*
                 * Here, we create the Intent with the action of ACTION_VIEW. This action allows the user
                 * to view particular content. In this case, our webpage URL.
                 */
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

                /*
                 * This is a check we perform with every implicit Intent that we launch. In some cases,
                 * the device where this code is running might not have an Activity to perform the action
                 * with the data we've specified. Without this check, in those cases your app would crash.
                 */
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}