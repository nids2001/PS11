package asteroids.participants;

import static asteroids.game.Constants.SIZE;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;

public class Bullet extends Participant implements AsteroidDestroyer
{
    /** Outline of the bullet */
    private Shape outline;
    
    /** Game controller */
    private Controller controller;
    
    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Bullet (double x, double y, double direction, double speed, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setVelocity(speed, direction);
        setRotation(0);
        
        Path2D.Double poly = new Path2D.Double();
        poly.append(new Ellipse2D.Double(-2,-2, 4, 4), true);
        outline = poly;
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }
    
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
            Participant.expire(this);
    }
    

}