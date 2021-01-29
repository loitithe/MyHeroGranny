package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyHeroGranny;
import com.mygdx.game.Screens.PlayScreen;

public class Granny extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD };

    public World world;
    public Body b2body;

    private PlayScreen screen;
    private TextureRegion grannyStand;


    public Granny( PlayScreen screen) {
        super(screen.getAtlas().findRegion("NES - Dragon Ball Z III Ressen Jinzou Ningen JPN - Overworld Characters"));
        this.screen = screen;
        this.world = screen.getWorld();
        defineGranny();
        grannyStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16 / MyHeroGranny.PPM, 16 / MyHeroGranny.PPM);
        setRegion(grannyStand);
    }

    public void defineGranny() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MyHeroGranny.PPM, 32 / MyHeroGranny.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MyHeroGranny.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);


    }

    public void update(float dt) {

    }
}
