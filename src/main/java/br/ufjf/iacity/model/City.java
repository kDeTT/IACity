package br.ufjf.iacity.model;

import br.ufjf.iacity.helper.Coordinate;
import java.util.Objects;

public class City 
{
    private final String name;
    private final Coordinate coordinate;
    
    public City(String name, Coordinate coordinate) throws IllegalArgumentException
    {
        if((name == null) || (coordinate == null))
        {
            throw new IllegalArgumentException("O nome da cidade e/ou a coordenada não deve(m) ser nulo(s)");
        }
        
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() 
    {
        return name;
    }

    public Coordinate getCoordinate() 
    {
        return coordinate;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.coordinate);
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
        
        final City other = (City) obj;
        
        if (!Objects.equals(this.name, other.name)) 
        {
            return false;
        }
        
        if (!Objects.equals(this.coordinate, other.coordinate)) 
        {
            return false;
        }
        
        return true;
    }
}
