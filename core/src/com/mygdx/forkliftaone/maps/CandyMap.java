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
import com.mygdx.forkliftaone.entity.SpecialBox;
import com.mygdx.forkliftaone.utils.AssetPaths;
import com.mygdx.forkliftaone.utils.BoxFactory;
import com.mygdx.forkliftaone.utils.Inventory;
import com.mygdx.forkliftaone.utils.ProcessInventoryImproved;
import com.mygdx.forkliftaone.utils.RegionNames;

import java.util.Random;

public class CandyMap extends MapBase {
    private Vector2[][] boxCoords;
    private BoxFactory factory;
    private World world;
    private TextureAtlas atlas;
    private Camera camera;
    private Stage stage;

    private Inventory inv;
    ProcessInventoryImproved pi = new ProcessInventoryImproved();

    private PrismaticJoint prismaticJoint, elevatorJoint;
    private float elevatorTimer, blinkingTimer, elevatorWidth = 3.2f, elevatorHeight = 0.16f, doorWidth = 0.1f, doorHeight = 1f;

    private Body elevatorMain, door;

    private AssetManager assetManager;

    // Изменить AssetPaths.TEST_TILED_MAP для загрузки другой карты
    public CandyMap(World world, AssetManager assetManager, Camera camera, Stage stage, TextureAtlas atlas) {
        super(world, AssetPaths.TEST_TILED_MAP, new Vector2(10.5f, 24.5f),
                52.8f, 11.2f, 4.2f, 1.52f, ((TextureRegion)atlas.findRegion(RegionNames.TRUCK_ONE)),
                52.8f + 1.28f ,11.84f + 1.0f, 1.28f, 1.0f);

        this.world = world;
        this.atlas = atlas;
        this.camera = camera;
        this.stage = stage;
        inv = pi.read();
        factory = new BoxFactory();
        this.assetManager = assetManager;

//        this.backTexture = atlas.findRegion(RegionNames.LAYER_ONE);
//        this.middleTexture = atlas.findRegion(RegionNames.LAYER_TWO);
        this.backTexture = atlas.findRegion(RegionNames.MAP_CANDY_BACK);
        this.middleTexture = atlas.findRegion(RegionNames.MAP_CANDY_FRONT);
        this.frontTexture = atlas.findRegion(RegionNames.MAP_CANDY_MIDDLE);

        boxCoords = new Vector2[4][];
        // Only one fuel will be spawned
        boxCoords[0] = new Vector2[3];
        // Two cheap will be spawned
        boxCoords[1] = new Vector2[2];
        // Only one middle will be spawned
        boxCoords[2] = new Vector2[1];
        // Only one expensive will be spawned
        boxCoords[3] = new Vector2[5];

        boxCoords[0][0] = new Vector2(15.04f, 15.20f); // Go up = y + 1.28 Go right = x + 4.80
        boxCoords[0][1] = new Vector2(19.84f, 16.48f);
        boxCoords[0][2] = new Vector2(8.96f, 3.52f);

        boxCoords[1][0] = new Vector2(15.04f, 16.48f);
        boxCoords[1][1] = new Vector2(15.04f, 17.76f);

        boxCoords[2][0] = new Vector2(19.84f, 15.20f);
//        boxCoords[2][1] = new Vector2(5.5f, 5f);
//
        boxCoords[3][0] = new Vector2(19.84f, 17.76f);
        boxCoords[3][1] = new Vector2(24.64f, 15.20f);
        boxCoords[3][2] = new Vector2(24.64f, 16.48f);
        boxCoords[3][3] = new Vector2(24.64f, 17.76f);
        boxCoords[3][4] = new Vector2(29.44f, 15.20f);

        createObstacles(8f, 4f, 0.1f, 1f,
                8f, 4f, doorWidth, doorHeight,
                4.16f, 11.68f, elevatorWidth, elevatorHeight);

    }

