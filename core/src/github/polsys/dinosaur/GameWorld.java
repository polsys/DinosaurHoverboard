package github.polsys.dinosaur;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;

/**
 * The world as it is known. That is, all the game objects and such.
 */
public class GameWorld {

    private static final float VIEWPORT_WIDTH = 30;
    private static final float VIEWPORT_HEIGHT = 30 * (9f / 16);
    private static final int WORLD_WIDTH = 560;

    private AssetManager assetManager;
    private SpriteBatch batch;
    private Camera camera;
    private Matrix4 defaultProjectionMatrix;
    private Box2DDebugRenderer debugRenderer;

    private Texture background;
    private Sprite wormhole;
    private Dinosaur player;
    private List<GameObject> objects;
    private World world;

    private int deathTicks;
    private static final int DEATH_TICKS_UNTIL_RESTART = 300;

    private Body worldBottom;
    private Body worldTop;
    private Body worldRight;

    public void create(AssetManager assetManager) {
        loadAssets(assetManager);

        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        defaultProjectionMatrix = batch.getProjectionMatrix().cpy();

        background = assetManager.get("Background.png");
        wormhole = new Sprite((Texture)assetManager.get("Wormhole.png"));
        wormhole.setPosition(WORLD_WIDTH - 25, 0);
        wormhole.setSize(30, VIEWPORT_HEIGHT);

        Box2D.init();
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();

                if (a == player) {
                    if ((b != null) && ((Obstacle)b).isLethal())
                        player.die();
                }
                else if (b == player) {
                    if ((a != null) && ((Obstacle)a).isLethal())
                        player.die();
                }
            }

            @Override
            public void endContact(Contact contact) { }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) { }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) { }
        });
        debugRenderer = new Box2DDebugRenderer();

        player = new Dinosaur();
        player.position = new Vector2(4, 10.8f);
        player.load(assetManager);
        player.addToWorld(world);

        generateWorldBorder();
        generateLevel();
    }

    public void dispose() {
        batch.dispose();
        world.dispose();
    }

    public void update() {
        // Physics
        world.step(1f / 60, 6, 2);

        // Player and objects
        player.update();
        if (player.dead)
            deathTicks++;
        for (GameObject object : objects) {
            object.update();
        }

        // Camera
        float cameraX = player.position.x + 8;
        if (cameraX > (WORLD_WIDTH - VIEWPORT_WIDTH / 2))
            cameraX = WORLD_WIDTH - VIEWPORT_WIDTH / 2;
        camera.position.set(cameraX, VIEWPORT_HEIGHT / 2, 0);
        camera.update();
    }

    public void render() {
        Gdx.gl.glClearColor(0.45f, 0.75f, 0.81f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Background - manually controlled translation
        batch.setProjectionMatrix(defaultProjectionMatrix);
        batch.draw(background, 0, 0);

        // Objects - automatic translation
        batch.setProjectionMatrix(camera.combined);
        wormhole.draw(batch);
        player.draw(batch);
        for (GameObject object : objects) {
            object.draw(batch);
        }

        batch.end();

        //debugRenderer.render(world, camera.combined);
    }

    public boolean shouldRestart() {
        if (deathTicks > DEATH_TICKS_UNTIL_RESTART) {
            return true;
        }

        return false;
    }

    public boolean isGameOver() {
        return (!player.dead) && (player.position.x > WORLD_WIDTH - 5);
    }

    private void loadAssets(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.load("Anvils.png", Texture.class);
        assetManager.load("Background.png", Texture.class);
        assetManager.load("Box.png", Texture.class);
        assetManager.load("Container.png", Texture.class);
        assetManager.load("Container_Hanging.png", Texture.class);
        assetManager.load("Dinosaur.png", Texture.class);
        assetManager.load("Dinosaur_Jet.png", Texture.class);
        assetManager.load("Launchpad.png", Texture.class);
        assetManager.load("Palm.png", Texture.class);
        assetManager.load("Wormhole.png", Texture.class);
        assetManager.finishLoading();
    }

    private void generateWorldBorder() {
        worldBottom = createStaticBox(0, WORLD_WIDTH, -1, -2);
        worldTop = createStaticBox(0, WORLD_WIDTH, VIEWPORT_HEIGHT, VIEWPORT_HEIGHT + 1);
        worldRight = createStaticBox(WORLD_WIDTH, WORLD_WIDTH + 1, 30, -1);
    }

    private Body createStaticBox(float left, float right, float top, float bottom) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2((left + right) / 2, (top + bottom) / 2));

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        Vector2 size = new Vector2(right - left, top - bottom);
        shape.setAsBox(size.x / 2, size.y / 2);

        body.createFixture(shape, 0);
        shape.dispose();

        return body;
    }

    private void generateLevel() {
        objects = new LinkedList<GameObject>();
        Random rand = new Random();

        // Starting position
        Launchpad launchpad = new Launchpad();
        launchpad.position = new Vector2(4, 3.5f);
        launchpad.load(assetManager);
        launchpad.addToWorld(world);
        objects.add(launchpad);

        // Random obstacles
        for (int i = 0; i < 25; i++) {
            float x = 30 + i * 20 + (rand.nextFloat() * 4);
            Obstacle obstacle = null;

            switch (rand.nextInt(7)) {
                case 0:
                    obstacle = new Container();
                    obstacle.position = new Vector2(x, 2.4f);
                    break;
                case 1:
                case 2:
                case 3:
                    obstacle = new HangingContainer();
                    obstacle.position = new Vector2(x, VIEWPORT_HEIGHT - 2.5f);
                    break;
                case 4:
                    obstacle = new AnvilStack();
                    obstacle.position = new Vector2(x, 3.5f);
                    break;
                case 5:
                    obstacle = new PalmTree();
                    obstacle.position = new Vector2(x, 5f);
                    break;
                case 6:
                    createBoxStack(x, rand);
                    continue;
            }


            obstacle.load(assetManager);
            obstacle.addToWorld(world);
            objects.add(obstacle);
        }
    }

    private void createBoxStack(float x, Random rand) {
        // 1 to 3 columns
        for (int xIndex = 0; xIndex < (1 + rand.nextInt(3)); xIndex++) {
            // 5 to 10 boxes
            for (int yIndex = 0; yIndex < (5 + rand.nextInt(6)); yIndex++) {
                CardboardBox box = new CardboardBox();
                box.position = new Vector2(x + (xIndex * 2f), yIndex * 2f);

                box.load(assetManager);
                box.addToWorld(world);
                objects.add(box);
            }
        }
    }

}
