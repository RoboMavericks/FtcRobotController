package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp
public class GoBildaStart extends OpMode {

    private DcMotor rightDrive = null;
    private DcMotor leftDrive = null;
    private DcMotor intakeMotor = null;
    private CRServo inside = null;
    private DcMotor shootMotor = null;
    private CRServo rightIn = null;
    private CRServo leftIn = null;
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;


    @Override
    public void init() {
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        inside = hardwareMap.get(CRServo.class, "inside");
        shootMotor = hardwareMap.get(DcMotor.class, "shootMotor");
        rightIn = hardwareMap.get(CRServo.class, "rightIn");
        leftIn = hardwareMap.get(CRServo.class, "leftIn");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);


        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad1.left_stick_y > 0) {
            rightDrive.setPower(-1);
            leftDrive.setPower(-1);
            leftFrontDrive.setPower(1);
            rightFrontDrive.setPower(1);
        }

        if (gamepad1.left_stick_y < 0) {
            rightDrive.setPower(-1);
            leftDrive.setPower(-1);
            leftFrontDrive.setPower(-1);
            rightFrontDrive.setPower(-1);
        }


        if (gamepad1.left_stick_y == 0) {
            rightDrive.setPower(0);
            leftDrive.setPower(0);
            leftFrontDrive.setPower(0);
            rightFrontDrive.setPower(0);
        }
        if (gamepad1.right_stick_x == 0) {
            rightDrive.setPower(0);
            leftDrive.setPower(0);
            leftFrontDrive.setPower(0);
            rightFrontDrive.setPower(0);
        }
        if (gamepad1.right_stick_x < 0) {
            rightDrive.setPower(-1);
            leftDrive.setPower(1);
            leftFrontDrive.setPower(1);
            rightFrontDrive.setPower(-1);
        }
        if (gamepad1.right_stick_x > 0) {
            rightDrive.setPower(1);
            leftDrive.setPower(-1);
            leftFrontDrive.setPower(1);
            rightFrontDrive.setPower(-1);
        }
        if (gamepad1.crossWasReleased()) {
            intakeMotor.setPower(1);
            rightIn.setPower(-1);
            leftIn.setPower(1);
        }
        if (gamepad1.cross) {
            intakeMotor.setPower(0);
            rightIn.setPower(0);
            leftIn.setPower(0);
        }
        if (gamepad1.circle) {
            inside.setPower(-0.75);
        }
        if (!gamepad1.circle) {
            inside.setPower(0);
        }
        if (gamepad1.right_bumper) {
            shootMotor.setPower(0);
        }
        if (!gamepad1.right_bumper) {
            shootMotor.setPower(0.5);
        }

    }
}

