package edu.qc.seclass.grocerylistmanager;

import android.os.Bundle;

public class SystemInfoActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.system_info);
        setActionBar(true);
        getSupportActionBar().setTitle("System Info");

    }

}
