package com.tokopedia.toped;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.tokopedia.toped.base.MainActivity;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class SellerActivity extends MainActivity {

    @Override
    protected void inflateMainView(int mainViewId) {
        nav.inflateMenu(R.menu.navigation_menu);
        nav.setNavigationItemSelectedListener(onNavigationItemSelectedListener());
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return onNavigationItemAction(menuItem);
            }
        };
    }

    private boolean onNavigationItemAction(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(SellerActivity.this, MapActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
