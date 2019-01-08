/**
 * 
 */
package le2lojosev3.robots.puppy;

import java.util.logging.Level;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.BrickButtons;
import le2lejosev3.pblocks.BrickStatusLight;
import le2lejosev3.pblocks.ColorSensor;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.MediumMotor;
import le2lejosev3.pblocks.MoveTank;
import le2lejosev3.pblocks.MoveTankUnregulated;
import le2lejosev3.pblocks.Random;
import le2lejosev3.pblocks.Sound;
import le2lejosev3.pblocks.Timer;
import le2lejosev3.pblocks.TouchSensor;
import le2lejosev3.pblocks.Wait;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Puppy
 * 
 * @author Roland Blochberger
 */
public class Puppy {

	private static Class<?> clazz = Puppy.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortA = MotorPort.A; // Large Motor
	static final Port motorPortC = MotorPort.C; // Medium Motor
	static final Port motorPortD = MotorPort.D; // Large Motor
	static final Port touchPort1 = SensorPort.S1; // Touch Sensor
	static final Port colorPort1 = SensorPort.S4; // Color Sensor

	// the motor:
	private static final MediumMotor motC = new MediumMotor(motorPortC);

	// move tank block with both large motors
	// NOTE: The original LEGO icon-based program uses the Unregulated Motor once in
	// the UP Block to move the puppy legs. I assume this gives the movement a less
	// robotic but more natural appearance. With the current release of the
	// Le2lejosev3 library I cannot mix the MoveTank and single unregulated motor
	// blocks, therefore I just use the MoveTank block in the whole program.
	private static final MoveTank move = new MoveTank(motorPortA, motorPortD);

	// the sensors
	private static final TouchSensor touch = new TouchSensor(touchPort1);
	private static final ColorSensor color = new ColorSensor(colorPort1);

	// Types of LCD images
	private static final int IS_NEUTRAL = 0;
	private static final int IS_SLEEPING = 1;
	private static final int IS_TEAR = 2;
	private static final int IS_HURT = 3;
	private static final int IS_ANGRY = 4;
	private static final int IS_TIRED_MIDDLE = 5;
	private static final int IS_TIRED_RIGHT = 6;
	private static final int IS_TIRED_LEFT = 7;
	private static final int IS_LOVE = 8;

	// variables
	// ISS
	private static int iss = 0;
	// P_T
	private static int p_t = 0;
	// F_T
	private static int f_t = 0;
	// P_C
	private static int p_c = 0;
	// F_C
	private static int f_c = 0;
	// DB_S
	private static int db_s = 0;
	// NS
	private static boolean ns = false;
	// IBP
	private static float ibp = 0F;
	// IAP
	private static float iap = 0F;
	// _C
	private static boolean _c = false;
	// OTC
	private static boolean otc = false;
	// TC
	private static boolean tc = false;
	// OCOL
	private static int ocol = 0;
	// COL
	private static int col = 0;
	// _R (local in one block only)
	// private static int _r = 0;
	// stateChanged (not used)
	// private static boolean stateChanged = false;
	// GTO
	private static float gto = 0;

