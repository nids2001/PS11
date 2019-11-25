package asteroids.participants;

import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class Mine extends Participant implements AsteroidDestroyer
{
    private Shape outline;
    private Controller controller;
    
    public Mine(int x, int y, double speed, double direction, Controller controller) {
        
        this.controller = controller;
        
        setPosition(x,y);
        setRotation(2 * Math.PI * RANDOM.nextDouble());
        setDirection(direction);
        setSpeed(speed);
        
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(28, 0);
        poly.lineTo(36,28);
        poly.lineTo(28,56);
        poly.lineTo(20,28);
        poly.lineTo(28,0);
        
        poly.moveTo(0,28);
        poly.lineTo(28,20);
        poly.lineTo(56,28);
        poly.lineTo(28,36);
        poly.lineTo(0, 28);
        
        poly.moveTo(24,12);
        poly.lineTo(8,8);
        poly.lineTo(12,24);
        
        poly.moveTo(32,12);
        poly.lineTo(48,8);
        poly.lineTo(44,24);
        
        poly.moveTo(44,36);
        poly.lineTo(48,48);
        poly.lineTo(32,44);
        
        poly.moveTo(24,44);
        poly.lineTo(8,48);
        poly.lineTo(12,32);
        
        outline = poly;
        
     // turn every few seconds 
        new ParticipantCountdownTimer(this, "turn", 6000);
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }
    

    public void turn() {
        setDirection(getDirection() + Math.PI/2);
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub
        
    }
    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        if (payload.equals("turn"))
        {
            turn();
            new ParticipantCountdownTimer(this, "turn", 5000);
        }
    }
}
