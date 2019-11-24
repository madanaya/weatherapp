package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.models.WeatherData;
import com.example.weatherapp.utils.SharedPreferenceFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScreenSlidePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScreenSlidePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ScreenSlidePageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferenceFunctions sharedPreferenceFunctions;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScreenSlidePageFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ScreenSlidePageFragment newInstance(String param1, String param2) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceFunctions = new SharedPreferenceFunctions(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        final String strtext = getArguments().getString("KEY");
        final String current_address = getArguments().getString("LOCATION");


        final int position = Integer.parseInt(strtext);

        TextView tv = (TextView)rootView.findViewById(R.id.place_details);
        tv.setText(current_address + strtext);

        FloatingActionButton fb = rootView.findViewById(R.id.floatingActionButton);
        fb.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("FRAGMENT ", strtext);
                Log.d("Parent Instance", getActivity().getLocalClassName());
                MainActivity instance = (MainActivity)getActivity();
                instance.removeFragment(position);
            }
        });

        if(position == 0){
            fb.hide();
        }

        CardView card_view = (CardView) rootView.findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailsActivity.class);
                startActivity(i);
            }
        });


        WeatherData weatherData = sharedPreferenceFunctions.getWeatherDataObject(current_address);

        ImageView card1_icon = rootView.findViewById(R.id.card1_icon);
        TextView card1_temperature = rootView.findViewById(R.id.card1_temperature);;
        TextView card1_summary = rootView.findViewById(R.id.card1_summaruy);

        TextView card2_humidity = rootView.findViewById(R.id.humidity_val);
        TextView card2_visibility = rootView.findViewById(R.id.visibility_val);
        TextView card2_windspeed = rootView.findViewById(R.id.windspeed_val);
        TextView card2_pressure = rootView.findViewById(R.id.gauge_val);


        card1_icon.setImageResource(weatherData.getIconId());
        card1_temperature.setText(weatherData.getTemperature());
        card1_summary.setText(weatherData.getSummary());

        card2_humidity.setText(weatherData.getHumidity());
        card2_visibility.setText(weatherData.getVisibility());
        card2_windspeed.setText(weatherData.getWindspeed());
        card2_pressure.setText(weatherData.getPressure());

        sharedPreferenceFunctions.printAllData();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
