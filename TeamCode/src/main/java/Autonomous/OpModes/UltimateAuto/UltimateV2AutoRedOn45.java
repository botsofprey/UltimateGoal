/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package Autonomous.OpModes.UltimateAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Autonomous.AutoAlliance;
import Autonomous.RingCount;

import static Autonomous.ConfigVariables.STARTING_ROBOT_LOCATION_RIGHT;
import static Autonomous.ConfigVariables.STARTING_ROBOT_LOCATION_RIGHT_ON_45;

/*
    An opmode for the Ultimate Goal Autonomous
 */
@Autonomous(name="UltimateV2AutoOn45", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class UltimateV2AutoRedOn45 extends LinearOpMode {
    @Override
    public void runOpMode() {

        // initialize robot // TODO: check starting location
        UltimateV2Autonomous robot = new UltimateV2Autonomous(AutoAlliance.RED, STARTING_ROBOT_LOCATION_RIGHT_ON_45, this);

        robot.init();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        try {

            waitForStart();

            telemetry.addData("Drop intake and wobble", "");
            telemetry.update();

            // start by dropping down the intake
            robot.dropIntakeAndWobble();

            RingCount ringCount = robot.driveToRingStackOn45();
//            RingCount ringCount = RingCount.SINGLE_STACK;
            // TODO one ring is the least reliable to detect, tune tolerances
            telemetry.addData("Ring Count", ringCount);

            telemetry.addData("deliver wobble goal", "");
            telemetry.update();

            // next we deliver the first wobble goal to the zone
            robot.deliverWobbleGoalOn45(ringCount, 1);
            telemetry.addData("power shots", "");
            telemetry.update();

            // following the delivery we shoot the preloaded rings at the power shot targets
            robot.performPowerShotsOn45(ringCount);

            telemetry.addData("intake rings, obtain second wobble", "");
            telemetry.update();
            // next we can intake the extra rings if there are some while we travel to the second wobble goal
            robot.obtainSecondWobbleGoalOn45(ringCount);
            robot.intakeExtraRings(ringCount);

            // after grabbing the second wobble goal, we can shoot the extra rings while travelling to deliver it
            telemetry.addData("deliver second wobble, shoot extra rings", "");
            telemetry.update();
            // todo: added wobbleNum parameter to differentiate between the first and second wobble goals
            robot.shootExtraRings(ringCount);
            robot.deliverWobbleGoalOn45(ringCount, 2);


            telemetry.addData("park", "");
            telemetry.update();
            // finally we park
            robot.parkOn45(ringCount);

            // run until the end of the match (driver presses STOP)
            while (opModeIsActive());
        } finally {
            robot.kill();
        }
    }
}
