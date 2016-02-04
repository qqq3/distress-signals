package org.asdtm.distresssignals;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class FlashlightActivity extends FragmentActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentContainer = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragmentContainer == null) {
            fragmentContainer = new FlashlightFragment();
            fragmentManager.beginTransaction().add(R.id.fragmentContainer,
                                                   fragmentContainer).commit();
        }
    }
}
