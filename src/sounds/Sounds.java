package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sounds
{
    /**
     * Individual Clip objects for each sound file
     */
    private Clip bangAS, bangL, bangM, bangShip, bangS, beat1, beat2,
                fire, saucerB, saucerS, thrust;
    
    /**
     * Constructor loads sound files to individual Clip objects to be played
     */
    public Sounds ()
    {
        bangAS = createClip("/sounds/bangAlienShip.wav");
        bangL = createClip("/sounds/bangLarge.wav");
        bangM = createClip("/sounds/bangMedium.wav");
        bangShip = createClip("/sounds/bangShip.wav");
        bangS = createClip("/sounds/bangSmall.wav");
        beat1 = createClip("/sounds/beat1.wav");
        beat2 = createClip("/sounds/beat2.wav");
        fire = createClip("/sounds/fire.wav");
        saucerB = createClip("/sounds/saucerBig.wav");
        saucerS = createClip("/sounds/saucerSmall.wav");
        thrust = createClip("/sounds/thrust.wav");
    }
    
    /**
     * Creates Clip from .wav file
     * 
     * @param soundFile disc url
     * @return Clip containing the soundFile
     */
    private Clip createClip (String soundFile)
    {
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        } catch (LineUnavailableException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (UnsupportedAudioFileException e) {
            return null;
        }
    }
    
    /**
     * Stops a specified sound
     * 
     * @param s name of sound file as listed in sounds package
     */
    public void stopSound (String s)
    {
        // saucerBig sound
        if (s.equals("saucerBig") && saucerB.isRunning()) {
            saucerB.stop();
            saucerB.setFramePosition(0);
        }
        
        // saucerSmill sound
        if (s.equals("saucerSmall") && saucerS.isRunning()) {
            saucerS.stop();
            saucerS.setFramePosition(0);
        }
        
        // thrust sound
        if (s.equals("thrust") && thrust.isRunning()) {
            thrust.stop();
            thrust.setFramePosition(0);
        }
    }
    
    /**
     * Plays a specified
     * 
     * @param s name of the sound file as listed in sounds package
     */
    public void playSound (String s)
    {
        // bangAlienShip sound
        if (s.equals("bangAlienShip")) {
            bangAS.setFramePosition(0);
            bangAS.start();
        }
        
        // bangLarge sound
        if (s.equals("bangLarge")) {
            bangL.setFramePosition(0);
            bangL.start();
        }
        
        // bangMedium sound
        if (s.equals("bangMedium")) {
            bangM.setFramePosition(0);
            bangM.start();
        }
        
        // bangSmall sound
        if (s.equals("bangSmall")) {
            bangS.setFramePosition(0);
            bangS.start();
        }
        
        // bangShip sound
        if (s.equals("bangShip")) {
            bangShip.setFramePosition(0);
            bangShip.start();
        }
        
        // beat1 sound
        if (s.equals("beat1")) {
            beat1.setFramePosition(0);
            beat1.start();
        }
        
        // beat2 sound
        if (s.equals("beat2")) {
            beat2.setFramePosition(0);
            beat2.start();
        }
        
        // Fire sound
        if (s.equals("fire")) {
            if (fire.isRunning())
            {
                fire.stop();
            }
            fire.setFramePosition(0);
            fire.start();
        }
        
        // saucerBig sound
        if (s.equals("saucerBig")) {
            if (saucerB.isRunning())
            {
                saucerB.stop();
            }
            saucerB.setFramePosition(0);
            saucerB.loop(Clip.LOOP_CONTINUOUSLY);  
        }
        
        // saucerSmall sound
        if (s.equals("saucerSmall")) {
            if (saucerS.isRunning())
            {
                saucerS.stop();
            }
            saucerS.setFramePosition(0);
            saucerS.loop(Clip.LOOP_CONTINUOUSLY);  
        }
        
        // thrust sound
        if (s.equals("thrust")) {
            thrust.loop(Clip.LOOP_CONTINUOUSLY);  
        }
    }
}
