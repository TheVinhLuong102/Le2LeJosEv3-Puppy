/**
 * 
 */
package le2lojosev3.robots.puppy;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.BrickButtons;
import le2lejosev3.pblocks.BrickStatusLight;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.MediumMotor;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

/**
 * Puppy.
 * Tests for head motor moves.
 * 
 * @author Roland Blochberger
 */
public class PuppyMoveHeadTest {

	private static Class<?> clazz = PuppyMoveHeadTest.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortC = MotorPort.C; // Medium Motor

	// the motor:
	private static final MediumMotor motC = new MediumMotor(motorPortC);

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		Setup.log2File(clazz, Level.ALL);
		log.fine("Starting ...");

		Display.textGrid("Adjust up/dn", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Display.textGrid("Press Up / Down", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Display.textGrid("Press Center to End", false, 0, 6, Display.COLOR_BLACK, Display.FONT_NORMAL);
		mNRH();

		Display.textGrid("Move 1500 deg.", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		mHT(1500);
		Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Button.waitForAnyPress();

		Display.textGrid("Move -1500 deg.", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		mHT(-1500);
		Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Button.waitForAnyPress();

		log.fine("The End");
	}

	/**
	 * MNRH Block
	 */
	private static void mNRH() {
		log.fine("");
		// Brick status light on: color orange (1) and pulse (true)
		BrickStatusLight.on(BrickStatusLight.COLOR_ORANGE, BrickStatusLight.PULSE);

		// T Loop
		int button = 0;
		while (Button.ESCAPE.isUp()) {
			// switch on brick buttons
			button = BrickButtons.measure();
			switch (button) {
			case BrickButtons.BB_UP:
				// Medium motor C on with power -100
				motC.motorOn(-100);
				break;
			case BrickButtons.BB_DOWN:
				// Medium motor C on with power 100
				motC.motorOn(100);
				break;
			case BrickButtons.BB_NONE:
			default:
				// Medium motor C off and do not brake
				motC.motorOff(false);
				break;
			}
			// compare brick button with center
			if (button == BrickButtons.BB_CENTER) {
				// exit T Loop
				break;
			}
		}

		// Medium motor C off and do not brake
		motC.motorOff(false);
		// Reset motor rotation for medium motor C
		motC.rotationReset();
		// Brick status light on: color green (0) and no pulse (false)
		BrickStatusLight.on(BrickStatusLight.COLOR_GREEN, BrickStatusLight.CONSTANT);
	}

	/**
	 * MHT Block
	 * 
	 * @param pos the number of degrees to move the head
	 */
	private static void mHT(int pos) {
		int _r = (pos - motC.measureDegrees());
		log.fine("Move head to " + _r + " degrees");
		if (_r >= 0) {
			// Motor C on for _r degrees with power 100 and brake at end
			motC.motorOnForDegrees(100, _r, true);

		} else {
			// Motor C on for abs(_r) degrees with power -100 and brake at end
			motC.motorOnForDegrees(-100, Math.abs(_r), true);
		}
	}
}