	/**
	 * Main program entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file for all levels
		Setup.log2File(clazz, Level.ALL);
		// setup logging to file
		// Setup.log2File(clazz);
		log.fine("Starting ...");

		// call DN Block
		dN();
		// call MNRH Block
		mNRH();
		// check if brick button left is pressed
		boolean leftPressed = (BrickButtons.measure() == BrickButtons.BB_LEFT);
		// call IS Block with type 1
		iS(IS_SLEEPING);
		// call UP Block
		uP();
		// call RST Block
		rST();
		// BHV Loop
		while (Button.ESCAPE.isUp()) {
			// call MON Block
			mON();
			// switch DB_S
			switch (db_s) {
			case 1:
				// call SLP Block
				sLP();
				break;
			case 2:
				// call PLF Block
				pLF();
				break;
			case 3:
				// call NGR Block
				nGR();
				break;
			case 4:
				// call HNG Block
				hNG();
				break;
			case 5:
				// call PPP Block
				pPP();
				break;
			case 6:
				// call HPY Block
				hPY();
				break;
			case 7:
				// call WKU Block
				wKU();
				break;
			case 0:
			default:
				// call IDL Block
				iDL();
				break;
			}
			// call _DBG Block with enable = leftPressed
			_DBG(leftPressed);
		}

		log.fine("The End");
	}

	/**
	 * PLF Block
	 */
	private static void pLF() {
		// check NS flag
		if (ns) {
			// reset NS flag
			ns = false;
			// call IS Block with state 0
			iS(IS_NEUTRAL);
			// call UP Block
			uP();
			// Play sound file "Dog bark 2" with volume 100 and wait until done
			Sound.playFile("Dog bark 2", 100, Sound.WAIT);
			// Timer 4 reset
			Timer.reset(4);
			// generate random number between 4 and 8 and store it in the GTO variable
			gto = Random.numeric(4F, 8F);
		}
		// call PTC Block
		if (pTC()) {
			// call CS Block with state 0
			cS(0);
		}
		// Compare Timer 4 value with GTO variable
		if (Timer.measure(4) > gto) {
			// Play sound file "Dog bark 2" with volume 100 and wait until done
			Sound.playFile("Dog bark 2", 100, Sound.WAIT);
			// Timer 4 reset
			Timer.reset(4);
			// generate random number between 4 and 8 and store it in the GTO variable
			gto = Random.numeric(4F, 8F);
		}
	}

	/**
	 * NGR Block (Anger)
	 */
	private static void nGR() {
		// reset NS flag
		ns = false;
		// call IS Block with state 4
		iS(IS_ANGRY);
		// Play sound file "Dog growl" with volume 100 and wait until done
		Sound.playFile("Dog growl", 100, Sound.WAIT);
		// call UP Block
		uP();
		// Wait 1.5 seconds
		Wait.time(1.5F);
		// Stop Sound
		Sound.stop();
		// Play sound file "Dog bark 1" with volume 100 and wait until done
		Sound.playFile("Dog bark 1", 100, Sound.WAIT);
		// Decrement variable P_C
		p_c--;
		// call CS Block with state 0
		cS(0);
	}

	/**
	 * HNG Block (Hunger)
	 */
	private static void hNG() {
		// check NS flag
		if (ns) {
			// reset NS flag
			ns = false;
			// call IS Block with state 3
			iS(IS_HURT);
			// call DN Block
			dN();
			// Play sound file "Dog whine" with volume 100 and wait until done
			Sound.playFile("Dog whine", 100, Sound.WAIT);
		}
		// call FDC Block
		if (fDC()) {
			// call CS Block with state 0
			cS(0);
		}
		// call PTC Block
		if (pTC()) {
			// call CS Block with state 3
			cS(3);
		}
	}

	/**
	 * PPP Block (Pee)
	 */
	private static void pPP() {
		// reset NS flag
		ns = false;
		// call IS Block with state 2
		iS(IS_TEAR);
		// call UP Block
		uP();
		// Wait 0.1 seconds
		Wait.time(0.1F);
		log.fine("Move right leg only");
		// Large motor A on for 70 degrees with power -30 and brake at end
		move.getLeftMotor().motorOnForDegrees(-30, 70, true);
		// Wait 0.8 seconds
		Wait.time(0.8F);
		// Play sound file "Horn 1" with volume 100 and wait until done
		Sound.playFile("Horn 1", 100, Sound.WAIT);
		// Wait 1 second
		Wait.time(1F);
		// P Loop
		for (int i = 0; (i < 3) && Button.ESCAPE.isUp(); i++) {
			// Large motor A on for 20 degrees with power -30 and brake at end
			move.getLeftMotor().motorOnForDegrees(-30, 20, true);
			// Large motor A on for 20 degrees with power 30 and brake at end
			move.getLeftMotor().motorOnForDegrees(30, 20, true);
		}
		// Large motor A on for 70 degrees with power 30 and brake at end
		move.getLeftMotor().motorOnForDegrees(30, 70, true);
		// set variable F_C
		f_c = 1;
		// call CS Block with state 0
		cS(0);
	}

