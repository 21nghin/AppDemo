package com.t3h.appdemo.intent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.AppAdapter;
import com.t3h.appdemo.adapter.ListJobAdapter;
import com.t3h.appdemo.fragment.BottomDialogFragment;
import com.t3h.appdemo.fragment.NewsFragment;
import com.t3h.appdemo.fragment.NotificationFragment;
import com.t3h.appdemo.fragment.SavedFragment;
import com.t3h.appdemo.model.JobModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainApp extends AppCompatActivity {

    private DatabaseReference dataRef;
    private FirebaseUser fireUser;

    private BottomAppBar appBar;
    private FloatingActionButton fabAdd;
    private Toolbar toolChat;

    private TabLayout tabApp;
    private ViewPager pagerApp;
    private NewsFragment news = new NewsFragment();
    private SavedFragment saved = new SavedFragment();
    private NotificationFragment notification = new NotificationFragment();
    private AppAdapter appAdapter;

    private ArrayList<JobModel> data;
    private ListJobAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);

        setUpAppView();
        setUpAppBar();
//        setUpDatasearch();
    }
//
//    private void setUpDatasearch() {
//        data = new ArrayList<>();
//        adapter = new ListJobAdapter(this);
//        adapter.setData(data);
//    }


    private void setUpAppBar() {
        toolChat = findViewById(R.id.tool_chat);
        appBar = findViewById(R.id.id_app_bar);
        fabAdd = findViewById(R.id.id_fab_add);

        setSupportActionBar(toolChat);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
                    appBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                    if (view.callOnClick()){
                        startActivity(new Intent(MainApp.this,CreatNews.class));
                    }

                } else {
                    appBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                    return;
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
        final MenuItem searchItem = menu.findItem(R.id.id_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (!searchView.isIconified()){
//                    searchView.setIconified(true);
//                }
//                searchItem.collapseActionView();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                List<JobModel> jobList = searchJob(data,newText);
//                adapter.setFilter(jobList);
//                return false;
//            }
//        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fabAdd.hide();
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                    fabAdd.hide();
                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                    fabAdd.show();
                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_main_message:
                startActivity(new Intent(getApplicationContext(),ChatApp.class));
                break;
        }
        return true;
    }

    private List<JobModel> searchJob(List<JobModel> pl, String query){
        query = query.toLowerCase();
        List<JobModel> jobList = new ArrayList<>();
        for (JobModel job : pl) {
            String text = job.getTitle().toLowerCase();
            if (text.startsWith(query)){
                jobList.add(job);
            }
        }
        return jobList;
    }

    private void status(String status){
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        dataRef = FirebaseDatabase.getInstance().getReference("Users").child(fireUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        dataRef.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status(getString(R.string.user_online));
    }

    @Override
    protected void onPause() {
        super.onPause();
        status(getString(R.string.user_offline));
    }

}
