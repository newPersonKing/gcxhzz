package com.whoami.gcxhzz.picturepreview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ImageShowAdapter extends FragmentStatePagerAdapter {
	private String[] imageUris;
	private OnPagerImageClickListener listener;
	
	public ImageShowAdapter(FragmentManager fm, String[] imageUris) {
		super(fm);
		this.imageUris = imageUris;
		this.listener = listener;
	}

	@Override
	public Fragment getItem(int position) {
		ImageShowFragment imageShowFragment = new ImageShowFragment();
		Bundle bundle = new Bundle();
		bundle.putString("uri", imageUris[position]);
		imageShowFragment.setArguments(bundle);

		return imageShowFragment;
	}

	@Override
	public int getCount() {
		return imageUris.length;
	}

}
