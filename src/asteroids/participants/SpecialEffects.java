package asteroids.participants;

import static asteroids.game.Constants.RANDOM;
import static asteroids.game.Constants.SHIP_HEIGHT;
import static asteroids.game.Constants.SHIP_WIDTH;
import java.awt.Shape;
import java.awt.geom.Point2D;
import asteroids.game.Controller;
import asteroids.game.Participant;

public class SpecialEffects
{

    private Shape outline;
    private boolean ship;
    private final int RANGE = 5;
    private final double SPEED = 1.5;
    private DustParticle particle1, particle2, particle3, particle4, line1, line2, line3;
    private Controller controller;

    public SpecialEffects (Participant p, Controller cont)
    {
        this.controller = cont;
        // makes the dust particles
        particle1 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());
        particle2 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());
        particle3 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());
        particle4 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());

        particle1.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        particle2.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        particle3.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        particle4.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);

        controller.addParticipant(particle1);
        controller.addParticipant(particle2);
        controller.addParticipant(particle3);
        controller.addParticipant(particle4);
    }
    
    public SpecialEffects (Participant p, Controller cont, boolean ship)
    {
        this.controller = cont;

        // adds debris to the dust
        if (p instanceof Ship)
        {
            // line1 = new DustParticle(RANDOM.nextInt(RANGE/2) + p.getX(), RANDOM.nextInt(RANGE/2) + p.getY(),
            // SHIP_HEIGHT);
            // line2 = new DustParticle(RANDOM.nextInt(RANGE/2) + p.getX(), RANDOM.nextInt(RANGE/2) + p.getY(),
            // SHIP_HEIGHT);
            // line3 = new DustParticle(RANDOM.nextInt(RANGE/2) + p.getX(), RANDOM.nextInt(RANGE/2) + p.getY(),
            // SHIP_WIDTH);
            
            line1 = new DustParticle(cont.getShip().getXNose(), cont.getShip().getYNose(), SHIP_HEIGHT);
            line2 = new DustParticle(cont.getShip().getXNose(), cont.getShip().getYNose(), SHIP_HEIGHT);
            line3 = new DustParticle(cont.getShip().getXNose(), cont.getShip().getYNose(), SHIP_WIDTH);

            line1.setRotation(RANDOM.nextDouble() * 2 * Math.PI);
            line2.setRotation(RANDOM.nextDouble() * 2 * Math.PI);
            line3.setRotation(RANDOM.nextDouble() * 2 * Math.PI);

            line1.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
            line2.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
            line3.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);

            controller.addParticipant(line1);
            controller.addParticipant(line2);
            controller.addParticipant(line3);

        }

    }

}
