package br.ufjf.iacity.helper.coordinate;

import java.text.DecimalFormat;

/**
 * Coordenada em latitude e longitude
 */
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
    
    public double degreeToRadians(double degree) 
    {
        return degree * Math.PI / 180;
    }
    
    // Haversine Formula
    public double distanceTo(GeoCoordinate other)
    {
        // Radius of the earth in km
        int earthRadius = 6371;
        
        double dLat = degreeToRadians(other.getLatitude() - getLatitude());
        double dLon = degreeToRadians(other.getLongitude() - getLongitude()); 
        
        double a = 
                Math.sin(dLat / 2) * 
                Math.sin(dLat / 2) + 
                Math.cos(degreeToRadians(getLatitude())) * 
                Math.cos(degreeToRadians(other.getLatitude())) * 
                Math.sin(dLon / 2) * 
                Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }
    
    public CartesianCoordinate castToCartesian()
    {
        // Radius of the earth in km
        int earthRadius = 6371;
        
        double x = earthRadius * Math.cos(degreeToRadians(getLatitude())) * Math.cos(degreeToRadians(getLongitude()));
        double y = earthRadius * Math.cos(degreeToRadians(getLatitude())) * Math.sin(degreeToRadians(getLongitude()));
        
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
    public String toString()
    {
        return String.format("%s, %s", formatPattern.format(latitude), formatPattern.format(longitude));
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
