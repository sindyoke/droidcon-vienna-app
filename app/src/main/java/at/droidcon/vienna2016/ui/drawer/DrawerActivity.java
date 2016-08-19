package at.droidcon.vienna2016.ui.drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import at.droidcon.vienna2016.R;
import at.droidcon.vienna2016.ui.BaseActivity;

import butterknife.BindView;

public class DrawerActivity extends BaseActivity<DrawerPresenter> implements DrawerMvp.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.drawer_navigation) NavigationView navigationView;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected DrawerPresenter newPresenter() {
        return new DrawerPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            presenter.onNavigationItemSelected(item.getItemId());
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        drawer.removeDrawerListener(actionBarDrawerToggle);
        super.onDestroy();
    }

    @Override
    public void updateToolbarTitle(@StringRes int resId) {
        getSupportActionBar().setTitle(resId);
    }

    @Override
    public boolean isNavigationDrawerOpen() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void closeNavigationDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showDrawerMenuItem(@IdRes int id, boolean show) {
        navigationView.getMenu().findItem(id).setVisible(show);
    }

    @Override
    public Context getContext() { return this; }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer_fragments_container, fragment)
                .commit();
    }

    @Override
    public void selectDrawerMenuItem(@IdRes int id) {
        navigationView.setCheckedItem(id);
    }

    @Override
    public void hideTabLayout() {
        tabLayout.setVisibility(View.GONE);
    }

    public void setupTabLayoutWithViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }
}
