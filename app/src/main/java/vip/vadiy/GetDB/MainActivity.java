package vip.vadiy.GetDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import vip.vadiy.GetDB.Utils.RootCMD;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String TAG = "StorageDBActivity";


    Fragment fragment_db;
    Fragment fragment_xml;

    LinearLayout ll_db;
    LinearLayout ll_xml;

    TextView tv_db;
    TextView tv_xml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        InitEvent();
        setSelect(1);

    }


    //初始化各个控件
    private void InitView() {

        ll_db = findViewById(R.id.ll_db);
        ll_xml = findViewById(R.id.ll_xml);

        tv_db = findViewById(R.id.tv_db);
        tv_xml = findViewById(R.id.tv_xml);


    }

    //初始化点击触发事件
    private void InitEvent() {

        ll_db.setOnClickListener(this);
        ll_xml.setOnClickListener(this);
        tv_db.setOnClickListener(this);
        tv_xml.setOnClickListener(this);

    }

    //初始化Fragment
    private void setSelect(int index) {
        tv_db.setTextColor(getResources().getColor(R.color.colorTextViewNormal));
        tv_xml.setTextColor(getResources().getColor(R.color.colorTextViewNormal));


        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);

        switch (index) {
            case 1:
                tv_db.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                if (fragment_db == null) {
                    fragment_db = new Fragment_db();
                    transaction.add(R.id.frame_content, fragment_db);
                } else {
                    transaction.show(fragment_db);
                }
                break;
            case 2:
                tv_xml.setTextColor(getResources().getColor(R.color.colorTextViewPress));
                if (fragment_xml == null) {
                    fragment_xml = new Fragment_xml();
                    transaction.add(R.id.frame_content, fragment_xml);
                } else {
                    transaction.show(fragment_xml);
                }
                break;
        }
        transaction.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_db:
                setSelect(1);
                break;
            case R.id.tv_db:
                setSelect(1);
                break;
            case R.id.ll_xml:
                setSelect(2);
                break;
            case R.id.tv_xml:
                setSelect(2);
                break;
        }

    }


    private void hideAllFragment(FragmentTransaction transaction) {
        if (fragment_db != null) {
            transaction.hide(fragment_db);
        }
        if (fragment_xml != null) {
            transaction.hide(fragment_xml);
        }
    }


}
