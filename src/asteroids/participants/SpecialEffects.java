package asteroids.participants;

import static asteroids.game.Constants.RANDOM;
import static asteroids.game.Constants.SHIP_HEIGHT;
import static asteroids.game.Constants.SHIP_WIDTH;
import java.awt.Shape;
import java.awt.geom.Point2D;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.participants.DustParticle;

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

        // if p is a ship, add ship debris
        if (p instanceof Ship)
        {
            line1 = new DustParticle(p.getX(), p.getY(), SHIP_HEIGHT);
            line2 = new DustParticle(p.getX(), p.getY(), SHIP_HEIGHT);
            line3 = new DustParticle(p.getX(), p.getY(), SHIP_WIDTH);

            line1.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
            line2.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
            line3.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);

            controller.addParticipant(line1);
            controller.addParticipant(line2);
            controller.addParticipant(line3);

        }

        // make the dust particles
        particle1 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());
        particle2 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());
        particle3 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());
        particle4 = new DustParticle(RANDOM.nextInt(RANGE) + p.getX(), RANDOM.nextInt(RANGE) + p.getY());

        // set the speed and random direction of each particle
        particle1.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        particle2.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        particle3.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);
        particle4.setVelocity(SPEED, RANDOM.nextDouble() * 2 * Math.PI);

        // add the particles to the controller
        controller.addParticipant(particle1);
        controller.addParticipant(particle2);
        controller.addParticipant(particle3);
        controller.addParticipant(particle4);
    }

}
