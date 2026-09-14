package org.firstinspires.ftc.teamcode;


import static com.pedropathing.ivy.groups.Groups.sequential;


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

@Autonomous
public class PreRedAuto extends OpMode{

    private Follower follower;
    private ElapsedTime pathTimer;

    private final Pose p1 = new Pose(60,8, Math.toRadians(90));
    private final Pose p2 = new Pose(60,12, Math.toRadians(90));
    private final Pose p3 = new Pose(9,12, Math.toRadians(270));
    private final Pose c2 = new Pose(36,24);
    private final Pose p4 = new Pose(108,36, Math.toRadians(0));
    private final Pose centerPose = new Pose(72,72, Math.toRadians(180));

    private final Pose c1 = new Pose(144,0);

    private PathChain pathChain1;
    private PathChain pathChain2;

    private Command total;
    private Command forward1;
    private Command shoot1;
    private Command forward2;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(p1);
        pathTimer = new ElapsedTime();

        pathChain1 = follower.pathBuilder()
                .addPath(new BezierLine(p1,p2))
                .setLinearHeadingInterpolation(p1.getHeading(), p2.getHeading())
                .build();
        pathChain2 = follower.pathBuilder()
                .addPath(new BezierCurve(p2,c2,p3))
                .setLinearHeadingInterpolation(p2.getHeading(), p3.getHeading())
                .build();

        forward1 = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                    follower.followPath(pathChain1);
                })
                .setExecute(() -> {
                    follower.update();
                    drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer.seconds() > 0.1 && !follower.isBusy()) || pathTimer.seconds() >= 20);
        shoot1 = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                })
                .setExecute(() -> {
                    follower.update();
                    drawing.drawDebug(follower);
                    //shooting code
                })
                .setDone(() ->(pathTimer.seconds() > 5.0));
        forward2 = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                    follower.followPath(pathChain2);
                })
                .setExecute(() -> {
                    follower.update();
                    drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer.seconds() > 0.1 && !follower.isBusy()) || pathTimer.seconds() >= 20);

        total = sequential(
                forward1,
                shoot1,
                forward2
        );
        telemetry.addData("Status", "Initialized");
        telemetry.update();



    }

    @Override
    public void start() {
        Scheduler.schedule(total);
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


