package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Ship;
import sounds.Sounds;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements ActionListener, Iterable<Participant>
{
    /** true if enhanced version is being playing, false otherwise */
    private boolean ENHANCED;

    /** The state of all the Participants */
    protected ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;
    private Ship ship2;

    private AlienShip alienShip;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;
    
    /** When this timer goes off, it is time to add an alien ship to the animation */
    private Timer addAlienShipTimer;

    /** Sound player */
    public Sounds sounds;

    /**key listener for when keys are pressed in the game */
    private AsteroidsKeyListener keyListener;
    private EnhancedKeyListener keyListener2;
    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;
    private long transitionTime2;


    /** Number of lives left */
    private int lives, livesP2, score, scoreP2, level;
    
    /** variable used to keep check if additional life should be added*/

    private int scoreMemory;
    
    /** used to store high score from current session*/
    private int highScore;

    /** The game display */
    private Display display;

    /**
     * Constructs a controller to coordinate the game and screen
     * 
     * @param enhanced whether or not entering enhanced mode
     */
    public Controller (boolean enh)
    {
        // Determining whether or not the gamemode is enhanced
        ENHANCED = enh;

        //set session high score to 0
        highScore = 0;
        
        //Initialize the AsteroidsKeyListener
        keyListener = new AsteroidsKeyListener(this);

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
        
        if (ENHANCED) {
            keyListener2 = new EnhancedKeyListener(this);
            transitionTime2 = Long.MAX_VALUE;
        }
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
     * Returns the ship2, or null if there isn't a player2
     */
    public Ship getShipP2 ()
    {
        return ship2;
    }

    /**
     * Returns lives
     */
    public int getLives ()
    {
        return lives;
    }
    
    /**
     * Returns lives
     */
    public int getLivesP2 ()
    {
        return livesP2;
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
    public int getScoreP2 ()
    {
        return scoreP2;
    }

    /**
     * Returns high score
     */
    public int getHighScore ()
    {
        return highScore;
    }

    /**
     * Returns score
     */
    public int getLevel ()
    {
        return level;
    }

    /**
     * Returns pstate
     */
    public ParticipantState getPState ()
    {
        return pstate;
    }

    /**
     * Returns enhanced
     */
    public boolean getEnhanced ()
    {
        return ENHANCED;
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
        if (score > scoreP2 && score > highScore)
            highScore = score;
        else if (scoreP2 > score && scoreP2 > highScore)
            highScore = scoreP2;
        display.setLegend(GAME_OVER);
        display.removeKeyListener(keyListener);
        if (ENHANCED)
            display.removeKeyListener(keyListener2);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        if (!ENHANCED)
            ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);    
        else
            ship = new Ship(SIZE / 3, SIZE / 2, -Math.PI / 2, this);
        ship.setOwner(1);
        addParticipant(ship);
        display.addKeyListener(keyListener);
    }
    
    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShipP2 ()
    {
        // Place a new ship
        Participant.expire(ship2);
        ship2 = new Ship(SIZE / 3 * 2, SIZE / 2, -Math.PI / 2, this);
        ship2.p2 = true;
        ship2.setOwner(2);
        addParticipant(ship2);
        display.addKeyListener(keyListener2);
    }

    /**
     * Place a new alien ship in the animation
     */
    private void placeAlienShip ()
    {
        alienShip = new AlienShip(this);
        addParticipant(alienShip);
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
        if (ENHANCED)
            ship2 = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Reset statistics
        lives = livesP2 = 3;
        score = scoreP2 = 0;
        level = 1;
        
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids(1);

        // Place the ship
        placeShip();

        // P2 ship
        if (ENHANCED) {
            placeShipP2();
        }
        
//        placeAlienShip();


        // //place mines
        // addParticipant(new Mine(50,80,1.0, Math.PI/4, this));
        // addParticipant(new Mine(80, 430, 2.0, Math.PI/8, this));

        // Reset statistics
        lives = 3;
        score = 0;
        level = 1;
        
        // Clear the transition time
        transitionTime = Long.MAX_VALUE;
        if (ENHANCED)
            transitionTime2 = Long.MAX_VALUE;
        
        //stop add alien ship timer
        addAlienShipTimer.stop();
        
        //stop alien ship sounds playing
        sounds.stopSound("saucerBig");
        sounds.stopSound("saucerSmall");

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(keyListener);
        display.addKeyListener(keyListener);
        // P2 controls
        if (ENHANCED) {
            display.removeKeyListener(keyListener2);
            display.addKeyListener(keyListener2);
        }


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
     * Increases scoreP2 by x amount
     * 
     * @p x points gained
     */
    public void addPointsP2 (int x)
    {
        scoreP2 += x;
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
        display.removeKeyListener(keyListener);
        
        // Decrement lives
        // lives--;

        // Play destruction sound (and end thrust sound if it's running)
        sounds.stopSound("thrust");
        sounds.playSound("bangShip");

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }
    
    /**
     * The ship has been destroyed
     */
    public void shipDestroyedP2 ()
    {
        display.removeKeyListener(keyListener2);
        // Decrement lives
        //lives--;

        // Play destruction sound (and end thrust sound if it's running)
        sounds.stopSound("thrust");
        sounds.playSound("bangShip");

        // Since the ship was destroyed, schedule a transition
        scheduleTransition2(END_DELAY);
    }

    /**
     * Alien ship has been destroyed
     */
    public void alienShipDestroyed ()
    {
        
        //make alienShip null
        alienShip = null;
        
        //start add alien ship timer
        addAlienShipTimer.start();
        if (level >= 3)

            sounds.stopSound("saucerSmall");
        else
            sounds.stopSound("saucerBig");
        
        sounds.playSound("bangAlienShip");
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
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition2 (int m)
    {
        transitionTime2 = System.currentTimeMillis() + m;
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
            // Gaining a life every 1000 points
            if (ENHANCED && (score - scoreMemory > 1000)) {

                lives++;
                scoreMemory = (score / 1000) * 1000;
            }

            // It may be time to make a game transition
            performTransition();
            if (ENHANCED)
                performTransition2();

            // Move the participants to their new locations
            pstate.moveParticipants();
            pstate.bulletMax();

            // Refresh screen
            display.refresh();
        }

        //it may be time to add an alien ship 
        else if (e.getSource() == addAlienShipTimer)
        {
            //if there isn't an alien ship, place a new one
            if (!pstate.alienShipIsActive())
            {
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
            if (lives <= 0 && livesP2 <= 0)
                finalScreen();
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
                    if (ENHANCED)
                        placeShipP2();
                }
            }
        }
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition2 ()
    {
        // Do something only if the time has been reached
        if (transitionTime2 <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime2 = Long.MAX_VALUE;
            
            if (lives <= 0 && livesP2 <= 0)
                finalScreen();
            else
            {
                placeShipP2();
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

}
