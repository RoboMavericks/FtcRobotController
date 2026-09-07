package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

@Autonomous
public class WorkingTest extends OpMode {

    private Follower follower;
    private ElapsedTime timer;

    public enum State{
        PATH_1,
        DONE
    }
    private State currentState = State.PATH_1;

    private final Pose startPose = new Pose(72,72, Math.toRadians(0));
    private final Pose endPose = new Pose(30,30, Math.PI);

    private PathChain pathChain1;



    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        timer = new ElapsedTime();
        pathChain1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start(){
        follower.followPath(pathChain1);
        currentState = State.PATH_1;
    }
    @Override
    public void loop(){

        follower.update();

        switch (currentState) {
            case PATH_1:
                if (!follower.isBusy()) {
                    timer.reset();
                    currentState = State.DONE;
                }
                break;
            case DONE:
                telemetry.addData("Status", "Auto Finished!");
                break;

        }
        telemetry.addData("State", currentState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
    }
}
