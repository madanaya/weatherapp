package com.example.weatherapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.models.WeatherData;
import com.example.weatherapp.utils.SharedPreferenceFunctions;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String selected_address;
    private SharedPreferenceFunctions sharedPreferenceFunctions;
    private WeatherData weatherData;
    private OnFragmentInteractionListener mListener;

    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            selected_address = getArguments().getString("SELECTED_LOCATION");
            sharedPreferenceFunctions = new SharedPreferenceFunctions(getContext());
            weatherData = sharedPreferenceFunctions.getWeatherDataObject(selected_address);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        TextView windspeed = view.findViewById(R.id.today_windspeed_val);
        TextView precipitation = view.findViewById(R.id.today_precipitation_val);
        TextView pressure = view.findViewById(R.id.today_pressure_val);
        TextView temperature = view.findViewById(R.id.today_temperature_val);
        TextView summary = view.findViewById(R.id.today_summary);
        TextView humidity = view.findViewById(R.id.today_humidity_val);
        TextView visibility = view.findViewById(R.id.today_visibility_val);
        TextView cloudclover = view.findViewById(R.id.today_cloudcover_val);
        TextView ozone = view.findViewById(R.id.today_ozone_val);
        ImageView imageView = view.findViewById(R.id.today_icon);

        windspeed.setText(weatherData.getWindspeed());
        precipitation.setText(weatherData.getPrecipitation());
        pressure.setText(weatherData.getPressure());
        temperature.setText((weatherData.getTemperature()));
        summary.setText(weatherData.getSummary());
        humidity.setText(weatherData.getHumidity());
        visibility.setText(weatherData.getVisibility());
        cloudclover.setText(weatherData.getCloudCover());
        ozone.setText(weatherData.getOzone());

        imageView.setImageResource(weatherData.getIconId());
        return view;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
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
