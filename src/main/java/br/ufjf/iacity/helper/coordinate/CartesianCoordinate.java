package br.ufjf.iacity.helper.coordinate;

/**
 *
 * @author Luis Augusto
 */
public class CartesianCoordinate 
{
    protected double x;
    protected double y;
    
    public CartesianCoordinate(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
}
