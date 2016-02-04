package org.asdtm.distresssignals;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FlashlightFragment extends Fragment
{
    private Camera mCamera;
    private boolean isFlashOn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_flashlight, parent, false);

        if (!checkFlash(getActivity())) {

            Toast.makeText(getActivity(), R.string.flashNotHasMessage, Toast.LENGTH_SHORT).show();

        }

        return v;
    }

    private boolean checkFlash(Context context)
    {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);
    }
}
