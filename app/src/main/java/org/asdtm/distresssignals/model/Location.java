package org.asdtm.distresssignals.model;

public class Location
{
    private double mLatitude;
    private double mLongitude;

    public Location(double latitude, double longitude)
    {
        mLatitude = 1;
        mLongitude = 1;
    }

    public double getLatitude()
    {
        return mLatitude;
    }

    public void setLatitude(double latitude)
    {
        mLatitude = latitude;
    }

    public double getLongitude()
    {
        return mLongitude;
    }

    public void setLongitude(double longitude)
    {
        mLongitude = longitude;
    }
}
