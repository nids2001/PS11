package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.SpecialEffects;

/**
 * Represents asteroids
 */
public class Asteroid extends Participant implements ShipDestroyer
{
    /** The size of the asteroid (0 = small, 1 = medium, 2 = large) */
    private int size;

    /** The outline of the asteroid */
    private Shape outline;

    /** The game controller */
    private Controller controller;

    /**
     * Throws an IllegalArgumentException if size or variety is out of range.
     * 
     * Creates an asteroid of the specified variety (0 through 3) and size (0 = small, 1 = medium, 2 = large) and
     * positions it at the provided coordinates with a random rotation. Its velocity has the given speed but is in a
     * random direction.
     */
    public Asteroid (int variety, int size, double x, double y, Controller controller)
    {
        // Make sure size and variety are valid
        if (size < 0 || size > 2)
        {
            throw new IllegalArgumentException("Invalid asteroid size: " + size);
        }
        else if (variety < 0 || variety > 3)
        {
            throw new IllegalArgumentException();
        }

        // Create the asteroid
        this.controller = controller;
        this.size = size;
        setPosition(x, y);

        // Sets the speed of the asteroid depending on the size. There are three speed limits for asteroids: slow,
        // medium, and fast. A large-sized asteroid always has the slow speed. A medium-sized asteroid has a randomly
        // chosen speed that lies between slow and medium. A small-size asteroid has a randomly chosen speed that lies
        // between slow and fast.
        if (size == 2)
        {
            setVelocity(MAX_L_ASTEROID_SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        }
        if (size == 1)
        {
            setVelocity(MAX_M_ASTEROID_SPEED + (MAX_L_ASTEROID_SPEED - MAX_M_ASTEROID_SPEED) * RANDOM.nextDouble(), RANDOM.nextDouble() * 2 * Math.PI);
        }
        if (size == 0)
        {
            setVelocity(MAX_S_ASTEROID_SPEED + (MAX_M_ASTEROID_SPEED - MAX_S_ASTEROID_SPEED) * RANDOM.nextDouble(), RANDOM.nextDouble() * 2 * Math.PI);
        }
        
        //randomly sets the rotation of the asteroid
        setRotation(2 * Math.PI * RANDOM.nextDouble());
        
        //creates shape outline
        createAsteroidOutline(variety, size);
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Creates the outline of the asteroid based on its variety and size.
     */
    private void createAsteroidOutline (int variety, int size)
    {
        // This will contain the outline
        Path2D.Double poly = new Path2D.Double();

        // Fill out according to variety
        if (variety == 0)
        {
            poly.moveTo(0, -30);
            poly.lineTo(28, -15);
            poly.lineTo(20, 20);
            poly.lineTo(4, 8);
            poly.lineTo(-1, 30);
            poly.lineTo(-12, 15);
            poly.lineTo(-5, 2);
            poly.lineTo(-25, 7);
            poly.lineTo(-10, -25);
            poly.closePath();
        }
        else if (variety == 1)
        {
            poly.moveTo(10, -28);
            poly.lineTo(7, -16);
            poly.lineTo(30, -9);
            poly.lineTo(30, 9);
            poly.lineTo(10, 13);
            poly.lineTo(5, 30);
            poly.lineTo(-8, 28);
            poly.lineTo(-6, 6);
            poly.lineTo(-27, 12);
            poly.lineTo(-30, -11);
            poly.lineTo(-6, -15);
            poly.lineTo(-6, -28);
            poly.closePath();
        }
        else if (variety == 2)
        {
            poly.moveTo(10, -30);
            poly.lineTo(30, 0);
            poly.lineTo(15, 30);
            poly.lineTo(0, 15);
            poly.lineTo(-15, 30);
            poly.lineTo(-30, 0);
            poly.lineTo(-10, -30);
            poly.closePath();
        }
        else
        {
            poly.moveTo(30, -18);
            poly.lineTo(5, 5);
            poly.lineTo(30, 15);
            poly.lineTo(15, 30);
            poly.lineTo(0, 25);
            poly.lineTo(-15, 30);
            poly.lineTo(-25, 8);
            poly.lineTo(-10, -25);
            poly.lineTo(0, -30);
            poly.lineTo(10, -30);
            poly.closePath();
        }

        // Scale to the desired size
        double scale = ASTEROID_SCALE[size];
        poly.transform(AffineTransform.getScaleInstance(scale, scale));

        // Save the outline
        outline = poly;
    }

    /**
     * Returns the size of the asteroid
     */
    public int getSize ()
    {
        return size;
    }

    /**
     * When an Asteroid collides with an AsteroidDestroyer: When a large asteroid collides with a bullet or a ship, the
     * asteroid splits into two medium asteroids. When a medium asteroid collides, it splits into two small asteroids.
     * When a small asteroid collides, it disappears.
     */
    @Override
    public void collidedWith (Participant p)
    {

        if (p instanceof AsteroidDestroyer)
        {
            new SpecialEffects(p, controller);

            // Expire the asteroid
            Participant.expire(this);
            Participant.expire(p);
            
            
            // Inform the controller
            controller.asteroidDestroyed();

            if (size == 2) {
                controller.addParticipant(new Asteroid(RANDOM.nextInt(3), 1, p.getX(), p.getY(), controller));
                controller.addParticipant(new Asteroid(RANDOM.nextInt(3), 1, p.getX(), p.getY(), controller));
                controller.sounds.playSound("bangLarge");
            }
            // else if medium asteroid, splits into 2 small asteroids
            else if (size == 1) {
                controller.addParticipant(new Asteroid(RANDOM.nextInt(3), 0, p.getX(), p.getY(), controller));
                controller.addParticipant(new Asteroid(RANDOM.nextInt(3), 0, p.getX(), p.getY(), controller));
                controller.sounds.playSound("bangMedium");
            } else {
                controller.sounds.playSound("bangSmall");
            }

            // else (if a small asteroid) it will disappear

        }
    }
}
