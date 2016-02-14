package org.asdtm.distresssignals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);

        ImageButton locationButton = (ImageButton) findViewById(R.id.ButtonLocationActivity);
        ImageButton flashlightButton = (ImageButton) findViewById(R.id.ButtonFlashlightActivity);
        ImageButton compassButton = (ImageButton) findViewById(R.id.ButtonCompassActivity);

        locationButton.setImageResource(R.drawable.ic_location_button);

        locationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        flashlightButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, FlashlightActivity.class);
                startActivity(intent);
            }
        });

        compassButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(intent);
            }
        });
    }
}
