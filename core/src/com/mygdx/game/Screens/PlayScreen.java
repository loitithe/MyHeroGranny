package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyHeroGranny;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Granny;
import com.mygdx.game.Sprites.Tools.B2WorldCreator;

public class PlayScreen implements Screen {
    private MyHeroGranny game;
    private TextureAtlas atlas;
    private Hud hud;

    private OrthographicCamera gamecam;
    private Viewport gameport;
    /*Tiled map variables*/
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Granny player;

    public PlayScreen(MyHeroGranny game) {
        atlas = new TextureAtlas("dragonball.pack");
        this.game = game;
        gamecam = new OrthographicCamera();

        gameport = new FitViewport(MyHeroGranny.V_WIDTH / MyHeroGranny.PPM, MyHeroGranny.V_HEIGHT / MyHeroGranny.PPM, gamecam);
        //creates game HUD for scores/timers/level info
        hud = new Hud(game.batch);
        // Load map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("lvl1map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyHeroGranny.PPM);
        // initially gamecam to be centered correctly at the start
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);
        //
        player = new Granny(world, this);
        //   world.setContactListener(new WorldContactListener());

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void update(float dt) {
        handleInput(dt);

        // ??
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        gamecam.position.x = player.b2body.getPosition().x;

        hud.update(dt);

        gamecam.update();
        renderer.setView(gamecam);
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
        // set batch to draw what the Hud camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
