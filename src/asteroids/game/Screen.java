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
                
                if (p instanceof Asteroid) {
                    g.setColor(Color.PINK);
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
        // Level and score HUD
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        g.drawString("" + controller.getLevel(), SIZE - 40, 45);
        g.drawString("" + controller.getScore(), 10, 45);

        // Enhanced HUD
        if (controller.getEnhanced())
            drawEnhancedHUD(g);

        // Drawing life count
        g.translate(-10, 75);
        g.rotate(-Math.PI * .5);
        for (int i = 0; i < controller.getLives(); i++)
        {
            g.translate(0, 30);
            g.draw(Ship.createOutline());
        }
    }

    /**
     * Drawing the enhanced HUD for the game
     * 
     * @param g
     */
    private void drawEnhancedHUD (Graphics2D g)
    {
        if (legend.equals(GAME_OVER))
        {
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            g.drawString("High Score: " + controller.getHighScore(), 10, SIZE - 15);
        }
    }
}
