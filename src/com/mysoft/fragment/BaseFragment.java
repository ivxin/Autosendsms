package com.mysoft.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	private Handler handler;
	protected void onPostDelayed(Runnable run,long delayed){
		if(handler==null)handler=new Handler();
		handler.postDelayed(run, delayed);
	}
}
