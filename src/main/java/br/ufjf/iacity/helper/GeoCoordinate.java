package br.ufjf.iacity.helper;

import java.text.DecimalFormat;

public class GeoCoordinate 
{
    private static final double MAX_LATITUDE = 90.0000000;
    private static final double MIN_LATITUDE = -90.0000000;
    
    private static final double MAX_LONGITUDE = 180.0000000;
    private static final double MIN_LONGITUDE = -180.0000000;
    
    private double latitude;
    private double longitude;
    
    private final DecimalFormat formatPattern;
    
    public GeoCoordinate(double latitude, double longitude) throws IllegalArgumentException
    {
        if(isValidLatitude(latitude) && isValidLongitude(longitude))
        {
            this.formatPattern = new DecimalFormat("##.#######");
            
            this.latitude = latitude;
            this.longitude = longitude;
        }
        else
        {
            throw new IllegalArgumentException("Latitude e/ou longitude inválida(s)");
        }
    }
    
    @Override
    public String toString()
    {
        return String.format("%s, %s", formatPattern.format(latitude), formatPattern.format(longitude));
    }
    
    private double longitudeToScreenX(double canvasWidth, double startX, double longitude) 
    {

        // Make the value positive, so we can calculate the percentage
        double iAdjustedDegreesOfLongitude = (longitude * 1) + 180;
        double iDegreesOfLongitudeToScreenX = 0;

        // Are we at the West -180 point?
        if (iAdjustedDegreesOfLongitude == 0) 
        {
            // Screen X is the left of the map (avoid divide by zero)
            iDegreesOfLongitudeToScreenX = startX;
        } 
        else if (iAdjustedDegreesOfLongitude > 360) 
        {
            // If the longitude crosses the 180 line fix it (doesn't translat to screen well)
            iDegreesOfLongitudeToScreenX = startX + canvasWidth;
        } 
        else 
        {
            // Convert the longitude value to screen X
            iDegreesOfLongitudeToScreenX = (startX + (iAdjustedDegreesOfLongitude * (canvasWidth / 360)));
        }

        return iDegreesOfLongitudeToScreenX;
    }
    
    private double latitudeToScreenY(double canvasHeight, double startY, double latitude) 
    {
        // Make the value positive, so we can calculate the percentage
        double iAdjustedDegreesOfLatitude = (latitude * 1) + 90;
        double iDegreesOfLatitudeToScreenY = 0;

        // Are we at the South pole?
        if (iAdjustedDegreesOfLatitude == 0) 
        {
            // Screen Y is the botton of the map (avoid divide by zero)
            iDegreesOfLatitudeToScreenY = canvasHeight + startY;
        } 
        else if (iAdjustedDegreesOfLatitude > 180)
        {
		// Are we at the North pole (or beyond)?
            // Screen Y is the top of the map
            iDegreesOfLatitudeToScreenY = startY;
        }
        else 
        {
            // Convert the latitude value to screen X
            iDegreesOfLatitudeToScreenY = (canvasHeight - (iAdjustedDegreesOfLatitude * (canvasHeight / 180)) + startY);
        }

        return iDegreesOfLatitudeToScreenY;
    }

    public CartesianCoordinate castToCartesian(double canvasWidth, double canvasHeight, double startX, double startY)
    {
        double x = this.longitudeToScreenX(canvasWidth, startX, longitude);
        double y = this.latitudeToScreenY(canvasHeight, startY, latitude);

        return new CartesianCoordinate(x, y);
    }
    
    public static boolean isValidLatitude(double latitude)
    {
        return ((latitude >= MIN_LATITUDE) && (latitude <= MAX_LATITUDE));
    }
    
    public static boolean isValidLongitude(double longitude)
    {
        return ((longitude >= MIN_LONGITUDE) && (longitude <= MAX_LONGITUDE));
    }
    
    private String castToString(double coordinate)
    {
        return formatPattern.format(coordinate);
    }

    public double getLatitude() 
    {
        return latitude;
    }
    
    public String getLatitudeString()
    {
        return castToString(latitude);
    }
    
    public double getLongitude() 
    {
        return longitude;
    }
    
    public String getLongitudeString()
    {
        return castToString(longitude);
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        
        final GeoCoordinate other = (GeoCoordinate) obj;
        
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) 
        {
            return false;
        }
        
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) 
        {
            return false;
        }
        
        return true;
    }
}
