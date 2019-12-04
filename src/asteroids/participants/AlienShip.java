package asteroids.participants;

import static asteroids.game.Constants.ASTEROID_SCALE;
import static asteroids.game.Constants.ALIENSHIP_SCALE;
import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class AlienShip extends Participant implements ShipDestroyer
{
    private Controller controller;
    private int size, direction, speed;

    /** The outline of the alien ship */
    private Shape outline;

    private final int MEDIUM_SPEED = 4;
    private final int SMALL_SPEED = 6;

    private final int HORIZONTAL = 0;
    private final int ANGLE_UP = 1;
    private final int ANGLE_DOWN = -1;

    public AlienShip (int size, double x, double y, Controller controller)
    {

        // Create the alien ship
        this.controller = controller;
        this.size = size;

        
        if (size == 0)
        {
            setSpeed(SMALL_SPEED);
        }
        else
        {
            setSpeed(MEDIUM_SPEED);
        }

        setPosition(x, y);
        setDirection(RANDOM.nextInt(3)-1);

        createShipOutline(size);
        
        new ParticipantCountdownTimer(this, "turn", 1000);

    }

    private void createShipOutline (int size)
    {
        // This will contain the outline
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(15, 0);
        poly.lineTo(35, 0);
        poly.lineTo(40, 15);
        poly.lineTo(10, 15);
        poly.lineTo(15, 0);

        poly.moveTo(10, 40);
        poly.lineTo(40, 40);
        poly.lineTo(50, 25);
        poly.lineTo(40, 15);
        poly.lineTo(10, 15);
        poly.lineTo(0, 25);
        poly.lineTo(10, 40);

        poly.moveTo(0, 25);
        poly.lineTo(50, 25);

        // Scale to the desired size
        double scale = ALIENSHIP_SCALE[size];
        poly.transform(AffineTransform.getScaleInstance(scale, scale));

        outline = poly;

       
    }

    private void turn() {
        setDirection(RANDOM.nextInt(3) -1);
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub

    }

   


    @Override
    public void countdownComplete (Object payload)
    {
        // Either a beep or a boop every second
        if (payload.equals("turn"))
        {
            turn();
            new ParticipantCountdownTimer(this, "turn", 1000);
        }

    }
}
