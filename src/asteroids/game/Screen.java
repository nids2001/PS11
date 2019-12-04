package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import javax.swing.*;
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
        for (Participant p: controller)
        {
            p.draw(g);
        }
        
        // showing the level, lifecount, and score in corners
        if (!legend.equals("ASTEROIDS")) {
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
            g.drawString("" + controller.getLevel(), SIZE - 40, 45);
            g.drawString("" + controller.getScore(), 10, 45);
            
            // Drawing life count
            g.translate(-10, 75);
            g.rotate(-Math.PI * .5);
            for (int i = 0; i < controller.getLives(); i++) {
                g.translate(0, 30);
                g.draw(Ship.createOutline());
            }
            
        }
        
        
    }
}
