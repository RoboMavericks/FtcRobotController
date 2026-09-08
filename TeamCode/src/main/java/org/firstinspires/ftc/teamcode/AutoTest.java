package org.firstinspires.ftc.teamcode;

//hellooooooooooooooooooooooooooooooooooooooooo kabir is testing again
//hello again Sanskruth

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//import org.firstinspires.ftc.teamcode.drawing;
//import org.firstinspires.ftc.teamcode.pedroPathing.Tuning;
//import com.pedropathing.ivy.Command;
//import com.pedropathing.ivy.behaviors.*;

@Autonomous
public class AutoTest extends OpMode {

    private Follower follower;
    private ElapsedTime timer;
    //private drawing draw;


    public enum State{
        PATH_1,
        //PATH_2,
        DONE
    }
    private State currentState = State.PATH_1;

    private final Pose startPose = new Pose(72,72, Math.toRadians(0));
    private final Pose endPose = new Pose(30,30, Math.PI);
    private final Pose curvePose = new Pose(120,30);

    private PathChain pathChain1;
    private PathChain pathChain2;



    @Override
    public void init(){

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        timer = new ElapsedTime();

        pathChain1 = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .addPath(new BezierCurve(endPose, curvePose, startPose))
                .setLinearHeadingInterpolation(endPose.getHeading(), startPose.getHeading())
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
        drawing.drawDebug(follower);

        //Need to replace with IVY
        /*
        switch (currentState) {
            case PATH_1:
                if (!follower.isBusy()){
                    timer.reset();
                    //follower.followPath(pathChain2);
                    currentState = State.DONE;
                }
                break;
            case PATH_2:
                if (!follower.isBusy()){
                    timer.reset();
                    currentState = State.PATH_1;
                }
                break;

            case DONE:
                telemetry.addData("Status", "Auto Finished!");
                stop();
                break;

        }
        */
        telemetry.addData("State", currentState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading (deg)", Math.toDegrees(follower.getPose().getHeading()));
        currentState = State.PATH_1;
    }
}
