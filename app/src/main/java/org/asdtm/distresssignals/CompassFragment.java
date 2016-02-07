package org.asdtm.distresssignals;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassFragment extends Fragment
{
    private ImageView mCompassImage;
    private TextView mCompassDegree;

    private SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_compass, parent, false);

        mCompassDegree = (TextView) v.findViewById(R.id.compass_degree);
        mCompassImage = (ImageView) v.findViewById(R.id.compass_image);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        return v;
    }
}
