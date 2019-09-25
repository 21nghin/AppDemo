package com.t3h.appdemo.intent;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.t3h.appdemo.R;
import com.t3h.appdemo.adapter.CmtAdapter;
import com.t3h.appdemo.model.CmtModel;

import java.util.ArrayList;

public class DetailNewsApp extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rcvCmt;
    private CmtAdapter adapter;
    private ArrayList<CmtModel> data = new ArrayList<>();
    private BottomAppBar appBar;
    private EditText edtComment;
    private ImageButton btnSaved;
    private ImageButton btnDelete;
    private ImageButton btnChat;

    private Toolbar toolbar;
    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        initView();


        toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setTitle(getString(R.string.title_detail_job));
        toolbar.setTitleTextColor(Color.DKGRAY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_black_24dp);
        initData();
    }


    private void initData() {
        adapter = new CmtAdapter(this);
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        data.add(new CmtModel("tam","12/212/1212","fiasdasdadadaddasddad"));
        adapter.setData(data);
    }

    private void initView() {
        btnChat = findViewById(R.id.item_detail_chat);
        btnSaved = findViewById(R.id.item_saved);
        btnDelete = findViewById(R.id.item_detail_delete);
        btnChat = findViewById(R.id.item_detail_chat);
        edtComment = findViewById(R.id.edt_comment);
        appBar = findViewById(R.id.id_app_bar);
        rcvCmt = findViewById(R.id.lv_comment);

        rcvCmt.setAdapter(adapter);
        btnDelete.setOnClickListener(this);
        btnSaved.setOnClickListener(this);
        btnChat.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_detail_chat:
                Toast.makeText(this, "chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_detail_delete:
                mDialog = new AlertDialog.Builder(this);
                mDialog.setMessage(getString(R.string.dialog_message_delete_detel))
                        .setPositiveButton(getString(R.string.message_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.message_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                mDialog.show();
                break;
            case R.id.item_saved:
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
