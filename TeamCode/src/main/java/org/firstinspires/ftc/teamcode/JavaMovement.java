package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class JavaMovement extends LinearOpMode {

    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;


    @Override
    public void runOpMode() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetry.update();
                if (gamepad1.left_stick_y < 0) {
                    frontRightMotor.setPower(0.5);
                    frontLeftMotor.setPower(-0.5);
                    backRightMotor.setPower(0.5);
                    backLeftMotor.setPower(-0.5);
                }
                if (gamepad1.left_stick_y > 0) {
                    backRightMotor.setPower(-0.5);
                    backLeftMotor.setPower(0.5);
                    frontLeftMotor.setPower(0.5);
                    frontRightMotor.setPower(-0.5);
                }
                if (gamepad1.left_stick_x < 0) {
                    frontLeftMotor.setPower(-0.5);
                    frontRightMotor.setPower(0.5);
                    backLeftMotor.setPower(0.5);
                    backRightMotor.setPower(-0.5);
                }
                if (gamepad1.left_stick_x > 0) {
                    backRightMotor.setPower(0.5);
                    backLeftMotor.setPower(-0.5);
                    frontRightMotor.setPower(-0.5);
                    frontLeftMotor.setPower(0.5);
                }
                if (gamepad1.left_stick_x == 0) {
                    frontRightMotor.setPower(0);
                    frontLeftMotor.setPower(0);
                    backRightMotor.setPower(0);
                    backLeftMotor.setPower(0);
                }
                if (gamepad1.left_stick_y == 0) {
                    backRightMotor.setPower(0);
                    backLeftMotor.setPower(0);
                    frontRightMotor.setPower(0);
                    frontLeftMotor.setPower(0);
                }
            }
        }


    }

}
