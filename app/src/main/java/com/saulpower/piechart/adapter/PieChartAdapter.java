package com.saulpower.piechart.adapter;

import android.content.Context;

import com.saulpower.piechart.views.PieChartView;
import com.saulpower.piechart.views.PieSliceDrawable;

import java.util.List;

public class PieChartAdapter extends BasePieChartAdapter {
    
    public final String TAG = this.getClass().getSimpleName();

	private Context mContext;
	private List<Float> mObjects;
	private List<String> mTitles;
	
	public PieChartAdapter(Context context, List<Float> objects, List<String> titles) {
		init(context, objects, titles);
	}
	
	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mObjects.get(position);
	}
	
	private void init(Context context, List<Float> objects, List<String> titles) {
		
		mContext = context;
		mObjects = objects;
		mTitles = titles;
	}

	@Override
	public float getPercent(int position) {
		Float percent = (Float) getItem(position);
		
		return percent;
	}

	@Override
	public PieSliceDrawable getSlice(PieChartView parent, PieSliceDrawable convertDrawable, int position, float offset) {

		PieSliceDrawable sliceView = convertDrawable;
		
		if (sliceView == null) {
			sliceView = new PieSliceDrawable(parent, mContext);
		}
		
//		sliceView.setSliceColor(UiUtils.getRandomColor(mContext, position));
		sliceView.setPercent(mObjects.get(position));
		sliceView.setTitle(mTitles.get(position));
		sliceView.setPosition(position);
		sliceView.setDegreeOffset(offset);
		
		return sliceView;
	}
}
