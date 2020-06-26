package com.mygdx.forkliftaone.handlers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.forkliftaone.entity.BoxBase;
import com.mygdx.forkliftaone.entity.DoorSensor;
import com.mygdx.forkliftaone.entity.ForkliftActorBase;
import com.mygdx.forkliftaone.entity.FuelCan;
import com.mygdx.forkliftaone.entity.Sensor;
import com.mygdx.forkliftaone.maps.CustomTestMap;

public class SensorContactListener implements ContactListener {

    private int salary, donateSalary;

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null)
            return;
        if (fa.getUserData() == null || fb.getUserData() == null)
            return;

        if (isSensorContact(fa, fb)) {
            Sensor sensor;
            BoxBase rubbishBox;
            if (fa.getUserData() instanceof Sensor) {
                sensor = (Sensor) fa.getUserData();
                rubbishBox = (BoxBase) fb.getUserData();
            } else {
                sensor = (Sensor) fb.getUserData();
                rubbishBox = (BoxBase) fa.getUserData();
            }

            sensor.trigger(rubbishBox.getPrice(), rubbishBox.getDonatePrice());
            salary += rubbishBox.getPrice();
            donateSalary += rubbishBox.getDonatePrice();

        }

        if (isForkFuelCollide(fa, fb)) {
            ForkliftActorBase forklift;
            FuelCan fuel;
            if (fa.getUserData() instanceof ForkliftActorBase) {
                forklift = (ForkliftActorBase) fa.getUserData();
                fuel = (FuelCan) fb.getUserData();
            } else {
                forklift = (ForkliftActorBase) fb.getUserData();
                fuel = (FuelCan) fa.getUserData();
            }

            forklift.setHasFuel(true);
            fuel.setActive(true);

        }

        if (isDoorOpen(fa, fb)) {
            BoxBase boxBase;
            DoorSensor doorSensor;
            if (fa.getUserData() instanceof BoxBase) {
                boxBase = (BoxBase) fa.getUserData();
                doorSensor = (DoorSensor) fb.getUserData();
            } else {
                boxBase = (BoxBase) fb.getUserData();
                doorSensor = (DoorSensor) fa.getUserData();
            }

            doorSensor.getMap().openDoor();

        }

        if (isFallen(fa, fb)) {
            BoxBase boxBase;
            MapObject mapObject;
            if (fa.getUserData() instanceof BoxBase) {
                boxBase = (BoxBase) fa.getUserData();
                mapObject = (MapObject) fb.getUserData();
            } else {
                boxBase = (BoxBase) fb.getUserData();
                mapObject = (MapObject) fa.getUserData();
            }

            System.out.println("Map collides with box. Box Speed = " + boxBase.getBody().getLinearVelocity().y);
            // Destroying box if speed is more then 6f (Falling and breaking logic), should play a breaking sound
            // World cannot be changed from listener, so instead the "dead" status is set to true
            if (boxBase.getBody().getLinearVelocity().y <= -6f){
                    boxBase.setDead(true);
            }

        }


    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null)
            return;
        if (fa.getUserData() == null || fb.getUserData() == null)
            return;

        if (isSensorContact(fa, fb)) {
            Sensor sensor;
            BoxBase rubbishBox;
            if (fa.getUserData() instanceof Sensor) {
                sensor = (Sensor) fa.getUserData();
                rubbishBox = (BoxBase) fb.getUserData();
            } else {
                sensor = (Sensor) fb.getUserData();
                rubbishBox = (BoxBase) fa.getUserData();
            }

            sensor.untrigger(rubbishBox.getPrice(), rubbishBox.getDonatePrice());
            salary -= rubbishBox.getPrice();
            donateSalary -= rubbishBox.getDonatePrice();

        }

        if (isForkFuelCollide(fa, fb)) {
            ForkliftActorBase forklift;
            FuelCan fuel;
            if (fa.getUserData() instanceof ForkliftActorBase) {
                forklift = (ForkliftActorBase) fa.getUserData();
                fuel = (FuelCan) fb.getUserData();
            } else {
                forklift = (ForkliftActorBase) fb.getUserData();
                fuel = (FuelCan) fa.getUserData();
            }

            forklift.setHasFuel(false);
            fuel.setActive(false);

        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean isSensorContact(Fixture a, Fixture b) {
        if (a.getUserData() instanceof BoxBase || b.getUserData() instanceof BoxBase) {
            if (a.getUserData() instanceof Sensor || b.getUserData() instanceof Sensor) {
                return true;
            }
        }
        return false;
    }

    private boolean isForkFuelCollide(Fixture a, Fixture b) {
        if (a.getUserData() instanceof FuelCan || b.getUserData() instanceof FuelCan) {
            if (a.getUserData() instanceof ForkliftActorBase || b.getUserData() instanceof ForkliftActorBase) {
                return true;
            }
        }
        return false;
    }

    private boolean isDoorOpen(Fixture a, Fixture b) {
        if (a.getUserData() instanceof BoxBase || b.getUserData() instanceof BoxBase) {
            if (a.getUserData() instanceof DoorSensor || b.getUserData() instanceof DoorSensor) {
                return true;
            }
        }
        return false;
    }

    private boolean isFallen(Fixture a, Fixture b) {
        if (a.getUserData() instanceof BoxBase || b.getUserData() instanceof BoxBase) {
            if (a.getUserData() instanceof MapObject || b.getUserData() instanceof MapObject) {
                return true;
            }
        }
        return false;
    }

    public int getSalary() {
        return salary;
    }

    public int getDonateSalary() {
        return donateSalary;
    }
}
