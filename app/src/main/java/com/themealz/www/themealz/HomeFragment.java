package com.themealz.www.themealz;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import com.saulpower.piechart.adapter.PieChartAdapter;
import com.saulpower.piechart.extra.Dynamics;
import com.saulpower.piechart.extra.FrictionDynamics;
import com.saulpower.piechart.views.PieChartView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public ArrayList<String> selectedMealOptionsIds;

    public View rootView;
    public ViewGroup mContainer;
    public PieChartView mChart;
    public Button mMainButton;

    public Button pizzaSlice;
    public Button sushiSlice;
    public Button meatSlice;
    public Button falafelSlice;

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mContainer = container;

        selectedMealOptionsIds = new ArrayList<String>();

        mChart = (PieChartView) rootView.findViewById(R.id.chart);
        mMainButton = (Button) rootView.findViewById(R.id.mainbutton);

        pizzaSlice = (Button) rootView.findViewById(R.id.pizza_slice);
        sushiSlice = (Button) rootView.findViewById(R.id.sushi_slice);
        meatSlice = (Button) rootView.findViewById(R.id.meat_slice);
        falafelSlice = (Button) rootView.findViewById(R.id.falafel_slice);

        pizzaSlice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedMealOptionsIds.add("5613bbe719bd6b4f232e6bfb");
                new DataRequestor().execute("");
            }
        });
        sushiSlice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedMealOptionsIds.add("5613b9d519bd6b4f232e6bf1");
                new DataRequestor().execute("");
            }
        });
        meatSlice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedMealOptionsIds.add("561af3f2721bc74808fc31a2");
                new DataRequestor().execute("");
            }
        });
        falafelSlice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedMealOptionsIds.add("5613bdf519bd6b4f232e6c0d");
                new DataRequestor().execute("");
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mChart != null) {
            mChart.onFinishTemporaryDetach();
        }
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mChart.onStartTemporaryDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mChart.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mChart.onPause();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void onBackPressed() {
        selectedMealOptionsIds.remove(selectedMealOptionsIds.size() - 1);
        if (selectedMealOptionsIds.size() > 0) {
            new DataRequestor().execute("");
        }
        else {
            ((TableLayout) rootView.findViewById(R.id.mainTableLayout)).setVisibility(View.VISIBLE);
            mChart.onPause();
            mChart.setVisibility(View.INVISIBLE);
            mMainButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * A very simple dynamics implementation with spring-like behavior
     */
    class SimpleDynamics extends Dynamics {

        /** The friction factor */
        private float mFrictionFactor;

        /**
         * Creates a SimpleDynamics object
         *
         * @param frictionFactor The friction factor. Should be between 0 and 1.
         *            A higher number means a slower dissipating speed.
         * @param snapToFactor The snap to factor. Should be between 0 and 1. A
         *            higher number means a stronger snap.
         */
        public SimpleDynamics(final float frictionFactor) {
            mFrictionFactor = frictionFactor;
        }

        @Override
        protected void onUpdate(final int dt) {

            // then update the position based on the current velocity
            mPosition += mVelocity * dt / 1000;

            // and finally, apply some friction to slow it down
            mVelocity *= mFrictionFactor;
        }
    }

    private class DataRequestor extends AsyncTask<String, Void, String> {
        private JSONArray ja;
        private String id = selectedMealOptionsIds.size() > 0 ? selectedMealOptionsIds.get(selectedMealOptionsIds.size() - 1) : null;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://themealz.com/api/mealoptions" + (id != null ? "/children/" + id : ""));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // gets the server json data
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                ja = new JSONArray(stringBuilder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (ja == null) {
                return;
            }

            ((TableLayout) rootView.findViewById(R.id.mainTableLayout)).setVisibility(View.INVISIBLE);
            mChart.setVisibility(View.VISIBLE);
            mChart.onResume();
            mMainButton.setVisibility(View.VISIBLE);

            List<Float> slices = new ArrayList<Float>();
            List<String> titles = new ArrayList<String>();
            final List<String> ids = new ArrayList<String>();

            for (int i = 0 ; i < ja.length() ; i++) {
                slices.add(1f / ja.length());
                try {
                    titles.add(ja.getJSONObject(i).has("label") && ja.getJSONObject(i).getString("label").length() > 0 ? ja.getJSONObject(i).getString("label") : ja.getJSONObject(i).getString("name"));
                    ids.add(ja.getJSONObject(i).getString("_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            PieChartAdapter adapter = new PieChartAdapter(mContainer.getContext(), slices, titles);

            mChart.setDynamics(new FrictionDynamics(0.95f));
            mChart.setSnapToAnchor(PieChartView.PieChartAnchor.BOTTOM);
            mChart.setAdapter(adapter);
            mChart.setOnPieChartChangeListener(new PieChartView.OnPieChartChangeListener() {
                @Override
                public void onSelectionChanged(final int index) {
                    int imageId;

                    switch (index % 4) {
                        case 0:
                            imageId = R.drawable.chicken;
                            break;
                        case 1:
                            imageId = R.drawable.meat;
                            break;
                        case 2:
                            imageId = R.drawable.steak;
                            break;
                        case 3:
                            imageId = R.drawable.turkey;
                            break;
                        default:
                            imageId = R.drawable.chicken;
                    }

                    mMainButton.setBackgroundResource(imageId);
                    mMainButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (selectedMealOptionsIds.size() == 0 || ids.get(index) != selectedMealOptionsIds.get(selectedMealOptionsIds.size() - 1)) {
                                selectedMealOptionsIds.add(ids.get(index));
                                new DataRequestor().execute("");
                            }
                        }
                    });
                }
            });
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}