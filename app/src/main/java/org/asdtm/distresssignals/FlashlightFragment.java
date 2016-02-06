package org.asdtm.distresssignals;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FlashlightFragment extends Fragment
{
    private static final String TAG = "FlashlightFragment";

    private Camera mCamera;
    private Camera.Parameters parameters;
    private boolean isFlashOn = false;

    private static String SOS = "0101010101010101010";

    private Button mOnOffFlash;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_flashlight, parent, false);

        mOnOffFlash = (Button) v.findViewById(R.id.flashlight_on_off);
        mOnOffFlash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isFlashOn) {
                    offFlash();
                } else {
                    onFlash();
                }
            }
        });

        if (!checkFlash(getActivity())) {
            Toast.makeText(getActivity(), R.string.flashNotHasMessage, Toast.LENGTH_SHORT).show();
            mOnOffFlash.setEnabled(false);
        }

        Button mSosButton = (Button) v.findViewById(R.id.sos_signal);
        mSosButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sosSignal();
            }
        });

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
    }
}
