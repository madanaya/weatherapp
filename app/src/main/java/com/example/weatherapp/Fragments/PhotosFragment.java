package com.example.weatherapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.PhotosAdapter;
import com.example.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String selected_address;
    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
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
            selected_address = getArguments().getString("SELECTED_LOCATION");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

//        String[] myDataset = {
//                "https://theculturetrip.com/wp-content/uploads/2018/10/g7dag5.jpg",
//                "https://consumerenergyalliance.org/cms/wp-content/uploads/2018/10/california-capitola.jpg",
//                "https://theculturetrip.com/wp-content/uploads/2018/10/g7dag5.jpg",
//                "https://consumerenergyalliance.org/cms/wp-content/uploads/2018/10/california-capitola.jpg",
//                "https://theculturetrip.com/wp-content/uploads/2018/10/g7dag5.jpg",
//                "https://consumerenergyalliance.org/cms/wp-content/uploads/2018/10/california-capitola.jpg",
//                "https://theculturetrip.com/wp-content/uploads/2018/10/g7dag5.jpg",
//                "https://consumerenergyalliance.org/cms/wp-content/uploads/2018/10/california-capitola.jpg",
//        };
//
//
//        mAdapter = new PhotosAdapter(myDataset);
//        recyclerView.setAdapter(mAdapter);
        setPhotos(selected_address);
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


    public void setPhotos(String address)
    {
        String url = "https://www.googleapis.com/customsearch/v1?q=" + address + "&cx=012110676718765995432:tn1arlbxw37&imgSize=huge&imgType=news&num=10&searchType=image&key=AIzaSyBAY4Pax3D-dg33_HKYnXpbiOus5KrknZA";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.d("PhotosFragment", url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {

//                            String [] dataset = new String[8];
//
//                            JSONArray items = response.getJSONArray("items");
//                            for(int i = 0; i < items.length(); i++){
//                                dataset[i] = items.getJSONObject(i).getString("link");
//
//                            }
//
//                            mAdapter = new PhotosAdapter(dataset, getContext());
//                            recyclerView.setAdapter(mAdapter);

                            String [] dataset = new String[8];
                            int data_index = 0;
                            int item_index = 0;

                            JSONArray items = response.getJSONArray("items");
                            while(item_index < 15 && data_index < 8){

                                dataset[data_index] = items.getJSONObject(item_index).getString("link");
                                if( !dataset[data_index].endsWith(".svg")){
                                    data_index += 1;
                                }
                                item_index += 1;
                            }
//                            for(int i = 0; i < items.length(); i++){
//                                dataset[i] = items.getJSONObject(i).getString("link");
//
//                            }

                            mAdapter = new PhotosAdapter(dataset, getContext());
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
                Log.d("RESPONSE","MESSAGE");
            }
        });

        queue.add(jsonRequest);

    }
}
