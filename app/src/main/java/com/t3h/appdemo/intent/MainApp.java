package com.t3h.appdemo.intent;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.AppAdapter;
import com.t3h.appdemo.fragment.BottomDialogFragment;
import com.t3h.appdemo.fragment.NewsFragment;
import com.t3h.appdemo.fragment.NotificationFragment;
import com.t3h.appdemo.fragment.SavedFragment;

public class MainApp extends AppCompatActivity {

    private BottomAppBar appBar;
    private FloatingActionButton fabAdd;
    private Switch aSwitch;

    private TabLayout tabApp;
    private ViewPager pagerApp;
    private NewsFragment news = new NewsFragment();
    private SavedFragment saved = new SavedFragment();
    private NotificationFragment notification = new NotificationFragment();
    private AppAdapter appAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);

        setUpAppView();
        setUpAppBar();
        initView();
    }

    private void initView() {
        aSwitch = findViewById(R.id.id_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    fabAdd.hide();
                } else {
                    fabAdd.show();
                }
            }
        });
    }

    private void setUpAppBar() {
        appBar = findViewById(R.id.id_app_bar);
        fabAdd = findViewById(R.id.id_fab_add);

        setSupportActionBar(appBar);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
                    appBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                } else {
                    appBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                }
            }
        });
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomDialogFragment dialogFragment = BottomDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(),"Đoạn hội thoại cuối cùng");
            }
        });
        appBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.id_search:
                        break;
                }
                return false;
            }
        });
    }

    private void setUpAppView() {
        appAdapter = new AppAdapter(getSupportFragmentManager());

        pagerApp = findViewById(R.id.id_viewpager);
        tabApp = findViewById(R.id.id_tablayout);

        pagerApp.setAdapter(appAdapter);
        Fragment[] frmData = {news, saved, notification};
        appAdapter.setFrmData(frmData);
        tabApp.setupWithViewPager(pagerApp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.id_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = true;
                if (b){
                    fabAdd.hide();
                    return;
                }
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                boolean b = true;
                if (b){
                    fabAdd.hide();
                }
                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                boolean b = true;
                if (b){
                    fabAdd.show();
                }
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });
        
        return true;
    }

}
