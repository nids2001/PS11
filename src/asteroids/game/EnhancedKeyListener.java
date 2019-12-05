package asteroids.game;

import static asteroids.game.Constants.SPEED_LIMIT;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import asteroids.participants.Bullet;
import asteroids.participants.Ship;

public class EnhancedKeyListener implements KeyListener
{
    private Controller controller;
    private Ship ship;
    
    public EnhancedKeyListener (Controller c) {
        controller = c;
    }
    
    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        ship = controller.getShipP2();
        
        // Movement
        if (e.getKeyCode() == KeyEvent.VK_D)
            ship.setTurning("left", true);
        if ((e.getKeyCode() == KeyEvent.VK_A))
            ship.setTurning("right", true);
        if (e.getKeyCode() == KeyEvent.VK_W){
            ship.setAccelerating(true);
            controller.sounds.playSound("thrust"); }
        
        // Fire
        if ((e.getKeyCode() == KeyEvent.VK_S) && !controller.getPState().isP2Maxed())
            controller.getPState().addParticipant(new Bullet("p2", ship.getXNose(), ship.getYNose(), ship.getRotation(), SPEED_LIMIT+5, controller));
    }
    
    @Override
    public void keyReleased (KeyEvent e)
    {
        // End movement
        if (e.getKeyCode() == KeyEvent.VK_D)
            ship.setTurning("left", false);
        if (e.getKeyCode() == KeyEvent.VK_A)
            ship.setTurning("right", false);
        if (e.getKeyCode() == KeyEvent.VK_W) {
            ship.setAccelerating(false);
            controller.sounds.stopSound("thrust"); }
    }
    
    @Override
    public void keyTyped (KeyEvent e)
    {
    }
}
