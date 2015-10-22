package com.themealz.www.themealz;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saulpower.piechart.adapter.PieChartAdapter;
import com.saulpower.piechart.extra.Dynamics;
import com.saulpower.piechart.extra.FrictionDynamics;
import com.saulpower.piechart.views.PieChartView;

import org.json.JSONArray;
import org.json.JSONException;

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
    public ImageView mPlate;
    public Button mMainButton;
    public TextView mMainButtonTitle;
//    public ListView mSummaryList;

    public Button pizzaSlice;
    public Button sushiSlice;
    public Button meatSlice;
    public Button falafelSlice;

//    public Animation stepFadeOut;
//    public Animation stepFadeIn;

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

        ((TextView) rootView.findViewById(R.id.maintitle)).setTypeface(Typeface.createFromAsset(mContainer.getContext().getAssets(), "fonts/regular.ttf"));
        ((TextView) rootView.findViewById(R.id.mainbuttontitle)).setTypeface(Typeface.createFromAsset(mContainer.getContext().getAssets(), "fonts/regular.ttf"));

        mChart = (PieChartView) rootView.findViewById(R.id.chart);
//        mPlate = (ImageView) rootView.findViewById(R.id.plate);
        mMainButton = (Button) rootView.findViewById(R.id.mainbutton);
        mMainButtonTitle = (TextView) rootView.findViewById(R.id.mainbuttontitle);
//        mSummaryList = (ListView) rootView.findViewById(R.id.summarylist);

        pizzaSlice = (Button) rootView.findViewById(R.id.pizza_slice);
        sushiSlice = (Button) rootView.findViewById(R.id.sushi_slice);
        meatSlice = (Button) rootView.findViewById(R.id.meat_slice);
        falafelSlice = (Button) rootView.findViewById(R.id.falafel_slice);

//        stepFadeOut = new AlphaAnimation(1, 0);
//        stepFadeOut.setInterpolator(new DecelerateInterpolator());
//        stepFadeOut.setDuration(500);
//        stepFadeOut.setFillEnabled(true);
//        stepFadeOut.setFillAfter(true);
//        stepFadeIn = new AlphaAnimation(0, 1);
//        stepFadeIn.setInterpolator(new DecelerateInterpolator());
//        stepFadeIn.setStartOffset(500);
//        stepFadeIn.setDuration(500);
//        stepFadeIn.setFillEnabled(true);
//        stepFadeIn.setFillAfter(true);

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

        ((ImageView) rootView.findViewById(R.id.restaurants_step)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((RelativeLayout) rootView.findViewById(R.id.step_3)).setVisibility(View.INVISIBLE);
                ((RelativeLayout) rootView.findViewById(R.id.step_4)).setVisibility(View.VISIBLE);
            }
        });

        Animation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setInterpolator(new DecelerateInterpolator());
        fadeInAnimation.setStartOffset(1000);
        fadeInAnimation.setDuration(750);
        ((TextView) rootView.findViewById(R.id.maintitle)).setAnimation(fadeInAnimation);

        Animation marginAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                final float scale = mContainer.getContext().getResources().getDisplayMetrics().density;
                int factor = (int)((232 * scale + 0.5f) * (1 - interpolatedTime)) + 8;

                setSlicesMargins(pizzaSlice, factor);
                setSlicesMargins(sushiSlice, factor);
                setSlicesMargins(meatSlice, factor);
                setSlicesMargins(falafelSlice, factor);
            }
        };
        marginAnimation.setStartOffset(1750);
        marginAnimation.setDuration(1250); // in ms
        rootView.startAnimation(marginAnimation);

        // Inflate the layout for this fragment
        return rootView;
    }

    void setSlicesMargins(Button button, int factor) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) falafelSlice.getLayoutParams();
        layoutParams.setMargins(factor, factor, factor, factor);
        button.setLayoutParams(layoutParams);
    }

    void setSelection(final int index, final List<String> ids, List<String> titles) {
        int imageId;

        switch (index % 7) {
            case 0:
                imageId = R.drawable.image01;
                break;
            case 1:
                imageId = R.drawable.image02;
                break;
            case 2:
                imageId = R.drawable.image03;
                break;
            case 3:
                imageId = R.drawable.image04;
                break;
            case 4:
                imageId = R.drawable.image05;
                break;
            case 5:
                imageId = R.drawable.image06;
                break;
            case 6:
                imageId = R.drawable.image07;
                break;
            default:
                imageId = R.drawable.image01;
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
        mMainButtonTitle.setText(titles.get(index));
        rootView.playSoundEffect(SoundEffectConstants.CLICK);
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
            ((RelativeLayout) rootView.findViewById(R.id.step_4)).setVisibility(View.INVISIBLE);
            ((RelativeLayout) rootView.findViewById(R.id.step_3)).setVisibility(View.INVISIBLE);
            mChart.onPause();
            mChart.setVisibility(View.INVISIBLE);
            ((RelativeLayout) rootView.findViewById(R.id.step_2)).setVisibility(View.INVISIBLE);
            ((RelativeLayout) rootView.findViewById(R.id.step_1)).setVisibility(View.VISIBLE);
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
            if (ja == null || ja.length() == 0) {
                ((RelativeLayout) rootView.findViewById(R.id.step_1)).setVisibility(View.INVISIBLE);
                mChart.onPause();
                mChart.setVisibility(View.INVISIBLE);
                ((RelativeLayout) rootView.findViewById(R.id.step_2)).setVisibility(View.INVISIBLE);
                ((RelativeLayout) rootView.findViewById(R.id.step_3)).setVisibility(View.VISIBLE);
                ((RelativeLayout) rootView.findViewById(R.id.step_4)).setVisibility(View.INVISIBLE);

//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContainer.getContext(),
//                        android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
//                        "Moe's Tavern",
//                        "המסעדה הגדולה",
//                        "Café 80's",
//                        "Ten Forward",
//                        "Monk's Café",
//                        "אימפריית השמש",
//                        "Central Perk",
//                        "Cheers"
//                });
//
//                mSummaryList.setAdapter(adapter);

                return;
            }

            ((RelativeLayout) rootView.findViewById(R.id.step_1)).setVisibility(View.INVISIBLE);
            mChart.setVisibility(View.VISIBLE);
            mChart.onResume();
            ((RelativeLayout) rootView.findViewById(R.id.step_2)).setVisibility(View.VISIBLE);
            ((RelativeLayout) rootView.findViewById(R.id.step_3)).setVisibility(View.INVISIBLE);
            ((RelativeLayout) rootView.findViewById(R.id.step_4)).setVisibility(View.INVISIBLE);

            List<Float> slices = new ArrayList<Float>();
            final List<String> titles = new ArrayList<String>();
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
            mChart.setSnapToAnchor(PieChartView.PieChartAnchor.TOP);
            mChart.setAdapter(adapter);
            mChart.setOnPieChartSlideListener(new PieChartView.OnPieChartSlideListener() {
                @Override
                public void onSelectionSlided(final int index) {
                    setSelection(index, ids, titles);
                }
            });
            setSelection(mChart.getCurrentIndex(), ids, titles);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}