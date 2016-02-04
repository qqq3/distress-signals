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
    private boolean isFlashOn;

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

        if (!checkFlash(getActivity())) {
            Toast.makeText(getActivity(), R.string.flashNotHasMessage, Toast.LENGTH_SHORT).show();
            mOnOffFlash.setEnabled(false);
        }

        return v;
    }

    private boolean checkFlash(Context context)
    {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void getCameraInstance()
    {
        mCamera = null;

        try {
            mCamera = Camera.open(0);
            Camera.Parameters parameters = mCamera.getParameters();
        } catch (Exception e) {
            Log.d(TAG, "Camera is not available");
        }
    }
}
