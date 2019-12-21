package com.app.test;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Статистика", "Пользователи" };
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
;
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return StatisticActivity.newInstance();

            case 1:
                return NotificationsFragment.newInstance();
               default:
                   return NotificationsFragment.newInstance();

        }
        //return PageFragment.newInstance(position+1);
    }

    @Override public CharSequence getPageTitle(int position) {
        // генерируем заголовок в зависимости от позиции
        return tabTitles[position];
    }
}
