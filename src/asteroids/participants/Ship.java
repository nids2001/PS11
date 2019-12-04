package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer
{
    /** The outline of the ship */
    private Shape outline;

    /** Game controller */
    private Controller controller;
    private boolean turningLeft, turningRight, accelerating;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);
        
        outline = createOutline();
        
        turningLeft = turningRight = accelerating = false;

        // Schedule a boop every second
        new ParticipantCountdownTimer(this, "boop", 1000);
        controller.sounds.playSound("beat1");
    }

    /**
     * Creates outline
     */
    public static Shape createOutline()
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        return poly;
    }
    
    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        if (accelerating)
            this.accelerate();
        applyFriction(SHIP_FRICTION);
        this.turn();
        super.move();
    }
    
    /**
     * Turns by Pi/16 radians
     */
    public void turn ()
    {
        if (turningLeft)
            rotate(Math.PI / 16);
        if (turningRight)
            rotate(-Math.PI / 16);
    }
    
    /**
     * Sets the ship to turning a certain direction
     * 
     * @param s Direction
     * @param b true or false
     */
    public void setTurning(String s, boolean b)
    {
        if (s.equals("left"))
            turningLeft = b;
        if (s.equals("right"))
            turningRight =b;
    }
    
    public void setAccelerating(boolean b)
    {
        accelerating = b;
        if (b) {
            Path2D.Double poly = new Path2D.Double();
            poly.moveTo(21, 0);
            poly.lineTo(-21, 12);
            poly.lineTo(-14, 10);
            poly.lineTo(-14, -10);
            poly.lineTo(-14, -5);
            poly.lineTo(-25,0);
            poly.lineTo(-14, 5);
            poly.lineTo(-14, 10);
            poly.lineTo(-14, -10);
            poly.lineTo(-21, -12);
            poly.closePath();
            outline = poly;
        } else {
            outline = createOutline();
        }
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            //new SpecialEffects(this, controller);
            
            // Expire the ship from the game
            Participant.expire(this);

            
            
            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Either a beep or a boop every second
        if (payload.equals("boop")) {
            controller.sounds.playSound("beat2");
            new ParticipantCountdownTimer(this, "beep", 1000);
        } else if (payload.equals("beep")) {
            controller.sounds.playSound("beat1");
            new ParticipantCountdownTimer(this,"boop", 1000);
        }
    }
}
