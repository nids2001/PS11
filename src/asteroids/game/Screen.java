package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import javax.swing.*;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Ship;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class Screen extends JPanel
{
    /** Legend that is displayed across the screen */
    private String legend;

    /** Game controller */
    private Controller controller;

    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setBackground(Color.black);
        setForeground(Color.white);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        setFocusable(true);
    }

    /**
     * Set the legend
     */
    public void setLegend (String legend)
    {
        this.legend = legend;
    }

    /**
     * Paint the participants onto this panel
     */
    @Override
    public void paintComponent (Graphics graphics)
    {
        // Use better resolution
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Do the default painting
        super.paintComponent(g);

        // Draw the legend across the middle of the panel
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        int size = g.getFontMetrics().stringWidth(legend);
        g.drawString(legend, (SIZE - size) / 2, SIZE / 2);

        // Draw each participant in its proper place
        for (Participant p : controller)
        {
            if (controller.getEnhanced())
            {
                if (p instanceof AlienShip)
                {
                    g.setColor(Color.GREEN);
                }

                if (p instanceof Asteroid)
                {
                    g.setColor(Color.ORANGE);
                }
                if (p instanceof Ship)
                {
                    if (p.equals(controller.getShip()))
                    {
                        g.setColor(Color.PINK);
                    }

                    else if (p.equals(controller.getShipP2()))
                    {
                        g.setColor(Color.CYAN);
                    }
                }
            }
            p.draw(g);
            g.setColor(Color.WHITE);

        }

        // in-game HUD
        if (!legend.equals("ASTEROIDS"))
            drawHUD(g);
    }

    /**
     * Drawing the HUD for the game
     * 
     * @param g
     */
    private void drawHUD (Graphics2D g)
    {
        
        // Non-enhanced HUD
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        g.drawString("" + controller.getScore(), 10, 45);
        if (!controller.getEnhanced())
            g.drawString("" + controller.getLevel(), SIZE - 40, 45);
        else {  // Enhanced HUD
            drawEnhancedHUD(g);
        }

        // Drawing life count
        g.translate(-10, 75);
        g.rotate(-Math.PI * .5);
        for (int i = 0; i < controller.getLives(); i++)
        {
            g.translate(0, 30);
            g.draw(Ship.createOutline());
        }
        
        if (controller.getEnhanced()) {
            g.translate(0, -30 * controller.getLives() +1);
            
            // Drawing life count
            g.translate(0, SIZE+15);
            for (int i = 0; i < controller.getLivesP2(); i++)
            {
                g.translate(0, -30);
                g.draw(Ship.createOutline());
            }
        }
    }

    /**
     * Drawing the enhanced HUD for the game
     * 
     * @param g
     */
    private void drawEnhancedHUD (Graphics2D g)
    {
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        // High Score
        if (legend.equals(GAME_OVER))
            g.drawString("High Score: " + controller.getHighScore(), 10, SIZE - 15);
        
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        // P2 Score
        g.drawString("" + controller.getScoreP2(), SIZE - 95, 45);
        // Level
        g.drawString("Level: " + controller.getLevel(), SIZE / 2 - 37, 45);
    }
}
