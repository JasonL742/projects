package edu.qc.seclass.grocerylistmanager;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected void setActionBar(boolean displayBackButton) {

        setSupportActionBar(findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(displayBackButton);
        actionBar.setDisplayShowHomeEnabled(displayBackButton);

        findViewById(R.id.info_button).setOnClickListener(this);

    }

    protected void setTitle(String title){

        getSupportActionBar().setTitle(title);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.info_button) {

            Intent intent = new Intent(this, SystemInfoActivity.class);

            startActivity(intent);

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
