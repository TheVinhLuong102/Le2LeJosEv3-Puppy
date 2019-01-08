/**
 * 
 */
package le2lojosev3.robots.puppy;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.Sound;
import lejos.hardware.Button;

/**
 * Puppy.
 * Tests for playing sounds and displying images.
 * 
 * @author Roland Blochberger
 */
public class PuppySoundLcdTest {

	private static Class<?> clazz = PuppySoundLcdTest.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		Setup.log2File(clazz, Level.ALL);
		log.fine("Starting ...");

		// All sound resource names for the Puppy program
		String[] soundNames = new String[] { "Crunching", "Dog bark 1", "Dog bark 2", "Dog growl", "Dog sniff",
				"Dog whine", "Horn 1", "Snoring" };
		for (String soundName : soundNames) {
			// display filename and hint
			Display.textGrid("Playing:", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
			Display.textGrid(soundName, false, 2, 3, Display.COLOR_BLACK, Display.FONT_NORMAL);
			Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);

			// play sound file once and wait until done
			log.fine("Playing sound " + soundName);
			Sound.playFile(soundName, 100, Sound.WAIT);
			log.fine("Playing sound done");

			// Wait until button press
			Button.waitForAnyPress();
		}

		// All image resource names for the Puppy program
		String[] imageNames = new String[] { "Angry", "EV3 icon", "Hurt", "Love", "Neutral", "Sleeping", "Tear",
				"Tired left", "Tired middle", "Tired right" };
		for (String imageName : imageNames) {
			// display image in top left corner
			log.fine("Display image " + imageName);
			Display.image(imageName, true, 0, 0);
			if ("Tear".equals(imageName)) {
				// remove the tear
				Display.shapesRectangle(false, 135, 63, 25, 35, true, Display.COLOR_WHITE);
			}
			// display image name in small font in top left corner
			Display.textPixels(imageName, false, 0, 0, Display.COLOR_BLACK, Display.FONT_SMALL);
			log.fine("Display image done");

			// Wait until button press
			Button.waitForAnyPress();
		}

		log.fine("The End");
	}
}
