package org.asdtm.distresssignals;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CompassFragment extends Fragment implements SensorEventListener
{
    private static final String TAG = "CompassFragment";
    private ImageView mCompassImage;
    private TextView mAzimuth;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;

    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;

    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;
    private float mCurrentDegree = 0f;

    static final float ALPHA = 0.25f;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_compass, parent, false);

        mAzimuth = (TextView) v.findViewById(R.id.azimuth);
        mCompassImage = (ImageView) v.findViewById(R.id.compass_image);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        assert appCompatActivity.getSupportActionBar() != null;
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];

        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    protected float[] lowPass(float[] input, float[] output)
    {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                //System.arraycopy(event.values, 0, valuesAccelerometer, 0, 3);
                valuesAccelerometer = lowPass(event.values.clone(), valuesAccelerometer);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                //System.arraycopy(event.values, 0, valuesMagneticField, 0, 3);
                valuesMagneticField = lowPass(event.values.clone(), valuesMagneticField);
                break;
        }

        boolean success = SensorManager.getRotationMatrix(matrixR,
                                                          matrixI,
                                                          valuesAccelerometer,
                                                          valuesMagneticField);

        if (success) {
            SensorManager.getOrientation(matrixR, matrixValues);

            float azimuthInRadians = matrixValues[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;

            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegrees,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);
            ra.setFillAfter(true);
            mCompassImage.startAnimation(ra);

            String formatAzimuth = new DecimalFormat("#0.0").format(azimuthInDegrees);
            mAzimuth.setText(formatAzimuth + getResources().getString(R.string.degree_sign));

            mCurrentDegree = -azimuthInDegrees;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

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
