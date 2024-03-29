package asteroids.game;

import static asteroids.game.Constants.SPEED_LIMIT;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;

/**
 * 
 * KeyListener for Asteroids Game
 *
 */
public class AsteroidsKeyListener implements KeyListener
{
    // Controller reference
    private Controller controller;
    // Player1 reference
    private Ship ship;
    
    /**
     * Creates a keyListener for Player1
     * 
     * @param c given Contoller
     */
    public AsteroidsKeyListener (Controller c) {
        controller = c;
    }
    
    /**
     * If a key of interest is pressed, record that it is down, toggling said ship movement on.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        ship = controller.getShip();
        
        // Movement
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            ship.setTurning("left", true);
        if ((e.getKeyCode() == KeyEvent.VK_LEFT))
            ship.setTurning("right", true);
        if (e.getKeyCode() == KeyEvent.VK_UP){
            ship.setAccelerating(true);
            controller.sounds.playSound("thrust"); }
        
        // Fire
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_DOWN) && !controller.getPState().isP1Maxed())
            controller.getPState().addParticipant(new Bullet("p1", ship.getXNose(), ship.getYNose(), ship.getRotation(), SPEED_LIMIT+5, controller));
        
        // Alt controls
        if (!controller.getEnhanced())
            altKeyPressed(e);
        else
            enhancedAltKeyPressed(e);
    }
    
    /**
     * if a key of interest is released, record that it was released, toggling said ship movement off.
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        // End movement
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            ship.setTurning("left", false);
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            ship.setTurning("right", false);
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            ship.setAccelerating(false);
            controller.sounds.stopSound("thrust"); }
        
        // Alt controls
        if (!controller.getEnhanced())
            altKeyReleased(e);
        else
            enhancedAltKeyReleased(e);
    }
    
    /**
     * Alternate (WASD) key-press controls for Classic Mode
     * 
     */
    private void altKeyPressed(KeyEvent e)
    {
        // Alt Movement
        if ( e.getKeyCode() == KeyEvent.VK_D)
            ship.setTurning("left", true);
        if (e.getKeyCode() == KeyEvent.VK_A)
            ship.setTurning("right", true);
        if (e.getKeyCode() == KeyEvent.VK_W) {
            ship.setAccelerating(true);
            controller.sounds.playSound("thrust"); }
        
        // Alt Fire
        if ((e.getKeyCode() == KeyEvent.VK_S) && !controller.getPState().isP1Maxed())
            controller.getPState().addParticipant(new Bullet("p1", ship.getXNose(), ship.getYNose(), ship.getRotation(), SPEED_LIMIT+5, controller));
    }
    
    /**
     * Alternate (WASD) key-release controls for Classic Mode
     *
     */
    public void altKeyReleased (KeyEvent e)
    {
        // Alt end movement
        if (e.getKeyCode() == KeyEvent.VK_D)
            ship.setTurning("left", false);
        if (e.getKeyCode() == KeyEvent.VK_A)
            ship.setTurning("right", false);
        if (e.getKeyCode() == KeyEvent.VK_W) {
            ship.setAccelerating(false);
            controller.sounds.stopSound("thrust");
        }
    }
    
    /**
     * In Enhanced Mode, the keys alternate keys for Player1 change from WASD to IJKL
     * 
     */
    private void enhancedAltKeyPressed(KeyEvent e)
    {
     // Alt Movement
        if ( e.getKeyCode() == KeyEvent.VK_L)
            ship.setTurning("left", true);
        if (e.getKeyCode() == KeyEvent.VK_J)
            ship.setTurning("right", true);
        if (e.getKeyCode() == KeyEvent.VK_I) {
            ship.setAccelerating(true);
            controller.sounds.playSound("thrust"); }
        
        // Alt Fire
        if ((e.getKeyCode() == KeyEvent.VK_K) && !controller.getPState().isP1Maxed())
            controller.getPState().addParticipant(new Bullet("p1", ship.getXNose(), ship.getYNose(), ship.getRotation(), SPEED_LIMIT+5, controller));
    }
    
    /**
     * In Enhanced Mode, the keys alternate keys for Player1 change from WASD to IJKL
     * 
     */
    private void enhancedAltKeyReleased (KeyEvent e)
    {
        // Alt end movement
        if (e.getKeyCode() == KeyEvent.VK_L)
            ship.setTurning("left", false);
        if (e.getKeyCode() == KeyEvent.VK_J)
            ship.setTurning("right", false);
        if (e.getKeyCode() == KeyEvent.VK_I) {
            ship.setAccelerating(false);
            controller.sounds.stopSound("thrust");
        }
    }
    
    @Override
    public void keyTyped (KeyEvent e)
    {
    }
}
