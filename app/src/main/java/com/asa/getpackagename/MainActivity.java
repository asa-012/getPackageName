package com.asa.getpackagename;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<LaunchItem> launchItemsAdapter = null;
    private ArrayList<LaunchItem> launchItems = new ArrayList<LaunchItem>();
    private MainAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = getPackageManager();
        List<PackageInfo> pckInfoList = pm.getInstalledPackages(
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        for(PackageInfo pckInfo : pckInfoList){
            LaunchItem oLaunchItem = null;
            if(pm.getLaunchIntentForPackage(pckInfo.packageName) != null){
                String packageName = pckInfo.packageName;
                String className = pm.getLaunchIntentForPackage(pckInfo.packageName).getComponent().getClassName()+"";
                Log.i("起動可能なパッケージ名",packageName);
                Log.i("起動可能なクラス名",className);
                oLaunchItem = new LaunchItem(true,packageName,className);
            }else{
                Log.i("----------起動不可能なパッケージ名",pckInfo.packageName);
                oLaunchItem = new LaunchItem(false,pckInfo.packageName,null);
            }
            launchItems.add(oLaunchItem);
        }

        mListAdapter = new MainAdapter(getApplicationContext());
        mListAdapter.setmArrayList(launchItems);
        mListAdapter.setLaunchAppListener(new MainAdapter.LaunchAppListener() {
            @Override
            public void onLaunch(Intent intent) {
                startActivity(intent);
            }
        });

        ListView launchListLayout = (ListView) findViewById(R.id.list_view);
        launchListLayout.setAdapter(mListAdapter);
    }
}