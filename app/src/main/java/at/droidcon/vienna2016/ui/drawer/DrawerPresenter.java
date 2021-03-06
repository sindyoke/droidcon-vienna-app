package at.droidcon.vienna2016.ui.drawer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import javax.inject.Inject;

import at.droidcon.vienna2016.DroidconApp;
import at.droidcon.vienna2016.R;
import at.droidcon.vienna2016.ui.home.HomeFragment;
import at.droidcon.vienna2016.ui.venue.VenueConferenceFragment;
import at.droidcon.vienna2016.ui.venue.VenuePagerFragment;
import at.droidcon.vienna2016.utils.Analytics;
import at.droidcon.vienna2016.utils.Configuration;
import at.droidcon.vienna2016.ui.BaseActivityPresenter;
import at.droidcon.vienna2016.ui.schedule.pager.SchedulePagerFragmentBuilder;
import at.droidcon.vienna2016.ui.settings.SettingsFragment;
import at.droidcon.vienna2016.ui.speakers.list.SpeakersListFragment;
import at.droidcon.vienna2016.ui.tweets.TweetsListFragment;

import icepick.State;

public class DrawerPresenter extends BaseActivityPresenter<DrawerMvp.View> implements DrawerMvp.Presenter {

    @State @StringRes int toolbarTitle;
    @State @IdRes int selectedItemId;
    @Inject Configuration cfg;
    @Inject Analytics analytics;

    public DrawerPresenter(DrawerMvp.View view) {
        super(view);
        DroidconApp.get(view.getContext()).component().inject(this);
    }

    private @IdRes int getFirstItem() {
        cfg.refresh();
        if (cfg.getBoolean("show_home_screen")) {
            return R.id.drawer_nav_home;
        }
        else {
            return R.id.drawer_nav_schedule;
        }
    }

    private void checkHiddenEntries() {
        cfg.refresh();
        view.showDrawerMenuItem(R.id.drawer_nav_home, cfg.getBoolean("show_home_screen"));
        view.showDrawerMenuItem(R.id.drawer_nav_tweets, cfg.getBoolean("show_tweets_screen"));
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // check if the home and twitter screen shall be shown
        checkHiddenEntries();
        if (savedInstanceState == null) {
            onNavigationItemSelected(getFirstItem());
            view.selectDrawerMenuItem(getFirstItem());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        checkHiddenEntries();
        view.updateToolbarTitle(toolbarTitle);
    }

    @Override
    public void onShow() {
        checkHiddenEntries();
    }

    @Override
    public void onNavigationItemSelected(@IdRes int itemId) {
        if (itemId != selectedItemId) {
            switch (itemId) {
                case R.id.drawer_nav_home:
                    view.showFragment(new HomeFragment());
                    toolbarTitle = R.string.drawer_nav_home;
                    analytics.logViewScreen("home");
                    break;
                case R.id.drawer_nav_schedule:
                    view.showFragment(new SchedulePagerFragmentBuilder(false).build());
                    toolbarTitle = R.string.drawer_nav_schedule;
                    analytics.logViewScreen("personal_schedule");
                    break;
                case R.id.drawer_nav_agenda:
                    view.showFragment(new SchedulePagerFragmentBuilder(true).build());
                    toolbarTitle = R.string.drawer_nav_agenda;
                    analytics.logViewScreen("general_schedule");
                    break;
                case R.id.drawer_nav_speakers:
                    view.showFragment(new SpeakersListFragment());
                    analytics.logViewScreen("speakers");
                    toolbarTitle = R.string.drawer_nav_speakers;
                    break;
                case R.id.drawer_nav_tweets:
                    view.showFragment(new TweetsListFragment());
                    analytics.logViewScreen("tweets");
                    toolbarTitle = R.string.drawer_nav_tweets;
                    break;
                case R.id.drawer_nav_venue:
                    view.showFragment(new VenuePagerFragment());
                    analytics.logViewScreen("venue");
                    toolbarTitle = R.string.drawer_nav_venue;
                    break;
                case R.id.drawer_nav_settings:
                    view.showFragment(new SettingsFragment());
                    analytics.logViewScreen("settings");
                    toolbarTitle = R.string.drawer_nav_settings;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            view.hideTabLayout();
            view.updateToolbarTitle(toolbarTitle);

            selectedItemId = itemId;
        }
        view.closeNavigationDrawer();
    }

    @Override
    public boolean onBackPressed() {
        if (view.isNavigationDrawerOpen()) {
            view.closeNavigationDrawer();
            return true;
        } else {
            int firstItem = getFirstItem();
            if (selectedItemId != firstItem) {
                onNavigationItemSelected(firstItem);
                view.selectDrawerMenuItem(firstItem);
                return true;
            }
        }
        return false;
    }
}
