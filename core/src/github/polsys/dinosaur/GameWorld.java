package github.polsys.dinosaur;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

    private AssetManager assetManager;
    private SpriteBatch batch;
    private Camera camera;
    private Matrix4 defaultProjectionMatrix;
    private Box2DDebugRenderer debugRenderer;

    private Texture background;
    private Dinosaur player;
    private List<GameObject> objects;
    private World world;

    private Body worldBottom;
    private Body worldTop;
    private Body worldRight;

    public void create(AssetManager assetManager) {
        loadAssets(assetManager);

        batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        defaultProjectionMatrix = batch.getProjectionMatrix().cpy();

        background = assetManager.get("Background.png");

        Box2D.init();
        world = new World(new Vector2(0, -9.8f), true);
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
        background.dispose();
    }

    public void update() {
        world.step(1f / 60, 6, 2);

        player.update();
        for (GameObject object : objects) {
            object.update();
        }

        camera.position.set(player.position.x + 8, VIEWPORT_HEIGHT / 2, 0);
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
        player.draw(batch);
        for (GameObject object : objects) {
            object.draw(batch);
        }

        batch.end();

        debugRenderer.render(world, camera.combined);
    }

    private void loadAssets(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.load("Anvils.png", Texture.class);
        assetManager.load("Background.png", Texture.class);
        assetManager.load("Dinosaur.png", Texture.class);
        assetManager.load("Dinosaur_Jet.png", Texture.class);
        assetManager.load("Launchpad.png", Texture.class);
        assetManager.load("Palm.png", Texture.class);
        assetManager.finishLoading();
    }

    private void generateWorldBorder() {
        worldBottom = createStaticBox(0, 530, -1, -2);
        worldTop = createStaticBox(0, 530, VIEWPORT_HEIGHT, VIEWPORT_HEIGHT + 1);
        worldRight = createStaticBox(530, 531, 30, -1);
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
        launchpad.position = new Vector2(4, 4);
        launchpad.load(assetManager);
        launchpad.addToWorld(world);
        objects.add(launchpad);

        // Random obstacles
        for (int i = 0; i < 25; i++) {
            float x = 20 + i * 20 + (rand.nextFloat() * 4);
            Obstacle obstacle = null;

            switch (rand.nextInt(3)) {
                case 0:
                    obstacle = new Launchpad();
                    obstacle.position = new Vector2(x, 4);
                    break;
                case 1:
                    obstacle = new AnvilStack();
                    obstacle.position = new Vector2(x, 3.5f);
                    break;
                case 2:
                    obstacle = new PalmTree();
                    obstacle.position = new Vector2(x, 5f);
                    break;
            }


            obstacle.load(assetManager);
            obstacle.addToWorld(world);
            objects.add(obstacle);
        }
    }

}