	/**
	 * HPY Block (Happy)
	 */
	private static void hPY() {
		// call IS Block with state 8
		iS(IS_LOVE);
		// call MHT Block with position 0
		mHT(0);
		// Tank Move (motors A + D) on for 0.8 seconds with power 10, 10 and brake at
		// end
		move.motorsOnForSeconds(10, 10, 0.8F, true);
		// H Loop
		for (int i = 0; (i < 3) && Button.ESCAPE.isUp(); i++) {
			// Play sound file "Dog bark 1" with volume 100 once in the background
			Sound.playFile("Dog bark 1", 100, Sound.ONCE);
			// Tank Move (motors A + D) on for 0.2 seconds with power -100, -100 and brake
			// at end
			move.motorsOnForSeconds(-100, -100, 0.2F, true);
			// Wait 0.3 seconds
			Wait.time(0.3F);
			// Tank Move (motors A + D) on for 0.3 seconds with power 10, 10 and brake at
			// end
			move.motorsOnForSeconds(10, 10, 0.3F, true);
		}
		// Wait 0.5 seconds
		Wait.time(0.5F);
		// Stop Sound
		Sound.stop();
		// call DN Block
		dN();
		// call RST Block
		rST();
	}

	/**
	 * WKU Block
	 */
	private static void wKU() {
		// reset stateChanged flag
		// stateChanged = false;
		// call IS Block with state 5
		iS(IS_TIRED_MIDDLE);
		// Play sound file "Dog whine" with volume 100 once in the background
		Sound.playFile("Dog whine", 100, Sound.ONCE);
		// call MHT Block with position 0
		mHT(0);
		// call DN Block
		dN();
		// call STL Block
		sTL();
		// Wait 1 second
		Wait.time(1F);
		// call UP Block
		uP();
		// call CS Block with state 0
		cS(0);
	}

	/**
	 * STL Block
	 */
	private static void sTL() {
		// call UP Block
		uP();
		// Tank Move (motors A + D) on for 60 degrees with power -20, -20 and brake at
		// end
		move.motorsOnForDegrees(-20, -20, 60, true);
		// Play sound file "Dog whine" with volume 100 and wait until done
		Sound.playFile("Dog whine", 100, Sound.WAIT);
		// Tank Move (motors A + D) on for 60 degrees with power 20, 20 and brake at end
		move.motorsOnForDegrees(20, 20, 60, true);
	}

	/**
	 * SLP Block (Sleep)
	 */
	private static void sLP() {
		// check NS flag
		if (ns) {
			ns = false;
			// call IS Block with state 5
			iS(IS_TIRED_MIDDLE);
			// call DN Block
			dN();
			// call MHT Block with position 3000
			mHT(3000);
			// call IS Block with state 1
			iS(IS_SLEEPING);
			// Play sound file "Snoring" with volume 100 and repeat
			Sound.playFile("Snoring", 100, Sound.REPEAT);
		}
		// measure touch sensor state and brick buttons
		if (touch.compareState(TouchSensor.PRESSED) || (BrickButtons.measure() == BrickButtons.BB_CENTER)) {
			// Sound stop
			Sound.stop();
			// Reset timer 3
			Timer.reset(3);
			// call CS Block with state 7
			cS(7);
		}
	}

	/**
	 * MHT Block (move head)
	 * 
	 * @param pos
	 */
	private static void mHT(int pos) {
		// From the parameter subtract the measured motor C rotation and store result in
		// variable _R
		int _r = (pos - motC.measureDegrees());
		log.fine("Move head " + _r + " degrees");
		// compare _R
		if (_r >= 0) {
			// Motor C on for _r degrees with power 100 and brake at end
			motC.motorOnForDegrees(100, _r, true);

		} else {
			// Motor C on for abs(_r) degrees with power -100 and brake at end
			motC.motorOnForDegrees(-100, Math.abs(_r), true);
		}
	}

