package com.mygdx.forkliftaone.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.forkliftaone.config.GameConfig;

public class TiledObjectUtil {

public static void parseTiledTiledObjectLayer(World world, MapObjects mapObjects){

    for (MapObject object : mapObjects){
        Shape shape;
        if (object instanceof PolylineMapObject){
            shape = createPolyline((PolylineMapObject) object);
        } else continue;

        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 1f;
        fixDef.filter.categoryBits = GameConfig.BIT_MAP;
        body.createFixture(fixDef).setUserData(object); // required for collision
        shape.dispose();
    }

}

private static ChainShape createPolyline(PolylineMapObject polyline){
    float[] vertices = polyline.getPolyline().getTransformedVertices();
    Vector2[] worldVertices = new Vector2[vertices.length / 2];

    for (int i = 0; i < worldVertices.length; i ++ ){
        worldVertices[i] = new Vector2(vertices[i * 2] / GameConfig.SCALE, vertices[i * 2 + 1] / GameConfig.SCALE);
    }

    ChainShape cs = new ChainShape();
    cs.createChain(worldVertices);
    return cs;
}

}
