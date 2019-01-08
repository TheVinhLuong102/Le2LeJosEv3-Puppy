/**
 * 
 */
package le2lojosev3.robots.puppy;

import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.MoveTank;
import le2lejosev3.pblocks.Wait;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;

/**
 * Puppy.
 * Tests for motor moves.
 * 
 * @author Roland Blochberger
 */
public class PuppyMoveLegsTest {

	private static Class<?> clazz = PuppyMoveLegsTest.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortA = MotorPort.A; // Large Motor
	static final Port motorPortD = MotorPort.D; // Large Motor

	// move tank block with both large motors
	private static final MoveTank move = new MoveTank(motorPortA, motorPortD);
	// private static final MoveTankUnregulated move = new
	// MoveTankUnregulated(motorPortA, motorPortD);

	// NOTE: Using the MoveTank block lets the puppy jump so it falls on its nose,
	// whereas the MoveTankUnregulated block is not strong enough to lift it up :-(

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		// Setup.log2File(clazz, Level.ALL);
		// setup logging to file
		Setup.log2File(clazz);
		log.info("Starting ...");

		Display.textGrid("Move both legs dn", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		down();
		Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Button.waitForAnyPress();

		Display.textGrid("Move both legs up", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		uP();
		Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Button.waitForAnyPress();

		Display.textGrid("Move both legs   ", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		stl();
		Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Button.waitForAnyPress();

		Display.textGrid("Move right leg   ", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		moveRightLegOnly();
		Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Button.waitForAnyPress();

		Display.textGrid("Move both legs   ", true, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		happy();
		Display.textGrid("Press Button", false, 0, 5, Display.COLOR_BLACK, Display.FONT_NORMAL);
		Button.waitForAnyPress();

		// go back to neutral position
		down();

		log.info("The End");
	}

	/**
	 * Part of DN Block
	 */
	private static void down() {
		log.info("");
		// MoveTank (motors A + D) on for 1 second, power left 10, power right 10, do
		// not brake at end
		log.info("OnForSeconds 10, 10, 1s");
		move.motorsOnForSeconds(10, 10, 1F, false);
	}

	/**
	 * UP Block
	 */
	private static void uP() {
		log.info("");
		int dgl = 0;
		// Motor A Rotation compare > -50 degrees
		if ((dgl = move.measureDegreesLeft()) > -50) {
			log.info("move both legs - dgl: " + dgl);
			// reset both motor rotation blocks
			move.rotationResetLeft();
			move.rotationResetRight();
			// Both Motors on with power -35
			log.info("On -35, -35");
			move.motorsOn(-35, -35);
			// Wait until motor rotation has reached -25 degrees
			while (Button.ESCAPE.isUp()) {
				dgl = move.measureDegreesLeft();
				log.info(" dgl: " + dgl);
				if (dgl < -25) {
					break;
				}
			}
			// Stop both motors but don't brake
			move.motorsOff(false);
			log.info("Off");

			// Both Motors on with power -15
			log.info("On -15, -15");
			move.motorsOn(-15, -15);
			// Wait until motor rotation has reached -65 degrees
			while (Button.ESCAPE.isUp()) {
				dgl = move.measureDegreesLeft();
				log.info(" dgl: " + dgl);
				if (dgl < -65) {
					break;
				}
			}
			// Stop both motors and brake
			move.motorsOff(true);
			log.info("Off");
		}
	}

	/**
	 * Part of STL Block
	 */
	private static void stl() {
		log.info("");
		// Tank Move (motors A + D) on for 60 degrees with power -20, -20 and brake at
		// end
		log.info("OnForDegr -20, -20, 60");
		move.motorsOnForDegrees(-20, -20, 60, true);
		// Wait 0.5 seconds
		Wait.time(0.5F);
		// Tank Move (motors A + D) on for 60 degrees with power 20, 20 and brake at end
		log.info("OnForDegr 20, 20, 60");
		move.motorsOnForDegrees(20, 20, 60, true);
	}

	/**
	 * Move right leg only (part of PPP Block).
	 */
	private static void moveRightLegOnly() {
		log.info("");
		// Large motor A on for 70 degrees with power -30 and brake at end
		log.info("OnForDegr -30, 70");
		move.getLeftMotor().motorOnForDegrees(-30, 70, true);
		// Wait 0.5 seconds
		Wait.time(0.5F);
		// P Loop
		for (int i = 0; (i < 3) && Button.ESCAPE.isUp(); i++) {
			// Large motor A on for 20 degrees with power -30 and brake at end
			log.info("OnForDegr -30, 20");
			move.getLeftMotor().motorOnForDegrees(-30, 20, true);
			// Large motor A on for 20 degrees with power 30 and brake at end
			log.info("OnForDegr 30, 20");
			move.getLeftMotor().motorOnForDegrees(30, 20, true);
		}
		// Large motor A on for 70 degrees with power 30 and brake at end
		log.info("OnForDegr 30, 70");
		move.getLeftMotor().motorOnForDegrees(30, 70, true);
	}

	/**
	 * Part of HPY Block
	 */
	private static void happy() {
		log.info("");
		// Tank Move (motors A + D) on for 0.8 seconds with power 10, 10 and brake at
		// end
		log.info("OnForSec 10, 10, 0.8s");
		move.motorsOnForSeconds(10, 10, 0.8F, true);
		// H Loop
		for (int i = 0; (i < 3) && Button.ESCAPE.isUp(); i++) {
			// Tank Move (motors A + D) on for 0.2 seconds with power -100, -100 and brake
			// at end
			log.info("OnForSec -100, -100, 0.2s");
			move.motorsOnForSeconds(-100, -100, 0.2F, true);
			// Wait 0.3 seconds
			Wait.time(0.3F);
			// Tank Move (motors A + D) on for 0.3 seconds with power 10, 10 and brake at
			// end
			log.info("OnForSec 10, 10, 0.3s");
			move.motorsOnForSeconds(10, 10, 0.3F, true);
		}
	}

}