	/**
	 * IDL Block
	 */
	private static void iDL() {
		// toggle NS flag
		if (ns) {
			ns = false;
			// call UP Block
			uP();
		}
		// call UIS Block
		uIS();
		// call UPDB Block
		uPDB();
		// call PTC Block
		pTC();
		// call FDC Block
		fDC();
	}

	/**
	 * UIS Block
	 */
	private static void uIS() {
		// Compare Timer 5 value with IBP variable
		if (Timer.measure(5) > ibp) {
			// Reset Timer 5
			Timer.reset(5);
			// check ISS variable
			if (iss == IS_SLEEPING) {
				// set ISS 6
				iss = IS_TIRED_RIGHT;
				// generate random number between 1 and 5 and store it in the IBP variable
				ibp = Random.numeric(1F, 5F);

			} else {
				// set ISS 1
				iss = IS_SLEEPING;
				// set ibp 0.25
				ibp = 0.25F;
			}
			// call IS Block with state from variable ISS
			iS(iss);
		}
		// Compare Timer 6 value with IAP variable
		if (Timer.measure(6) > iap) {
			// check ISS variable
			if (iss != IS_SLEEPING) {
				// Reset Timer 6
				Timer.reset(6);
				// generate random number between 1 and 10 and store it in the IAP variable
				iap = Random.numeric(1F, 10F);
				// check ISS variable
				if (iss != IS_TIRED_LEFT) {
					// set ISS 7
					iss = IS_TIRED_LEFT;
				} else {
					// set ISS 6
					iss = IS_TIRED_RIGHT;
				}
				// call IS Block with state from variable ISS
				iS(iss);
			}
		}
	}

	/**
	 * UPDB Block
	 */
	private static void uPDB() {
		// compare P_C and P_T and F_C and F_T
		if ((p_c == p_t) && (f_c == f_t)) {
			// call CS Block with state 6
			cS(6);
		}
		// compare P_C and P_T and F_C and F_T
		if ((p_c > p_t) && (f_c < f_t)) {
			// call CS Block with state 3
			cS(3);
		}
		// compare P_C and P_T and F_C and F_T
		if ((p_c < p_t) && (f_c > f_t)) {
			// call CS Block with state 5
			cS(5);
		}
		// compare P_C with 0 and F_C with 0
		if ((p_c == 0) && (f_c > 0)) {
			// call CS Block with state 2
			cS(2);
		}
		// compare F_C with 0
		if (f_c == 0) {
			// call CS Block with state 4
			cS(4);
		}
	}

	/**
	 * PTC Block
	 * 
	 * @return _C
	 */
	private static boolean pTC() {
		// clear _C
		_c = false;
		// set OTC from TC
		otc = tc;
		// measure state of touch sensor
		tc = touch.measureState();
		// compare
		if ((tc != otc) && tc) {
			// Increment P_C
			p_c++;
			// Reset Timer 3
			Timer.reset(3);
			// compare DB_S
			if (db_s != 4) {
				// call IS Block with state 2
				iS(IS_TEAR);
				// Play sound file "Dog sniff" with volume 100 and wait until done
				Sound.playFile("Dog sniff", 100, Sound.WAIT);
			}
			// set _C
			_c = true;
		}
		// return -C
		return _c;
	}

	/**
	 * FDC Block (Feeding)
	 * 
	 * @return _C
	 */
	private static boolean fDC() {
		// set OCOL from COL
		ocol = col;
		// measure color
		col = color.measureColor();
		// clear _C
		_c = false;
		// compare
		if ((col != 0) && (ocol != col)) {
			// Increment F_C
			f_c++;
			// set _C
			_c = true;
			// Reset Timer 3
			Timer.reset(3);
			// call IS Block with state 2
			iS(IS_TEAR);
			// Play sound file "Crunching" with volume 100 and wait until done
			Sound.playFile("Crunching", 100, Sound.WAIT);
		}
		// return -C
		return _c;
	}

