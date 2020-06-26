package com.mygdx.forkliftaone.maps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.forkliftaone.config.GameConfig;
import com.mygdx.forkliftaone.entity.DoorSensor;
import com.mygdx.forkliftaone.entity.SpecialBox;
import com.mygdx.forkliftaone.utils.AssetPaths;
import com.mygdx.forkliftaone.utils.BoxFactory;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;
import com.mygdx.forkliftaone.utils.RegionNames;

import java.util.Random;

public class CustomTestMap extends MapBase {
    private Vector2[][] boxCoords;
    private BoxFactory factory;
    private World world;
    private TextureAtlas atlas;
    private Camera camera;
    private Stage stage;
    private DoorSensor doorSensor;

    private PrismaticJoint prismaticJoint, elevatorJoint;
    private float elevatorTimer, blinkingTimer;
    private boolean drawing;

    private Inventory inv;

    private AssetManager assetManager;

//    ProcessInventory pi = new ProcessInventory();
ProcessInventoryImproved pi = new ProcessInventoryImproved();

    public CustomTestMap(World world, AssetManager assetManager, TextureRegion backTexture, TextureRegion middleTexture, Camera camera, Stage stage, TextureAtlas atlas) {
        super(world, AssetPaths.CUSTOM_TILED_MAP, new Vector2(1.5f, 1.5f),
                10f, 1f, 1f, 0.5f, ((TextureRegion)atlas.findRegion(RegionNames.TRUCK_ONE)),
                1f ,1f, 1f, 1f);

        this.world = world;
        this.atlas = atlas;
        this.camera = camera;
        this.stage = stage;
        inv = pi.read();
        factory = new BoxFactory();
        this.assetManager = assetManager;

//        this.backTexture = backTexture;
//        this.middleTexture = middleTexture;
        this.backTexture = atlas.findRegion(RegionNames.BOX_TEXTURE);
        this.middleTexture = atlas.findRegion(RegionNames.FORKLIFT_WHEEL);
        this.frontTexture = atlas.findRegion(RegionNames.BOX_TEXTURE);

        // Logic for spawning different number of different goods
        // Total number of good-types (4 = fuel, cheap, middle, expensive)
        boxCoords = new Vector2[4][];
        // Only one fuel will be spawned
        boxCoords[0] = new Vector2[1];
        // Two cheap will be spawned
        boxCoords[1] = new Vector2[2];
        // Only one middle will be spawned
        boxCoords[2] = new Vector2[1];
        // Only one expensive will be spawned
        boxCoords[3] = new Vector2[5];


        // Coordinates of each box
        boxCoords[0][0] = new Vector2(4.5f, 5f);
//        boxCoords[0][1] = new Vector2(5.5f, 5f);

        boxCoords[1][0] = new Vector2(3f, 5f);
        boxCoords[1][1] = new Vector2(5.5f, 5f);

        boxCoords[2][0] = new Vector2(6f, 5f);
//        boxCoords[2][1] = new Vector2(5.5f, 5f);
//
        boxCoords[3][0] = new Vector2(6.5f, 5f);
        boxCoords[3][1] = new Vector2(5.5f, 5f);
        boxCoords[3][2] = new Vector2(5.5f, 5f);
        boxCoords[3][3] = new Vector2(5.5f, 5f);
        boxCoords[3][4] = new Vector2(5.5f, 5f);

        createObstacles(8f, 4f, 0.1f, 1f,
                8f, 4f, 0.1f, 1f,
                6f, 6f, 1f, 0.05f);
        doorSensor = new DoorSensor(world, this, 1.5f, 1f, 1f, 1f);
    }

    public void spawnBoxes() {
        // Spawn fuel
        for (Vector2 coord : boxCoords[0]) {
                stage.addActor(factory.getFuelCan(world, assetManager, camera, atlas, coord));
//            stage.addActor(factory.getBox(world, camera, atlas, coord));
        }

        // Spawn cheap goods
        for (Vector2 coord : boxCoords[1]) {
            stage.addActor(factory.getBox(world, assetManager, camera, atlas, coord));
        }

        // Spawn middle goods
        for (Vector2 coord : boxCoords[2]) {
//            stage.addActor(factory.getBox(world, assetManager, camera, atlas, coord));
            stage.addActor(factory.getSpecialBox(world, assetManager, camera, atlas, coord));
        }

        // Spawn expensive goods
        // (Logic works OK)
        if (inv.isDonateBoxesPurchased()){
            Vector2 coord;
            int rand = new Random().nextInt(boxCoords[3].length) + 1;
            for (int i = 0; i < rand; i++){
                coord = boxCoords[3][i];
                // Spawn donate boxes here
                stage.addActor(factory.getBox(world, assetManager, camera, atlas, coord));
                System.out.println("Donate box spawned");
            }

            if (boxCoords[3].length > rand){
                for (int i = rand; i < boxCoords[3].length; i++){
                    coord = boxCoords[3][i];
                    // Spawn expensive, but not donate boxes here
                    stage.addActor(factory.getBox(world, assetManager, camera, atlas, coord));
                    System.out.println("Not donate box spawned");
                }
            }

        } else {
            for (Vector2 coord : boxCoords[3]) {
                stage.addActor(factory.getBox(world, assetManager, camera, atlas, coord));
            }
        }

    }

