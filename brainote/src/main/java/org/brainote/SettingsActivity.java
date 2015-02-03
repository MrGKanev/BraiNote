package org.brainote;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private List<Fragment> backStack = new ArrayList<Fragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.brainote.R.layout.activity_settings);
        initUI();
        getFragmentManager().beginTransaction().replace(org.brainote.R.id.content_frame, new SettingsFragment()).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    void initUI() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(org.brainote.R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    void switchToScreen(String key) {
        SettingsFragment sf = new SettingsFragment();
        Bundle b = new Bundle();
        b.putString(SettingsFragment.XML_NAME, key);
        sf.setArguments(b);
        backStack.add(getFragmentManager().findFragmentById(org.brainote.R.id.content_frame));
        replaceFragment(sf);
    }


    private void replaceFragment(Fragment sf) {
        getFragmentManager().beginTransaction().setCustomAnimations(org.brainote.R.animator.fade_in, org.brainote.R.animator.fade_out,
                org.brainote.R.animator.fade_in, org.brainote.R.animator.fade_out).replace(org.brainote.R.id.content_frame, sf).commit();
    }


    @Override
    public void onBackPressed() {
        if (backStack.size() > 0) {
            replaceFragment(backStack.remove(backStack.size() - 1));
        } else {
            super.onBackPressed();
        }
    }
}