    public void spawnBoxes() {
//        for (Vector2 coord : boxCoords) {
//            stage.addActor(factory.getFuelCan(world, assetManager, camera, atlas, coord));
////            stage.addActor(factory.getBox(world, camera, atlas, coord));
//
//        }

        // Spawn fuel
        for (Vector2 coord : boxCoords[0]) {
            stage.addActor(factory.getFuelCan(world, assetManager, camera, atlas, coord));
//            stage.addActor(factory.getBox(world, camera, atlas, coord));
        }

        // Spawn cheap goods
//        for (Vector2 coord : boxCoords[1]) {
//            stage.addActor(factory.getBox(world, assetManager, camera, atlas, coord));
//        }
        for (Vector2 coord : boxCoords[1]) {
            stage.addActor(factory.getRandomCheapBox(world, assetManager, camera, atlas, coord));
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


//        stage.addActor(factory.getSpecialBox(world, assetManager, camera, atlas, coord));
//        SpecialBox specialBox = new SpecialBox(world, assetManager, camera, atlas,
//                0.1f, 0.3f, 0.3f, RegionNames.BOX_TEXTURE, new Vector2(15.04f, 15.20f));
//        stage.addActor(specialBox);

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

        door = world.createBody(doorDef);

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
        fixDef.filter.maskBits = (GameConfig.BIT_MAP);
        elevator.createFixture(fixDef);

        // Elevator moving part
        BodyDef elevatorMainDef = new BodyDef();
        elevatorMainDef.type = BodyDef.BodyType.DynamicBody;
        elevatorMainDef.fixedRotation = true;
        elevatorMainDef.position.set(elevatorX, elevatorY);

        elevatorMain = world.createBody(elevatorMainDef);

        shape = new PolygonShape();
        shape.setAsBox(elevatorWidth, elevatorHeight);

        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 0.1f;
        fixDef.filter.categoryBits = GameConfig.BIT_WALLS;
        fixDef.filter.maskBits = (GameConfig.BIT_FORKLIFT | GameConfig.BIT_OBSTACLE);
        elevatorMain.createFixture(fixDef);

        // Elevator joint (motor)
        PrismaticJointDef pjdElevator = new PrismaticJointDef();
        pjdElevator.enableMotor = true;
        pjdElevator.maxMotorForce = 300f;
        // Need to assign the value because of the positioning bug (to be remade)
        pjdElevator.motorSpeed = -1;
        pjdElevator.enableLimit = true;
        pjdElevator.upperTranslation = 0f;
        pjdElevator.lowerTranslation = -11.52f;

        pjdElevator.bodyA = elevator;
        pjdElevator.bodyB = elevatorMain;
        pjdElevator.collideConnected = false;
        // model.getFrontWheelRadius() * 0.8f is required to make the position of the tubes lower
        pjdElevator.localAnchorA.set(0f, 0f);
        pjdElevator.localAxisA.set(0, 1f);
        elevatorJoint = (PrismaticJoint) world.createJoint(pjdElevator);

        shape.dispose();
    }


    @Override
    public void openDoor() {
        // Some code here
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Check for the lower and upper limits
        // elevatorTimer (here it is 3) marks the pause which the elevator makes (how long it waits
        if (elevatorJoint.getJointTranslation() <= -11.52){
            elevatorTimer += 1 * delta;
            if (elevatorTimer >= 5f){
                elevatorJoint.setMotorSpeed(1);
                elevatorTimer = 0;
            }

        } else if (elevatorJoint.getJointTranslation() >= 0){
            elevatorTimer += 1 * delta;
            if (elevatorTimer >= 5f){
                elevatorJoint.setMotorSpeed(-1);
                elevatorTimer = 0;
            }
        }


//        blinkingTimer += 1f * delta;
//        if (blinkingTimer <= 0.75f){
//            drawing = false;
//        } else if (blinkingTimer >= 0.75f && blinkingTimer <= 1.5f){
//            drawing = true;
//        } else if (blinkingTimer >= 1.5f){
//            blinkingTimer = 0;
//        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Drawing wall n` doors here
//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
        super.draw(batch, parentAlpha);

        batch.draw(atlas.findRegion(RegionNames.ELEVATOR), // Texture
                elevatorMain.getPosition().x - elevatorWidth , elevatorMain.getPosition().y - elevatorHeight, // Texture position
                getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
                elevatorWidth * 2f, elevatorHeight * 2f, // Width and height of the texture
                1f, 1f, //scaling
                0); // Rotation (radiants to degrees)

        batch.draw(atlas.findRegion(RegionNames.DOOR), // Texture
                door.getPosition().x - doorWidth , door.getPosition().y - doorHeight, // Texture position
                getOriginX(), getOriginY(), // Rotation point (width / 2, height /2 = center)
                doorWidth * 2f, doorHeight * 2f, // Width and height of the texture
                1f, 1f, //scaling
                0); // Rotation (radiants to degrees)


//        batch.end();
    }



}
