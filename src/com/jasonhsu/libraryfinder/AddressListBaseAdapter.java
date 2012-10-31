package com.jasonhsu.libraryfinder;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressListBaseAdapter extends BaseAdapter {
	private static ArrayList<AddressDetails> AddressDetailsArrayList;
	private LayoutInflater LayoutInflater1;
	
	public AddressListBaseAdapter(Context context, ArrayList<AddressDetails> results) {
		AddressDetailsArrayList = results;
		LayoutInflater1 = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return AddressDetailsArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return AddressDetailsArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder ViewHolder1;
		if (convertView == null) {
			convertView = LayoutInflater1.inflate(R.layout.list_addresses, null);
			ViewHolder1 = new ViewHolder();
			ViewHolder1.TextViewAddress = (TextView) convertView.findViewById(R.id.textViewAddress);
			ViewHolder1.TextViewLat = (TextView) convertView.findViewById(R.id.textViewLat);
			ViewHolder1.TextViewLng = (TextView) convertView.findViewById(R.id.textViewLong);
			convertView.setTag(ViewHolder1);
		}
		else {
			ViewHolder1 = (ViewHolder) convertView.getTag();
		}
		ViewHolder1.TextViewAddress.setText(AddressDetailsArrayList.get(position).getAddress());
		ViewHolder1.TextViewLat.setText("Latitude: " + AddressDetailsArrayList.get(position).getLatStr());
		ViewHolder1.TextViewLng.setText("Longitude: " + AddressDetailsArrayList.get(position).getLongStr());

		return convertView;
	}
	
	static class ViewHolder {
		TextView TextViewAddress;
		TextView TextViewLat;
		TextView TextViewLng;
	}
	
}
