package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;
import sounds.Sounds;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener, Iterable<Participant>
{
    /** The state of all the Participants */
    protected ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;

    private AlienShip alienShip;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer, addAlienShipTimer;

    /** Sound player */
    public Sounds sounds;

    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives, score, level;
    private int scoreMemory;

    /** The game display */
    private Display display;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);
        
        // Set up time to delay alien ship coming on screen
        addAlienShipTimer = new Timer(RANDOM.nextInt(5000) + 5000, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Load sound files
        sounds = new Sounds();

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
    }

    /**
     * This makes it possible to use an enhanced for loop to iterate through the Participants being managed by a
     * Controller.
     */
    @Override
    public Iterator<Participant> iterator ()
    {
        return pstate.iterator();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Returns lives
     */
    public int getLives ()
    {
        return lives;
    }

    /**
     * Returns score
     */
    public int getScore ()
    {
        return score;
    }

    /**
     * Returns score
     */
    public int getLevel ()
    {
        return level;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("ASTEROIDS");

        // Place four asteroids near the corners of the screen.
        placeAsteroids(1);
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.addKeyListener(this);
    }

    private void placeAlienShip ()
    {

        alienShip = new AlienShip(this);

        addParticipant(alienShip);
        display.addKeyListener(this);
    }

    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids (int level)
    {
        addParticipant(
                new Asteroid(RANDOM.nextInt(4), 2, RANDOM.nextInt(EDGE_OFFSET), RANDOM.nextInt(EDGE_OFFSET), this));
        addParticipant(new Asteroid(RANDOM.nextInt(4), 2, 750 - RANDOM.nextInt(EDGE_OFFSET),
                RANDOM.nextInt(EDGE_OFFSET), this));
        addParticipant(new Asteroid(RANDOM.nextInt(4), 2, RANDOM.nextInt(EDGE_OFFSET),
                750 - RANDOM.nextInt(EDGE_OFFSET), this));
        addParticipant(new Asteroid(RANDOM.nextInt(4), 2, 750 - RANDOM.nextInt(EDGE_OFFSET),
                750 - RANDOM.nextInt(EDGE_OFFSET), this));

        for (int i = 1; i < level; i++)
            addParticipant(
                    new Asteroid(RANDOM.nextInt(4), 2, RANDOM.nextInt(EDGE_OFFSET), RANDOM.nextInt(EDGE_OFFSET), this));
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids(1);

        // Place the ship
        placeShip();
        
        placeAlienShip();


        // //place mines
        // addParticipant(new Mine(50,80,1.0, Math.PI/4, this));
        // addParticipant(new Mine(80, 430, 2.0, Math.PI/8, this));

        // Reset statistics
        lives = 3;
        score = 0;
        level = 1;

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Increases score by x amount
     * 
     * @p x points gained
     */
    public void addPoints (int x)
    {
        score += x;
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        display.removeKeyListener(this);
        // Decrement lives
        //lives--;

        // Play destruction sound (and end thrust sound if it's running)
        sounds.stopSound("thrust");
        sounds.playSound("bangShip");

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * Alien ship has been destroyed
     */
    public void alienShipDestroyed ()
    {
        display.removeKeyListener(this);
        alienShip = null;
        addAlienShipTimer.start();
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed ()
    {
        // If all the asteroids are gone, schedule a transition
        if (countAsteroids() == 0)
        {
            scheduleTransition(END_DELAY);
        }
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to gain a life
            if (score - scoreMemory > 1000)
            {
                lives++;
                scoreMemory = (score / 1000) * 1000;
            }

            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();
            pstate.bulletMax();

            // Refresh screen
            display.refresh();
        }
        
        else if(e.getSource() == addAlienShipTimer) {
            if(!pstate.alienShipIsActive()) {
                placeAlienShip();
            }
        }
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final screen.
            if (lives <= 0)
            {
                finalScreen();
            }
            else
            {
                // Replace the ship if there are lives remaining
                placeShip();
                // Places new asteroids if level has increased
                if (countAsteroids() == 0)
                {
                    level++;
                    placeAsteroids(level);
                    addAlienShipTimer.restart();
                }
            }
        }
    }

    /**
     * Returns the number of asteroids that are active participants
     */
    private int countAsteroids ()
    {
        int count = 0;
        for (Participant p : this)
        {
            if (p instanceof Asteroid)
                count++;
        }
        return count;
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && ship != null)
            ship.setTurning("left", true);
        if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && ship != null)
            ship.setTurning("right", true);
        if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && ship != null)
        {
            ship.setAccelerating(true);
            sounds.playSound("thrust");
        }
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_S
                || e.getKeyCode() == KeyEvent.VK_DOWN) && !pstate.isBulletMaxed() && ship != null)
        {
            pstate.addParticipant(
                    new Bullet(ship.getXNose(), ship.getYNose(), ship.getRotation(), SPEED_LIMIT + 5, this));
        }
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    @Override
    public void keyReleased (KeyEvent e)
    {
        if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && ship != null)
            ship.setTurning("left", false);
        if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && ship != null)
            ship.setTurning("right", false);
        if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && ship != null)
        {
            ship.setAccelerating(false);
            sounds.stopSound("thrust");
        }
    }
}
