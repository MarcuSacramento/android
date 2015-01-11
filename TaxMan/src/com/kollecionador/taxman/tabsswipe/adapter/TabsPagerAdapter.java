package com.kollecionador.taxman.tabsswipe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kollecionador.taxman.SalarioFragment;
import com.kollecionador.taxman.ConversaoFragment;
import com.kollecionador.taxman.TaxaFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new TaxaFragment();
		case 1:
			// Games fragment activity
			return new SalarioFragment();
		case 2:
			// Movies fragment activity
			return new ConversaoFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}