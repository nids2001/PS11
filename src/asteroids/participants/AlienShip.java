package asteroids.participants;

import static asteroids.game.Constants.ALIENSHIP_SCALE;
import static asteroids.game.Constants.RANDOM;
import static asteroids.game.Constants.SIZE;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class AlienShip extends Participant implements ShipDestroyer, AsteroidDestroyer
{
    private Controller controller;
    private int size, speed, length, height;
    private boolean direction;

    /** The outline of the alien ship */
    private Shape outline;

    private final int MEDIUM_SPEED = 4;
    private final int SMALL_SPEED = 6;

    public AlienShip (Controller controller)
    {

        // Create the alien ship
        this.controller = controller;
        direction = RANDOM.nextBoolean();

        setPosition(-52, RANDOM.nextInt(SIZE + 1));

        if (this.controller.getLevel() >= 3)
        {
            size = 0;
            length = 20;
            height = 25;
            controller.sounds.playSound("saucerSmall");
            setSpeed(SMALL_SPEED);
        }
        else
        {
            size = 1;
            length = 40;
            height = 50;
            controller.sounds.playSound("saucerBig");
            setSpeed(MEDIUM_SPEED);
        }

        if (direction)
        {
            setDirection(RANDOM.nextInt(3) - 1);
        }
        else
        {
            setDirection(Math.PI + (RANDOM.nextInt(3) - 1));
        }

        createShipOutline();

        new ParticipantCountdownTimer(this, "turn", 3000);
        new ParticipantCountdownTimer(this, "shoot", RANDOM.nextInt(3000) + 1000);

    }

    private void createShipOutline ()
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

    private void turn ()
    {
        if (direction)
        {
            setDirection(RANDOM.nextInt(3) - 1);
        }
        else
        {
            setDirection(Math.PI + (RANDOM.nextInt(3) - 1));
        }
    }

    private void shoot ()
    {
        if (size == 1)
        {
            controller.addParticipant(new AlienBullet(getX(), getY(), 2* Math.PI * RANDOM.nextDouble(), controller));
        }
        if (size == 0)
        {
            controller.addParticipant(new AlienBullet(getX(), getY(), getRandAngleToShip(), controller));
        }
    }

    @Override
    public double getX ()
    {
        return super.getX() + length / 2;
    }

    @Override
    public double getY ()
    {
        return super.getY() + height / 2;
    }

    private double getRandAngleToShip ()
    {
        double num1 = controller.getShip().getXNose() - getX();

        double num2 = controller.getShip().getYNose() - getY();

        double angle = Math.atan2(num2, num1);

        //return Participant.normalize(angle) + 5 ;
        
        return angle + Math.toRadians(RANDOM.nextInt(6));

    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (!(p instanceof AlienBullet))
        {
            if (p instanceof AsteroidDestroyer)
            {
                Participant.expire(this);
                Participant.expire(p);
                controller.alienShipDestroyed();
            }
            if(p instanceof Asteroid) {
                Participant.expire(this);
                Participant.expire(p);
                controller.alienShipDestroyed();
                
            }
        }

    }

    @Override
    public void countdownComplete (Object payload)
    {

        if (payload.equals("turn"))
        {
            turn();
            new ParticipantCountdownTimer(this, "turn", 1000);
        }

        if (payload.equals("shoot"))
        {
            shoot();
            new ParticipantCountdownTimer(this, "shoot", RANDOM.nextInt(3000) + 1000);
        }

    }

}
