package com.sunday.myplan.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.myplan.R;
import com.sunday.myplan.bean.PlanName;

public class PlanNameList extends ArrayAdapter<PlanName> {
	Context context;
	public PlanNameList(Context context, int resource,
			List<PlanName> objects) {
		super( context, resource, objects);
		this.context=context;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewholder;
		PlanName planname = getItem( position);
		if( convertView== null){
			view=LayoutInflater. from(getContext()).inflate(R.layout.cell_list_plan, null);
			viewholder= new ViewHolder();
			viewholder.imageview= (ImageView) view.findViewById(R.id.icon_plan);
			viewholder.textview=(TextView) view.findViewById(R.id.tv_plan_name);
			viewholder.textview2=(TextView) view.findViewById(R.id.tv_plan_time);
			view.setTag( viewholder);
		}
		else
		{
			view= convertView;
			viewholder=(ViewHolder) view.getTag();
		}
		viewholder.imageview.setImageResource(Integer.parseInt(planname.getSrc()));
		viewholder. textview.setText(planname.getPlanname());
		viewholder.textview2.setText(planname.getTime()+"s");

		return view;
	}
	class ViewHolder
	{
		ImageView imageview;
		TextView textview;
		TextView textview2;

	}

}