    private void createObstacles(float wallX, float wallY, float wallWidth, float wallHeight,
                                 float doorX, float doorY, float doorWidth, float doorHeight,
                                 float elevatorX, float elevatorY, float elevatorWidth, float elevatorHeight){
        // Creating wall
        BodyDef wallsDef = new BodyDef();
        wallsDef.type = BodyDef.BodyType.StaticBody;
        wallsDef.fixedRotation = true;
        wallsDef.position.set(wallX, wallY);

        Body wall = world.createBody(wallsDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(wallWidth, wallHeight);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.3f;
        fixDef.filter.categoryBits = GameConfig.BIT_WALLS;
        fixDef.filter.maskBits = (GameConfig.BIT_FORKLIFT | GameConfig.BIT_OBSTACLE);
        wall.createFixture(fixDef);

        // Creating sliding door
        BodyDef doorDef = new BodyDef();
        doorDef.type = BodyDef.BodyType.DynamicBody;
        doorDef.fixedRotation = true;
        doorDef.position.set(doorX, doorY);

        Body door = world.createBody(doorDef);

        shape = new PolygonShape();
        shape.setAsBox(doorWidth, doorHeight);

        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.3f;
        fixDef.filter.categoryBits = GameConfig.BIT_WALLS;
        fixDef.filter.maskBits = (GameConfig.BIT_FORKLIFT | GameConfig.BIT_OBSTACLE | GameConfig.BIT_MAP);
        door.createFixture(fixDef);

        // Creating prismatic joint
        PrismaticJointDef pjd = new PrismaticJointDef();
        pjd.enableMotor = true;
        pjd.maxMotorForce = 3f;
        // Need to assign the value because of the positioning bug (to be remade)
        pjd.motorSpeed = -1;
        pjd.enableLimit = true;
        pjd.upperTranslation = 0f;
        pjd.lowerTranslation = -2f;

        pjd.bodyA = wall;
        pjd.bodyB = door;
        pjd.collideConnected = false;
        // model.getFrontWheelRadius() * 0.8f is required to make the position of the tubes lower
        pjd.localAnchorA.set(0f, 0);
        pjd.localAxisA.set(0, 1.0f);
        prismaticJoint = (PrismaticJoint) world.createJoint(pjd);

        // Creating elevator
        // Elevator platform
        BodyDef elevatorDef = new BodyDef();
        elevatorDef.type = BodyDef.BodyType.StaticBody;
        elevatorDef.fixedRotation = true;
        elevatorDef.position.set(elevatorX, elevatorY);

        Body elevator = world.createBody(elevatorDef);

        shape = new PolygonShape();
        shape.setAsBox(elevatorWidth, elevatorHeight);

        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.3f;
//        fixDef.filter.categoryBits = GameConfig.BIT_WALLS;
//        fixDef.filter.maskBits = (GameConfig.BIT_FORKLIFT | GameConfig.BIT_OBSTACLE | GameConfig.BIT_MAP);
        elevator.createFixture(fixDef);

        // Elevator moving part
        BodyDef elevatorMainDef = new BodyDef();
        elevatorMainDef.type = BodyDef.BodyType.DynamicBody;
        elevatorMainDef.fixedRotation = true;
        elevatorMainDef.position.set(elevatorX, elevatorY);

        Body elevatorMain = world.createBody(elevatorMainDef);

        shape = new PolygonShape();
        shape.setAsBox(elevatorWidth, elevatorHeight);

        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.3f;
        fixDef.filter.categoryBits = GameConfig.BIT_WALLS;
        fixDef.filter.maskBits = (GameConfig.BIT_FORKLIFT | GameConfig.BIT_OBSTACLE | GameConfig.BIT_MAP);
        elevatorMain.createFixture(fixDef);

        // Elevator joint (motor)
        PrismaticJointDef pjdElevator = new PrismaticJointDef();
        pjdElevator.enableMotor = true;
        pjdElevator.maxMotorForce = 3f;
        // Need to assign the value because of the positioning bug (to be remade)
        pjdElevator.motorSpeed = -1;
        pjdElevator.enableLimit = true;
        pjdElevator.upperTranslation = 0f;
        pjdElevator.lowerTranslation = -1f;

        pjdElevator.bodyA = elevator;
        pjdElevator.bodyB = elevatorMain;
        pjdElevator.collideConnected = false;
        // model.getFrontWheelRadius() * 0.8f is required to make the position of the tubes lower
        pjdElevator.localAnchorA.set(0f, 0f);
        pjdElevator.localAxisA.set(0, 1f);
        elevatorJoint = (PrismaticJoint) world.createJoint(pjdElevator);

        shape.dispose();
    }

    public void openDoor(){
        prismaticJoint.setMotorSpeed(1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Drawing wall n` doors here
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
        super.draw(batch, parentAlpha);

        if (drawing) {
            batch.draw(atlas.findRegion(RegionNames.BOX_TEXTURE), // Texture
                    doorSensor.getX() - 0.5f, doorSensor.getY() - 0.5f, // Texture position
                    getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
                    1f, 1f, // Width and height of the texture
                    1f, 1f, //scaling
                    0); // Rotation (radiants to degrees)
        }



//        batch.end();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Check for the lower and upper limits
        // elevatorTimer (here it is 3) marks the pause which the elevator makes (how long it waits
        if (elevatorJoint.getJointTranslation() <= -1){
            elevatorTimer += 1 * delta;
            if (elevatorTimer >= 3f){
                elevatorJoint.setMotorSpeed(1);
                elevatorTimer = 0;
            }

        } else if (elevatorJoint.getJointTranslation() >= 0){
            elevatorTimer += 1 * delta;
            if (elevatorTimer >= 3f){
                elevatorJoint.setMotorSpeed(-1);
                elevatorTimer = 0;
            }
        }


            blinkingTimer += 1f * delta;
            if (blinkingTimer <= 0.75f){
                drawing = false;
            } else if (blinkingTimer >= 0.75f && blinkingTimer <= 1.5f){
                drawing = true;
            } else if (blinkingTimer >= 1.5f){
                blinkingTimer = 0;
            }

    }
}
