package asteroids.participants;

import static asteroids.game.Constants.SHIP_FRICTION;
import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import asteroids.game.Participant;

/**
 * creates the particles that appear when an object is destroyed
 *
 */
public class DustParticle extends Participant
{
    /** outline of the particle */
    private Shape outline;

    /** friction that is applied to the particle */
    private double friction;
    
    /**x and y coordinates for each end of the line and the length of the line */
    private double x1,y1, x2, y2;

    /**
     * creates a dot particle at x,y with SHIP_FRICTION friction that varies randomly up to 0.02
     *
     */
    public DustParticle (double x, double y)
    {
        friction = SHIP_FRICTION + RANDOM.nextDouble() * 0.02;
        outline = new Ellipse2D.Double(x, y, 1, 1);
    }

    /**
     * creates a line particle at x,y with a length of length*, with SHIP_FRICTION friction that varies randomly up to
     * 0.02
     */
    public DustParticle (double x, double y, double length)
    {
        rotate(x,y, length);
        friction = SHIP_FRICTION + RANDOM.nextDouble() * 0.02;
        outline = new Line2D.Double(x1, y1, x2, y2);
    }

    /** if the particle isn't moving, expire it. Otherwise, apply the friction and move() */
    @Override
    public void move ()
    {
        if (this.getSpeed() == 0)
        {
            expire();
        }
        applyFriction(friction);
        super.move();
    }
    
    /** Gives a random rotation to the particle */
    private void rotate(double x, double y, double length) {
        double direction = Math.PI * RANDOM.nextDouble();
        x1 = x;
        y1 = y;
        if(direction < Math.PI / 2) {
            x2 = x + (Math.cos(direction) * length);
        }
        else {
            x2 = x -(Math.cos(direction) * length);
        }
        y2 = y - (Math.sin(direction) * length);
    }

    /**
     * Returns outline of dustParticle
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
    }

    /**
     * Expire this object
     */
    public void expire ()
    {
        super.expire(this);
    }

}
