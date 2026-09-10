package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import static com.pedropathing.ivy.groups.Groups.sequential;

@Autonomous(name = "IvyTest", group = "Working")
public class IvyTest extends OpMode {

    private Follower follower;
    private ElapsedTime pathTimer;

    // Define Poses
    private final Pose startPose = new Pose(72, 72, Math.toRadians(0));
    private final Pose endPose = new Pose(30, 30, Math.PI);
    private final Pose curvePose = new Pose(120, 30);
    private final Pose finalPose = new Pose(60, 132, Math.toRadians(90));

    // PathChains
    private PathChain pathChain1;
    private PathChain pathChain2;

    // Commands
    private Command followPath1Command;
    private Command followPath2Command;
    private Command fullAutoSequence;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        pathTimer = new ElapsedTime();

        // 1. Build Path 1
        pathChain1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .addPath(new BezierCurve(endPose, curvePose, startPose))
                .setLinearHeadingInterpolation(endPose.getHeading(), startPose.getHeading())
                .build();

        // 2. Build Path 2
        pathChain2 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, finalPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), finalPose.getHeading())
                .build();

        // 3. Build Ivy Command for Path 1
        followPath1Command = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                    follower.followPath(pathChain1);
                })
                .setExecute(() -> {
                    follower.update();
                    drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer.seconds() > 0.1 && !follower.isBusy()) || pathTimer.seconds() >= 20);

        // 4. Build Ivy Command for Path 2
        followPath2Command = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                    follower.followPath(pathChain2);
                })
                .setExecute(() -> {
                    follower.update();
                    drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer.seconds() > 0.1 && !follower.isBusy()) || pathTimer.seconds() >= 20);

        // 5. Chain Path 1 and Path 2 in Series
        fullAutoSequence = sequential(
                followPath1Command,
                followPath2Command
        );

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        Scheduler.schedule(fullAutoSequence);
    }

    @Override
    public void loop() {
        Scheduler.execute();

        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.addData("Follower Busy", follower.isBusy());
    }

    @Override
    public void stop() {
        Scheduler.reset();
    }
}