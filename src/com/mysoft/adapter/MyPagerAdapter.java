package com.mysoft.adapter;

import java.util.ArrayList;

import com.mysoft.fragment.SMSRecordFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	ArrayList<SMSRecordFragment> list;

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyPagerAdapter(FragmentManager fm, ArrayList<SMSRecordFragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
