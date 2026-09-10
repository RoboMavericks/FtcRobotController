package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler; // Required to run Ivy commands
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Disabled
public class IvyAuto extends OpMode {

    private Follower follower;
    private ElapsedTime pathTimer;
    private ElapsedTime pathTimer2;

    private final Pose startPose = new Pose(72, 72, Math.toRadians(0));
    private final Pose endPose = new Pose(30, 30, Math.PI);
    private final Pose curvePose = new Pose(120, 30);
    private final Pose midPose = new Pose(120, 30, Math.PI);
    private PathChain pathChain1;
    private PathChain pathChain2;

    private Command followPathCommand;
    private Command followPathCommand2;

    int i = 0;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        pathTimer = new ElapsedTime();
        pathTimer2 = new ElapsedTime();

        // 1. Build Paths
        pathChain1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .addPath(new BezierCurve(endPose, curvePose, startPose))
                .setLinearHeadingInterpolation(endPose.getHeading(), startPose.getHeading())
                .build();

        pathChain2 = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, endPose, midPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), midPose.getHeading())
                .addPath(new BezierLine(midPose, startPose))
                .setLinearHeadingInterpolation(midPose.getHeading(), startPose.getHeading())
                .build();

        // 2. Build Ivy Command inside init() after objects exist
        followPathCommand = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                    follower.followPath(pathChain1);
                })
                .setExecute(() -> {
                    follower.update();
                    // drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer.seconds() > 0.1 && !follower.isBusy()) || pathTimer.seconds() >= 20)
                .setEnd(interrupted -> {
                    // Custom end/cleanup logic if needed
                });
        followPathCommand2 = Command.build()
                .setStart(() -> {
                    pathTimer2.reset();
                    follower.followPath(pathChain2);
                })
                .setExecute(() -> {
                    follower.update();
                    // drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer2.seconds() > 0.1 && !follower.isBusy()) || pathTimer2.seconds() >= 20)
                .setEnd(interrupted -> {
                    // Custom end/cleanup logic if needed
                });
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        // Schedule the command to run via the Scheduler
        Scheduler.schedule(followPathCommand, followPathCommand2);
        /*
        if ((pathTimer.seconds() > 0.1 && !follower.isBusy())) {
            if (i == 1) {
                //Scheduler.reset();
                stop();
            }
            i++;
            Scheduler.schedule(followPathCommand2);
        }
        */

    }

    @Override
    public void loop() {
        // Step the Ivy Scheduler
        Scheduler.execute();


        // Telemetry outputs
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Follower Busy", follower.isBusy());
        //telemetry.addData("pathTimer1", pathTimer.seconds());
        //telemetry.addData("pathTimer2", pathTimer2.seconds());
    }

    @Override
    public void stop() {
        Scheduler.reset();
    }
}