	/**
	 * DN Block (Lay down)
	 */
	private static void dN() {
		log.fine("move both legs 1 second");
		// Move Tank (motors A + D) on for 1 second, power left 10, power right 10, do
		// not brake at end
		move.motorsOnForSeconds(10, 10, 1F, false);
		// Wait 0.1 seconds
		Wait.time(0.1F);
		// Reset motor rotation for both move tank motors
		move.rotationResetLeft();
		move.rotationResetRight();
	}

	/**
	 * MNRH Block (adjust head position)
	 */
	private static void mNRH() {
		log.fine("");
		// Display image "EV3 icon" at coords 0,0 and clear screen before
		Display.image("EV3 icon", true, 0, 0);
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
	 * IS Block (display image on LCD)
	 * 
	 * @param type
	 */
	private static void iS(int type) {
		// log.fine("type: " + type);
		// write type to ISS variable
		iss = type;
		switch (type) {
		case IS_SLEEPING:
			// display image "Sleeping" on coordinates 0,0 and clear screen before
			Display.image("Sleeping", true, 0, 0);
			log.fine("Sleeping");
			break;
		case IS_TEAR:
			// display image "Tear" on coordinates 0,0 and clear screen before
			Display.image("Tear", true, 0, 0);
			// display shape rectangle at 135, 63, width 25, height 35, fill with white
			// color, no clear screen before (remove the tear drop)
			Display.shapesRectangle(false, 135, 63, 25, 35, true, Display.COLOR_WHITE);
			log.fine("Tear");
			break;
		case IS_HURT:
			// display image "Hurt" on coordinates 0,0 and clear screen before
			Display.image("Hurt", true, 0, 0);
			log.fine("Hurt");
			break;
		case IS_ANGRY:
			// display image "Angry" on coordinates 0,0 and clear screen before
			Display.image("Angry", true, 0, 0);
			log.fine("Angry");
			break;
		case IS_TIRED_MIDDLE:
			// display image "Tired middle" on coordinates 0,0 and clear screen before
			Display.image("Tired middle", true, 0, 0);
			log.fine("Tired middle");
			break;
		case IS_TIRED_RIGHT:
			// display image "Tired right" on coordinates 0,0 and clear screen before
			Display.image("Tired right", true, 0, 0);
			log.fine("Tired right");
			break;
		case IS_TIRED_LEFT:
			// display image "Tired left" on coordinates 0,0 and clear screen before
			Display.image("Tired left", true, 0, 0);
			log.fine("Tired left");
			break;
		case IS_LOVE:
			// display image "Love" on coordinates 0,0 and clear screen before
			Display.image("Love", true, 0, 0);
			log.fine("Love");
			break;
		case IS_NEUTRAL:
		default:
			// display image "Neutral" on coordinates 0,0 and clear screen before
			Display.image("Neutral", true, 0, 0);
			log.fine("Neutral");
			break;
		}
	}

	/**
	 * UP Block (Stand up)
	 */
	private static void uP() {
		log.fine("");
		// Motor A Rotation compare > -50 degrees
		if (move.measureDegreesLeft() > -50) {
			log.fine("move both legs");
			// NOTE: the original icon-based UP Block has 2 parallel threads, each for one
			// large motor that do the same sequence. We use the MoveTank block instead:
			// reset both motor rotation blocks
			move.rotationResetLeft();
			move.rotationResetRight();
			// Both Motors on with power -35
			// NOTE: Using the MoveTank block lets the puppy jump so it falls on its nose,
			// whereas the MoveTankUnregulated block is not strong enough to lift it up :-(
			if (MoveTankUnregulated.class.isInstance(move)) {
				move.motorsOn(-55, -55);
			} else {
				move.motorsOn(-25, -25);
			}
			// Wait until motor rotation has reached -25 degrees
			while (Button.ESCAPE.isUp()) {
				if (move.measureDegreesLeft() < -25) {
					break;
				}
			}
			// Stop both motors but don't brake
			move.motorsOff(false);
			// NOTE: the original icon-based UP Block now uses Unregulated Motor blocks in
			// its both threads to rotate -65 degrees and then brake. We still use the Move
			// Tank block instead:
			// Both Motors on with power -15
			if (MoveTankUnregulated.class.isInstance(move)) {
				move.motorsOn(-35, -35);
			} else {
				move.motorsOn(-10, -10);
			}
			// Wait until motor rotation has reached -65 degrees
			while (Button.ESCAPE.isUp()) {
				if (move.measureDegreesLeft() < -65) {
					break;
				}
			}
			// Stop both motors and brake
			move.motorsOff(true);

			// Wait 0.5 seconds
			Wait.time(0.5F);
			// log.fine("move both legs done");
		}
	}

	/**
	 * RST Block (Reset)
	 */
	private static void rST() {
		log.fine("");
		// generate random integer between 3 and 6 and store it in the p_t variable
		p_t = Random.numeric(3, 6);
		// generate random integer between 2 and 4 and store it in the f_t variable
		f_t = Random.numeric(2, 4);
		// initialize p_c and f_c variables
		p_c = 1;
		f_c = 1;
		// reset Timers 1, 2, 3
		Timer.reset(1);
		Timer.reset(2);
		Timer.reset(3);
		// call CS Block with state 0
		cS(0);
	}

	/**
	 * CS Block
	 * 
	 * @param state
	 */
	private static void cS(int state) {
		// compare DB_S variable with state
		if (db_s != state) {
			// store state in DB_S
			db_s = state;
			// set NS
			ns = true;
		}
	}

	/**
	 * MON Block
	 */
	private static void mON() {
		// log.fine("");
		// Compare Timer 2 > 10 seconds
		if (Timer.measure(2) > 10F) {
			// Reset Timer 2
			Timer.reset(2);
			// Decrement variable P_C
			p_c--;
			// if variable P_C < 0, set it 0
			if (p_c < 0) {
				p_c = 0;
			}
		}
		// Compare Timer 1 > 20 seconds
		if (Timer.measure(1) > 20F) {
			// Reset Timer 1
			Timer.reset(1);
			// Decrement variable F_C
			f_c--;
			// if variable F_C < 0, set it 0
			if (f_c < 0) {
				f_c = 0;
			}
		}
		// Compare Timer 3 > 30 seconds
		if (Timer.measure(3) > 30F) {
			// Reset Timer 3
			Timer.reset(3);
			// call CS Block with state 1
			cS(1);
		}
	}

	/**
	 * _DBG Block (debug display)
	 * 
	 * @param enable
	 */
	private static void _DBG(boolean enable) {
		// check parameter
		if (enable) {
			log.fine("enabled");
			// Display DB_S Variable on Text LCD at 0,0 with color black and normal font, do
			// not clear screen before
			String text = "DBS=" + db_s;
			Display.textGrid(text, false, 0, 0, Display.COLOR_BLACK, Display.FONT_NORMAL);
			// Display F_C and F_T variables on Text LCD at 0,1 with color black and normal
			// font, do not clear screen before
			text = "F=" + f_c + "/" + f_t;
			Display.textGrid(text, false, 0, 1, Display.COLOR_BLACK, Display.FONT_NORMAL);
			// Display P_C and P_T variables on Text LCD at 0,2 with color black and normal
			// font, do not clear screen before
			text = "P=" + p_c + "/" + p_t;
			Display.textGrid(text, false, 0, 2, Display.COLOR_BLACK, Display.FONT_NORMAL);
		}
	}
}
