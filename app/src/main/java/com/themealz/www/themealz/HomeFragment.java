package com.themealz.www.themealz;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.saulpower.piechart.adapter.PieChartAdapter;
import com.saulpower.piechart.extra.Dynamics;
import com.saulpower.piechart.extra.FrictionDynamics;
import com.saulpower.piechart.views.PieChartView;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PieChartView mChart;
    public Button mMainButton;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        List<Float> slices = new ArrayList<Float>();

        slices.add(0.125f);
        slices.add(0.125f);
        slices.add(0.125f);
        slices.add(0.125f);
        slices.add(0.125f);
        slices.add(0.125f);
        slices.add(0.125f);
        slices.add(0.125f);

        PieChartAdapter adapter = new PieChartAdapter(container.getContext(), slices);

        mChart = (PieChartView) rootView.findViewById(R.id.chart);
        mChart.setDynamics(new FrictionDynamics(0.95f));
        mChart.setSnapToAnchor(PieChartView.PieChartAnchor.BOTTOM);
        mChart.setAdapter(adapter);
        mChart.setOnPieChartChangeListener(new PieChartView.OnPieChartChangeListener() {
            @Override
            public void onSelectionChanged(int index) {
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
            }
        });

        mMainButton = (Button) rootView.findViewById(R.id.mainbutton);

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
}
