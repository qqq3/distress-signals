package org.asdtm.distresssignals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);
    }

    public void startFlashlightActivity(View v)
    {
        Intent intent = new Intent(MainActivity.this, FlashlightActivity.class);
        startActivity(intent);
    }

    public void startLocationActivity(View v)
    {
        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
        startActivity(intent);
    }
}
