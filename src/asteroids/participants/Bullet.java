package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import sounds.Sounds;

/**
 * Representation of bullets
 *
 */
public class Bullet extends Participant implements AsteroidDestroyer
{
    /** Outline of the bullet */
    private Shape outline;
    
    /** Sound controller reference */
    private Sounds sounds;
    
    /** owner of the bullet */
    private int player;
    
    /**
     * Constructs a bullet at the specified coordinates that is pointed in the given direction.
     */
    public Bullet (double x, double y, double direction, double speed, Controller controller)
    {
        // Set Bullet information
        sounds = controller.sounds;
        setPosition(x, y);
        setVelocity(speed, direction);
        setRotation(0);
        player = 0;
        
        // Draw bullet
        Path2D.Double poly = new Path2D.Double();
        poly.append(new Ellipse2D.Double(-.5,-.5, 1, 1), true);
        outline = poly;
        
        sounds.playSound("fire");
    }
    
    /**
     * Constructs a bullet with a specified owner at the specified coordinates that is pointed in the given direction.
     */
    public Bullet (String owner, double x, double y, double direction, double speed, Controller controller)
    {
        // Bullet information
        sounds = controller.sounds;
        setPosition(x, y);
        setVelocity(speed, direction);
        setRotation(0);
        
        // set bullet owner
        if (owner.equals("p1"))
                player = 1;
        if (owner.equals("p2"))
                player = 2;
        
        // Draw bullet
        Path2D.Double poly = new Path2D.Double();
        poly.append(new Ellipse2D.Double(-.5,-.5, 1, 1), true);
        outline = poly;
        sounds.playSound("fire");
    }
    
    /**
     * Returns owner of the bullet
     */
    @Override
    public int getOwner() {
        return player;
    }
    
    /**
     * Returns the outline of the bullet
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }
    
    /**
     * Expires bullet if it runs into a ShipDestroyer
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
            Participant.expire(this);
    }
}
