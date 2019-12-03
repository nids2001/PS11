package asteroids.participants;

import static asteroids.game.Constants.SHIP_FRICTION;
import static asteroids.game.Constants.RANDOM;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class DustParticle extends Participant
{
    private Shape outline;
    private double friction = SHIP_FRICTION + RANDOM.nextDouble()*0.02;
    
    public DustParticle(double x, double y) {        
        outline = new Ellipse2D.Double(x,y, 1, 1);
        
    }
    
    public DustParticle(double x, double y, double length) {
        outline = new Line2D.Double(x,y,x, y+length);
    }
    
    @Override
    public void move() {
        if(this.getSpeed() ==0 ) {
            expire();
        }
        applyFriction(friction);
        super.move();
    }
    
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        
    }
    
    public void expire() {
        super.expire(this);
    }

}
