package com.mygdx.forkliftaone.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.forkliftaone.ForkliftModel;
import com.mygdx.forkliftaone.config.GameConfig;

//To be done: Code refactoring, disposement

public class ForkliftActorBase extends Actor {

    private World world;
    private Body forklift, rearWheel, frontWheel, fork;
    private Body[] forkliftTubes;
    private WheelJoint rearWheelJoint, frontWheelJoint;
    private PrismaticJoint[] prismaticJoints;
    private RevoluteJoint revoluteJointFork;
    private TextureRegion bodyTexture, tubeTexture, wheelTexture, forkTexture;
    private ForkliftModel model; //Not used...
    private float fuelTank;
    private boolean hasFuel;

    public ForkliftActorBase(World world, ForkliftModel model){
        this.world = world;
        this.model = model;
    }

    public void createForklift(ForkliftModel model){
        // Initializing fuel tank
        fuelTank = 100.0f;

        // Creating body
        BodyDef forkLiftBodyDef = new BodyDef();
        forkLiftBodyDef.type = BodyDef.BodyType.DynamicBody;
        forkLiftBodyDef.fixedRotation = false;
        forkLiftBodyDef.position.set(model.getSpawnPosition());
//        forkLiftBodyDef.position.set(1f, 2.5f); // Should be obtained from the map

        forklift = world.createBody(forkLiftBodyDef);

        PolygonShape shape = new PolygonShape(); //can be created N times to get the desired result
        shape.set(model.getCabin());

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.3f;
        // Collision works in the following way: you specify category of your fixture and then each category with which it collides
        fixDef.filter.categoryBits = GameConfig.BIT_FORKLIFT;
        fixDef.filter.maskBits = (GameConfig.BIT_MAP | GameConfig.BIT_OBSTACLE);
        forklift.createFixture(fixDef);
//        forklift.createFixture(shape, 0.3f);

        shape.set(model.getEngine());
        fixDef.shape = shape;
        fixDef.density = 1f;
        fixDef.filter.categoryBits = GameConfig.BIT_FORKLIFT;
        fixDef.filter.maskBits = (GameConfig.BIT_MAP | GameConfig.BIT_OBSTACLE);
        forklift.createFixture(fixDef);
//        forklift.createFixture(shape, 1.0f);

        // Creating forklift tubes
        forkliftTubes = new Body[model.getNumberOfTubes()];
        BodyDef tubeBodyDef = new BodyDef();
        tubeBodyDef.type = BodyDef.BodyType.DynamicBody;
        tubeBodyDef.fixedRotation = false;
//        tubeBodyDef.position.set(10f, 25f); // Should be obtained from the map
        float offset = 0;

        for (int i = 0; i < model.getNumberOfTubes(); i++ ){
            tubeBodyDef.position.set(model.getSpawnPosition().x + offset, model.getSpawnPosition().y);
//            tubeBodyDef.position.set(1.0f + offset, 2.5f);
            forkliftTubes[i] = world.createBody(tubeBodyDef);
            offset += model.getTubeSize()[0];
        }

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(model.getTubeSize()[0], model.getTubeSize()[1]);
//        forkliftTubes = new Body[model.getNumberOfTubes()];

        for (int i = 0; i < model.getNumberOfTubes(); i++ ){
            fixDef.shape = ps;
            fixDef.density = 0.25f;
            fixDef.filter.categoryBits = GameConfig.BIT_FORKLIFT;
            fixDef.filter.maskBits = (GameConfig.BIT_MAP | GameConfig.BIT_OBSTACLE);
            forkliftTubes[i].createFixture(fixDef);
//            forkliftTubes[i].createFixture(ps, 0.25f);
        }
        ps.dispose();

        // Creating Fork
        BodyDef forkDef = new BodyDef();
        forkDef.type = BodyDef.BodyType.DynamicBody;
        forkDef.fixedRotation = false;
//        forkDef.position.set(model.getSpawnPosition().x * 4, model.getSpawnPosition().y);
        forkDef.position.set(model.getSpawnPosition().x, model.getSpawnPosition().y);

//        forkDef.position.set(forkliftTubes[model.getNumberOfTubes()-1].getPosition().x * 5, // 5 is a random number
//                forkliftTubes[model.getNumberOfTubes()-1].getPosition().y); // Should be obtained from the map

        fork = world.createBody(forkDef);

        PolygonShape forkShape = new PolygonShape();
        forkShape.setAsBox(GameConfig.FORK_WIDTH, GameConfig.FORK_HEIGHT);

        fixDef.shape = forkShape;
        fixDef.density = 0.25f;
        fixDef.friction = 0.8f;
        fixDef.filter.categoryBits = GameConfig.BIT_FORKLIFT;
        fixDef.filter.maskBits = (GameConfig.BIT_MAP | GameConfig.BIT_OBSTACLE);
        fork.createFixture(fixDef).setUserData(this); // required for collision;
//        fork.createFixture(forkShape, 0.25f);

        forkShape.dispose();

        // Creating Prismatic Joints
//        Working
        prismaticJoints = new PrismaticJoint[model.getNumberOfTubes()];
        PrismaticJointDef pjd = new PrismaticJointDef();
        float offsetTwo = 0;
        for (int i = 0; i < model.getNumberOfTubes(); i++ ){
            pjd.enableMotor = true;
            pjd.maxMotorForce = 10f;
            // Need to assign the value because of the positioning bug (to be remade)
            pjd.motorSpeed = -1;
            pjd.enableLimit = true;
            pjd.upperTranslation = (i+1) * (1.6f * model.getTubeSize()[1]); // Limit of movement based on length of the tube

            pjd.bodyA = forklift;
            pjd.bodyB = forkliftTubes[i];
            pjd.collideConnected = false;
            // model.getFrontWheelRadius() * 0.8f is required to make the position of the tubes lower
//            pjd.localAnchorA.set(model.getLocationOfTubes() + offsetTwo, model.getTubeSize()[1] - model.getFrontWheelRadius() * 0.6f);
            pjd.localAnchorA.set(model.getLocationOfTubes() + offsetTwo, model.getTubeSize()[1] - model.getFrontWheelRadius() * 0.4f);
            pjd.localAxisA.set(0, 1.0f);
            prismaticJoints[i] = (PrismaticJoint) world.createJoint(pjd);
            offsetTwo += model.getTubeSize()[0] * 2;
        }

        // Revolute Joint Fork to Tube
        RevoluteJointDef rjd  = new RevoluteJointDef();

        rjd.enableMotor = true;
//        rjd.maxMotorTorque = 50f;
        rjd.maxMotorTorque = 3f;
        // Need to assign the value because of the positioning bug (to be remade)
        rjd.motorSpeed = -1;

        rjd.enableLimit = true;
        rjd.lowerAngle = 0;
        rjd.upperAngle = 0.5f;

        rjd.bodyA = forkliftTubes[model.getNumberOfTubes()-1];
        rjd.bodyB = fork;
        rjd.collideConnected = false;
        rjd.localAnchorA.set(0,  -model.getTubeSize()[1] + GameConfig.FORK_HEIGHT);
//        rjd.localAnchorA.set(GameConfig.FORK_WIDTH,  -model.getTubeSize()[1]); // Data should be taken from the Forklift class
        rjd.localAnchorB.set(-GameConfig.FORK_WIDTH, 0);
        revoluteJointFork = (RevoluteJoint) world.createJoint(rjd);

        // Creating wheels (should be declared as a new body)
        // RearWheel
        BodyDef wheelBodyDef = new BodyDef();
        wheelBodyDef.type = BodyDef.BodyType.DynamicBody;
        wheelBodyDef.fixedRotation = false;
        wheelBodyDef.position.set(model.getSpawnPosition());

        rearWheel = world.createBody(wheelBodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(model.getRearWheelRadius());
        FixtureDef fd = new FixtureDef();
        fd.density = 1.0f;
        fd.friction = 0.7f;
        fd.shape = circle;
        fd.filter.categoryBits = GameConfig.BIT_FORKLIFT;
        fd.filter.maskBits = (GameConfig.BIT_MAP | GameConfig.BIT_OBSTACLE);
        rearWheel.createFixture(fd);

        // FrontWheel
        frontWheel = world.createBody(wheelBodyDef);
        circle.setRadius(model.getFrontWheelRadius());
        fd.shape = circle;
        frontWheel.createFixture(fd);

        shape.dispose();
        circle.dispose();

        // WheelJoints
        WheelJointDef wjd = new WheelJointDef();
        wjd.enableMotor = true;
        wjd.maxMotorTorque = 10.0f;
        wjd.motorSpeed = 0;
//        wjd.dampingRatio = 0.8f;
////        wjd.frequencyHz = 7f;

        wjd.dampingRatio = 0.9f;
        wjd.frequencyHz = 10f;

        wjd.bodyA = forklift;
        wjd.bodyB = frontWheel;
        wjd.collideConnected = false;
        wjd.localAnchorA.set(model.getFrontWheelPosition());
        wjd.localAxisA.set(0, 1.0f);
        frontWheelJoint = (WheelJoint) world.createJoint(wjd);

        WheelJointDef rearWheelJointdef = new WheelJointDef();
        rearWheelJointdef.enableMotor = false;
        rearWheelJointdef.dampingRatio = 1f;
        rearWheelJointdef.frequencyHz = 7f;

        rearWheelJointdef.bodyA = forklift;
        rearWheelJointdef.bodyB = rearWheel;
        rearWheelJointdef.collideConnected = false;
        rearWheelJointdef.localAnchorA.set(model.getRearWheelPosition());
        rearWheelJointdef.localAxisA.set(0, 1.0f);
        world.createJoint(rearWheelJointdef);



    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(bodyTexture == null) {
            System.out.println("Region not set on Actor " + getClass().getName());
            return;
        }

//                batch.draw(bodyTexture, // Texture
//                        forklift.getPosition().x, forklift.getPosition().y, // Texture position
//                getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
//                1.5f, 1.2f, // Width and height of the texture
//                getScaleX(), getScaleY(), //scaling
//                        forklift.getAngle()*57.2957f); // Rotation (radiants to degrees)

        // drawing tubes texture (Text)
        for (Body forkliftTube : forkliftTubes){
            batch.draw(tubeTexture, // Texture
                    forkliftTube.getPosition().x - model.getTubeSize()[0], forkliftTube.getPosition().y - model.getTubeSize()[1], // Texture position
                    model.getTubeSize()[0], model.getTubeSize()[1], // Rotation point (width / 2, height /2 = center)
                    model.getTubeSize()[0] * 2, model.getTubeSize()[1] * 2, // Width and height of the texture
                    getScaleX(), getScaleY(), //scaling
                    forkliftTube.getAngle()*57.2957f);
        }

        batch.draw(forkTexture, // Texture
                fork.getPosition().x - GameConfig.FORK_WIDTH, fork.getPosition().y - GameConfig.FORK_HEIGHT, // Texture position
                GameConfig.FORK_WIDTH, GameConfig.FORK_HEIGHT, // Rotation point (width / 2, height /2 = center)
                GameConfig.FORK_WIDTH * 2, GameConfig.FORK_HEIGHT * 2, // Width and height of the texture
                getScaleX(), getScaleY(), //scaling
                fork.getAngle()*57.2957f);

        if (model.isThreeweeler()){
            batch.draw(wheelTexture, // Texture
                    rearWheel.getPosition().x-model.getRearWheelRadius(), rearWheel.getPosition().y -model.getRearWheelRadius(), // Texture position
                    model.getRearWheelRadius(), model.getRearWheelRadius(), // Rotation point (width / 2, height /2 = center)
                    model.getRearWheelRadius() * 2, model.getRearWheelRadius() * 2, // Width and height of the texture
                    getScaleX(), getScaleY(), //scaling
                    rearWheel.getAngle()*57.2957f);

            batch.draw(bodyTexture, // Texture
                    forklift.getPosition().x, forklift.getPosition().y, // Texture position
                    getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
                    model.getBodyWidth(), model.getBodyHeight(), // Width and height of the texture
                    getScaleX(), getScaleY(), //scaling
                    forklift.getAngle()*57.2957f); // Rotation (radiants to degrees)
        } else {
            batch.draw(bodyTexture, // Texture
                    forklift.getPosition().x, forklift.getPosition().y, // Texture position
                    getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
                    model.getBodyWidth(), model.getBodyHeight(), // Width and height of the texture
                    getScaleX(), getScaleY(), //scaling
                    forklift.getAngle() * 57.2957f); // Rotation (radiants to degrees)

            batch.draw(wheelTexture, // Texture
                    rearWheel.getPosition().x - model.getRearWheelRadius(), rearWheel.getPosition().y - model.getRearWheelRadius(), // Texture position
                    model.getRearWheelRadius(), model.getRearWheelRadius(), // Rotation point (width / 2, height /2 = center)
                    model.getRearWheelRadius() * 2, model.getRearWheelRadius() * 2, // Width and height of the texture
                    getScaleX(), getScaleY(), //scaling
                    rearWheel.getAngle() * 57.2957f);
        }

        batch.draw(wheelTexture, // Texture
                frontWheel.getPosition().x-model.getFrontWheelRadius(), frontWheel.getPosition().y -model.getFrontWheelRadius(), // Texture position
                model.getFrontWheelRadius(), model.getFrontWheelRadius(), // Rotation point (width / 2, height /2 = center)
                model.getFrontWheelRadius() * 2, model.getFrontWheelRadius() * 2, // Width and height of the texture
                getScaleX(), getScaleY(), //scaling
                frontWheel.getAngle()*57.2957f);


    }

    @Override
    public void act(float delta) {
        super.act(delta);

        //Fuel burning speed
        if (fuelTank > 0){
//            fuelTank -= 1f * delta;
            fuelTank -= 0f * delta;
        }

    }

    public boolean isFuelTankEmpty(){
        return (fuelTank <= 0);
    }

    public void moveForkliftRight(){
        System.out.println("Key D is pressed");
        //Movement logic with interpolation
//        frontWheelJoint.setMotorSpeed(-1.3f/model.getFrontWheelRadius());
        float motorSpeed = (-1.3f/model.getFrontWheelRadius());
        frontWheelJoint.setMotorSpeed(frontWheelJoint.getMotorSpeed() + ( motorSpeed - frontWheelJoint.getMotorSpeed()) * 0.025f);
    }

    public void moveForkliftLeft(){
        System.out.println("Key A is pressed");
        //Movement logic
        frontWheelJoint.setMotorSpeed(1.3f/model.getFrontWheelRadius());
    }

    public void moveTubeUp(){
        System.out.println("Key W is pressed");
        for (PrismaticJoint joint : prismaticJoints){
            joint.setMotorSpeed(0.5f);
        }
    }

    public void moveTubeDown(){
        System.out.println("Key S is pressed");
        for (PrismaticJoint joint : prismaticJoints){
            joint.setMotorSpeed(-0.5f);
        }
    }

    public void stopMoveForkliftRight(){
        System.out.println("Key D is pressed");
        //Movement logic
        frontWheelJoint.setMotorSpeed(0);
    }

    public void stopMoveForkliftLeft(){
        System.out.println("Key A is pressed");
        //Movement logic
        frontWheelJoint.setMotorSpeed(0);
    }

    public void stopMoveTubeUp(){
        System.out.println("Key W is pressed");
        for (PrismaticJoint joint : prismaticJoints){
            joint.setMotorSpeed(0);
        }
    }

    public void stopMoveTubeDown(){
        System.out.println("Key S is pressed");
        for (PrismaticJoint joint : prismaticJoints){
            joint.setMotorSpeed(0);
        }
    }

    public void rotateForkUp(){
        System.out.println("Key A is pressed");
        //Movement logic
        revoluteJointFork.setMotorSpeed(-0.5f);
    }

    public void rotateForkDown(){
        System.out.println("Key A is pressed");
        //Movement logic
        revoluteJointFork.setMotorSpeed(0.5f);
    }

    public void stopRotatingFork(){
        revoluteJointFork.setMotorSpeed(0);
    }

    public void setRegion() {
//        bodyTexture = bodyRegion;
//        tubeTexture = tubeRegion;
//        wheelTexture = wheelRegion;
//        forkTexture = forkRegion;
        bodyTexture = model.getForkliftRegion();
        tubeTexture = model.getTubeRegion();
        wheelTexture = model.getWheelRegion();
        forkTexture = model.getForkRegion();
        forkTexture = model.getForkRegion();
    }

    public Vector2 getFrokPosition(){
        return fork.getPosition();
    }

//    public Body getFork() {
//        return fork;
//    }

    public float getFuelTank() {
        return fuelTank;
    }

    public void setFuelTank(float fuelTank) {
        this.fuelTank = fuelTank;
    }

    public boolean isHasFuel() {
        return hasFuel;
    }

    public void setHasFuel(boolean hasFuel) {
        this.hasFuel = hasFuel;
    }

    public Body getForklift() {
        return forklift;
    }

    public void fillTank(){
        System.out.println("Forklift and fuel are colliding");
    }
}