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
public class Square extends OpMode{

    private Follower follower;
    private ElapsedTime pathTimer;

    private final Pose p1 = new Pose(36,36, Math.toRadians(90));
    private final Pose p2 = new Pose(36,108, Math.toRadians(180));
    private final Pose p3 = new Pose(108,108, Math.toRadians(270));
    private final Pose p4 = new Pose(108,36, Math.toRadians(0));
    private final Pose centerPose = new Pose(72,72, Math.toRadians(180));
    private final Pose c1 = new Pose(0,144);
    private final Pose c2 = new Pose(144,0);

    private PathChain pathChain;
    private PathChain pathChain2;

    private Command main;
    private Command center;
    private Command autoSequence;


    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(p1);
        pathTimer = new ElapsedTime();

        pathChain = follower.pathBuilder()
                .addPath(new BezierLine(p1,p2))
                .setLinearHeadingInterpolation(p1.getHeading(), p2.getHeading())
                .addPath(new BezierLine(p2,p3))
                .setLinearHeadingInterpolation(p2.getHeading(), p3.getHeading())
                .addPath(new BezierLine(p3,p4))
                .setLinearHeadingInterpolation(p3.getHeading(), p4.getHeading())
                .addPath(new BezierLine(p4,p1))
                .setLinearHeadingInterpolation(p4.getHeading(), p1.getHeading())
                .build();
        pathChain2 = follower.pathBuilder()
                .addPath(new BezierCurve(p1,c1,centerPose))
                .setLinearHeadingInterpolation(p1.getHeading(), centerPose.getHeading())
                .addPath(new BezierCurve(centerPose,c2,p3))
                .setLinearHeadingInterpolation(centerPose.getHeading(), p3.getHeading())
                .build();

        main = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                    follower.followPath(pathChain);
                })
                .setExecute(() -> {
                    follower.update();
                    drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer.seconds() > 0.1 && !follower.isBusy()) || pathTimer.seconds() >= 20);
        center = Command.build()
                .setStart(() -> {
                    pathTimer.reset();
                    follower.followPath(pathChain2);
                })
                .setExecute(() -> {
                    follower.update();
                    drawing.drawDebug(follower);
                })
                .setDone(() -> (pathTimer.seconds() > 0.1 && !follower.isBusy()) || pathTimer.seconds() >= 20);
        autoSequence = sequential(
                main,
                center
        );

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        Scheduler.schedule(autoSequence);
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
