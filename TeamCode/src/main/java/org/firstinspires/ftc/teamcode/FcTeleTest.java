package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Mecanum TeleOp (OpMode)", group = "TeleOp")
public class FcTeleTest extends OpMode {

    // Declare drive motors
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;

    @Override
    public void init() {
        // Initialize the hardware variables using hardwareMap strings matching the Robot Controller config
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
        leftBackDrive   = hardwareMap.get(DcMotor.class, "leftBack");
        rightBackDrive  = hardwareMap.get(DcMotor.class, "rightBack");

        // Reverse left side motors so positive power drives the robot forward
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        // Ensure motors stop instantly when power is 0
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        // Read Gamepad 1 joysticks
        // Y joystick values are inverted in FTC SDK (up is negative), so negate them
        double drive  = -gamepad1.left_stick_y;  // Forward / Backward
        double strafe =  gamepad1.left_stick_x;  // Left / Right
        double turn   =  gamepad1.right_stick_x; // Rotation

        // Calculate individual wheel powers for Mecanum kinematics
        double leftFrontPower  = drive + strafe + turn;
        double rightFrontPower = drive - strafe - turn;
        double leftBackPower   = drive - strafe + turn;
        double rightBackPower  = drive + strafe - turn;

        // Normalize powers if any calculated speed exceeds 1.0 (100%)
        double maxPower = Math.max(
                Math.abs(leftFrontPower),
                Math.max(Math.abs(rightFrontPower),
                        Math.max(Math.abs(leftBackPower), Math.abs(rightBackPower)))
        );

        if (maxPower > 1.0) {
            leftFrontPower  /= maxPower;
            rightFrontPower /= maxPower;
            leftBackPower   /= maxPower;
            rightBackPower  /= maxPower;
        }

        // Output motor powers
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        // Telemetry readout for debugging
        telemetry.addData("Status", "Running");
        telemetry.addData("Front Motors", "Left: %.2f | Right: %.2f", leftFrontPower, rightFrontPower);
        telemetry.addData("Back Motors",  "Left: %.2f | Right: %.2f", leftBackPower, rightBackPower);
    }
}
