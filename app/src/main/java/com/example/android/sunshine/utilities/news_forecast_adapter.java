
package com.example.android.sunshine.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.sunshine.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class news_forecast_adapter extends RecyclerView.Adapter<news_forecast_adapter.ForecastAdapterViewHolder> {

    private String[][] mWeatherData;
    private Context contexti;
    public news_forecast_adapter(Context applicationContext,ForecastAdapterOnClickHandler clickHandler){

        this.contexti=applicationContext;
        mClickHandler=clickHandler;
    }



    private final ForecastAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay[]);
    }









    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_forecast_list_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ForecastAdapterViewHolder viewHolder = new ForecastAdapterViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        String[] weatherForThisDay=new String[2];
        weatherForThisDay[0] = mWeatherData[position][0];
        weatherForThisDay[1]=mWeatherData[position][1];
        forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay[0]);
        Picasso.with(contexti).load(weatherForThisDay[1]).placeholder(R.drawable.news)
                .error(R.drawable.news)
                .into(forecastAdapterViewHolder.newsImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

    }

    @Override
    public int getItemCount() {
        if (mWeatherData==null)
            return 0;
        else
            return mWeatherData.length;
    }


    public void setWeatherData(String[][] weatherData){

        mWeatherData=weatherData;
        notifyDataSetChanged();
    }


    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public  final TextView mWeatherTextView;
        public final ImageView newsImage;


        public ForecastAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mWeatherTextView=(TextView)itemView.findViewById(R.id.tv_weather_data);
            newsImage=(ImageView)itemView.findViewById(R.id.imageView3);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            String weatherForDay[]=new String[4];
             weatherForDay[0] = mWeatherData[adapterPosition][0];
             weatherForDay[1]=mWeatherData[adapterPosition][1];
             weatherForDay[2]=mWeatherData[adapterPosition][2];
            weatherForDay[3]=mWeatherData[adapterPosition][3];
            mClickHandler.onClick(weatherForDay);

        }
    }
}
