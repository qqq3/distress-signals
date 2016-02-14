package org.asdtm.distresssignals;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FlashlightFragment extends Fragment
{
    private static final String TAG = "FlashlightFragment";

    private Camera mCamera;
    private Camera.Parameters parameters;
    private boolean isFlashOn = false;

    private static String SOS = "0101010101010101010";

    private ImageButton mOnOffFlash;
    private Button mSosButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_flashlight, parent, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        assert appCompatActivity.getSupportActionBar() != null;
        appCompatActivity.getSupportActionBar().setTitle(0);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOnOffFlash = (ImageButton) v.findViewById(R.id.flashlight_on_off);
        mOnOffFlash.setImageResource(R.drawable.flash_off);
        mOnOffFlash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isFlashOn) {
                    offFlash();
                    mSosButton.setEnabled(true);
                    mOnOffFlash.setImageResource(R.drawable.flash_off);
                } else {
                    onFlash();
                    mSosButton.setEnabled(false);
                    mOnOffFlash.setImageResource(R.drawable.flash_on);
                }
            }
        });

        mSosButton = (Button) v.findViewById(R.id.sos_signal);
        mSosButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sosSignal();
            }
        });

        if (!checkFlash(getActivity())) {
            Toast.makeText(getActivity(), R.string.flashNotHasMessage, Toast.LENGTH_SHORT).show();
            mOnOffFlash.setEnabled(false);
            mSosButton.setEnabled(false);
        }

        return v;
    }

    private boolean checkFlash(Context context)
    {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        getCameraInstance();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }

        isFlashOn = false;
    }

    private void onFlash()
    {
        if (!isFlashOn) {
            if (mCamera == null || parameters == null) {
                return;
            }

            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();

            isFlashOn = true;
        }
    }

    private void offFlash()
    {
        if (isFlashOn) {
            if (mCamera == null || parameters == null) {
                return;
            }

            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            mCamera.startPreview();

            isFlashOn = false;
        }
    }

    private void getCameraInstance()
    {
        mCamera = null;

        try {
            mCamera = Camera.open(0);
            parameters = mCamera.getParameters();
        } catch (Exception e) {
            Log.d(TAG, "Camera is not available");
        }
    }

    private void sosSignal()
    {
        mOnOffFlash.setEnabled(false);

        for (int i = 0; i < SOS.length(); i++) {
            if (SOS.charAt(i) == '0') {
                offFlash();
            } else {
                onFlash();
            }
            try {
                if ((SOS.charAt(i) == '1') && (i < 7 && i > 11)) {
                    Thread.sleep(300);
                } else if ((SOS.charAt(i) == '1') && (i > 6 && i < 12)) {
                    Thread.sleep(900);
                } else {
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mOnOffFlash.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());

                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
