package Actions.Ultimate;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class RingIntakeSystemV1 {

    // TODO this class should be working rn
    
    private static final int MOTOR_POWER = 1;
    
    private static final int OFF = 0;
    private static final int ON = 1;
    private static final int REVERSE = 2;
    
    private static final double[] POWERS = { 0, MOTOR_POWER, -MOTOR_POWER };
    private static final int[][] STATE_SWITCH = { { ON, REVERSE }, { OFF, REVERSE }, { ON, OFF } };
    
    private DcMotor intakeMotor;
    private int state;

    public RingIntakeSystemV1(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        state = OFF;
    }
    
    private void updateRobot() {
        intakeMotor.setPower(POWERS[state]);//todo make led lights indicate state
    }

    //tele-op function
    public void updateState(int buttonPressed) {
        state = STATE_SWITCH[state][buttonPressed];
        updateRobot();
    }
    
    // the following are used in auto
    public void intakeOn() {
        state = ON;
        updateRobot();
    }

    public void IntakeReverse() {
        state = REVERSE;
        updateRobot();
    }

    public void intakeOff() {
        state = OFF;
        updateRobot();
    }
}