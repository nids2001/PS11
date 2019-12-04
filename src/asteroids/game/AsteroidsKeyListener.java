package asteroids.game;

import static asteroids.game.Constants.SPEED_LIMIT;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import asteroids.participants.Bullet;

public class AsteroidsKeyListener implements KeyListener
{
    
    private Controller controller;
    
    public AsteroidsKeyListener (Controller c) {
        controller = c;
    }
    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        // Movement
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            controller.getShip().setTurning("left", true);
        if ((e.getKeyCode() == KeyEvent.VK_LEFT))
            controller.getShip().setTurning("right", true);
        if (e.getKeyCode() == KeyEvent.VK_UP){
            controller.getShip().setAccelerating(true);
            controller.sounds.playSound("thrust"); }
        
        // Fire
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_DOWN) && !controller.getPState().isBulletMaxed())
            controller.getPState().addParticipant(new Bullet(controller.getShip().getXNose(), controller.getShip().getYNose(), controller.getShip().getRotation(), SPEED_LIMIT+5, controller));
        
        // Enhanced Controls
        if (controller.getEnhanced())
            enhancedKeyPressed(e);
    }
    
    /**
     * Enhanced key controls
     * 
     */
    private void enhancedKeyPressed(KeyEvent e)
    {
        // Alt Movement
        if ( e.getKeyCode() == KeyEvent.VK_D)
            controller.getShip().setTurning("left", true);
        if (e.getKeyCode() == KeyEvent.VK_A)
            controller.getShip().setTurning("right", true);
        if (e.getKeyCode() == KeyEvent.VK_W) {
            controller.getShip().setAccelerating(true);
            controller.sounds.playSound("thrust"); }
        
        // Alt Fire
        if ((e.getKeyCode() == KeyEvent.VK_S) && !controller.getPState().isBulletMaxed())
            controller.getPState().addParticipant(new Bullet(controller.getShip().getXNose(), controller.getShip().getYNose(), controller.getShip().getRotation(), SPEED_LIMIT+5, controller));
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    @Override
    public void keyReleased (KeyEvent e)
    {
        // End movement
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            controller.getShip().setTurning("left", false);
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            controller.getShip().setTurning("right", false);
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            controller.getShip().setAccelerating(false);
            controller.sounds.stopSound("thrust"); }
        
        // Enhanced Controls
        if (controller.getEnhanced())
            enhancedKeyReleased(e);
    }
    
    public void enhancedKeyReleased (KeyEvent e)
    {
        // Alt end movement
        if (e.getKeyCode() == KeyEvent.VK_D)
            controller.getShip().setTurning("left", false);
        if (e.getKeyCode() == KeyEvent.VK_A)
            controller.getShip().setTurning("right", false);
        if (e.getKeyCode() == KeyEvent.VK_W) {
            controller.getShip().setAccelerating(false);
            controller.sounds.stopSound("thrust");
        }
    }
}
