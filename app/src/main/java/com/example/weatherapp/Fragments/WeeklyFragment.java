package com.example.weatherapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.weatherapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeeklyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeeklyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WeeklyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeeklyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyFragment newInstance(String param1, String param2) {
        WeeklyFragment fragment = new WeeklyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);

        LineChart chart = (LineChart) view.findViewById(R.id.chart);
        LineDataSet lineDataSet1 =  new LineDataSet(dataValues1(), "Temperature Min");
        LineDataSet lineDataSet2 =  new LineDataSet(dataValues2(), "Temperature Max");
        lineDataSet1.setColor(Color.parseColor("#B075F4"));
        lineDataSet2.setColor(Color.parseColor("#DF9108"));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);

        LineData data = new LineData(dataSets);
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.colorWhite));
        chart.getXAxis().setTextColor(getResources().getColor(R.color.colorWhite));
        chart.getLegend().setTextColor(getResources().getColor(R.color.colorWhite));


        Legend legend = chart.getLegend();
        legend.setTextSize(15);
        List<LegendEntry> legends = new ArrayList<>();
        LegendEntry purple = new LegendEntry();
        purple.label = "Minimum Temperature";
        purple.formColor = Color.parseColor("#B075F4");
        purple.formSize = 18;
        legends.add(purple);


        LegendEntry orange = new LegendEntry();
        orange.label = "Maximum Temperature";
        orange.formColor = Color.parseColor("#DF9108");
        orange.formSize = 18;
        legends.add(orange);

        chart.getLegend().setCustom(legends);
        legend.setForm(Legend.LegendForm.SQUARE);


        chart.setData(data);
        chart.invalidate();


        return view;
    }


    public ArrayList<Entry> dataValues1(){
        ArrayList<Entry> dataVals = new ArrayList<>();

        dataVals.add(new Entry(0,20));
        dataVals.add(new Entry(1,30));
        dataVals.add(new Entry(2,40));
        dataVals.add(new Entry(3,50));
        dataVals.add(new Entry(4,60));
        dataVals.add(new Entry(5,70));
        dataVals.add(new Entry(6,80));
        dataVals.add(new Entry(7,90));

        return dataVals;
    }

    public ArrayList<Entry> dataValues2(){
        ArrayList<Entry> dataVals = new ArrayList<>();

        dataVals.add(new Entry(0,10));
        dataVals.add(new Entry(1,50));
        dataVals.add(new Entry(2,20));
        dataVals.add(new Entry(3,30));
        dataVals.add(new Entry(4,90));
        dataVals.add(new Entry(5,20));
        dataVals.add(new Entry(6,70));
        dataVals.add(new Entry(7,80));

        return dataVals;
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
