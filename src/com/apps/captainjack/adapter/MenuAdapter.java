package com.apps.captainjack.adapter;

import com.apps.captainjack.fragmentmenu.MenuPage1;
import com.apps.captainjack.fragmentmenu.MenuPage2;
import com.apps.captainjack.fragmentmenu.MenuPage3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MenuAdapter extends FragmentPagerAdapter {

	public MenuAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int item) {
		switch (item) {
		case 0:
			return new MenuPage1();
		case 1:
			return new MenuPage2();
		case 2:
			return new MenuPage3();
		default:
			return new MenuPage1();
		}

	}

	@Override
	public int getCount() {
		return 3;
	}

}
