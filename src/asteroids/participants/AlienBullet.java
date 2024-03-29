package asteroids.participants;

import static asteroids.game.Constants.SPEED_LIMIT;
import java.awt.Shape;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;

/**
 * 
 * Representation of bullets shot by the Alien Ship
 *
 */
public class AlienBullet extends Bullet implements ShipDestroyer
{
    /** Outline of the bullet */
    private Shape outline;

    /** Game controller */
    private Controller controller;

    /**
     * Constructs an alien bullet at the specified coordinates that is pointed in the given direction.
     */
    public AlienBullet (double x, double y, double direction, Controller controller)
    {
        super(x, y, direction, SPEED_LIMIT - 5, controller);
    }
    /**
    * When an alien bullet collides with a ShipDestroyer, the alien bullet expires
    */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
            if (!(p instanceof AlienShip))
                Participant.expire(this);
    }
}
