package github.polsys.dinosaur;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

/**
 * THE DINOSAUR!!!
 */
class Dinosaur extends GameObject {

    private static final float DINOSAUR_SIZE = 5f;
    public static final float DINOSAUR_BOUNDS_HALF_HEIGHT = DINOSAUR_SIZE / 2.4f;
    public static final float DINOSAUR_BOUNDS_HALF_WIDTH = DINOSAUR_SIZE / 2.3f;
    public static final int DINOSAUR_MASS = 2000;
    public static final float DINOSAUR_DENSITY = DINOSAUR_MASS / (DINOSAUR_SIZE * DINOSAUR_SIZE);
    public static final float MAX_VELOCITY_HORIZONTAL = 10f;
    public static final float MAX_VELOCITY_VERTICAL = 5f;

    private Sprite idleSprite;
    private Sprite jetSprite;
    private boolean engineOn;
    private boolean shouldDieNextUpdate;
    private boolean dead;

    private Body body;

    public void die() {
        shouldDieNextUpdate = true;
    }

    @Override
    public Sprite getSprite() {
        return engineOn ? jetSprite : idleSprite;
    }

    @Override
    public void addToWorld(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(DINOSAUR_BOUNDS_HALF_WIDTH, DINOSAUR_BOUNDS_HALF_HEIGHT);

        body.createFixture(shape, DINOSAUR_DENSITY);
        shape.dispose();
    }

    @Override
    public void load(AssetManager assetManager) {
        idleSprite = setupSprite(assetManager, "Dinosaur.png");
        jetSprite = setupSprite(assetManager,  "Dinosaur_Jet.png");
    }

    private Sprite setupSprite(AssetManager assetManager, String filename) {
        Texture texture = assetManager.get(filename);
        Sprite sprite = new Sprite(texture);
        sprite.setSize(DINOSAUR_SIZE, DINOSAUR_SIZE);
        sprite.setOriginCenter();

        return sprite;
    }

    @Override
    public void update() {
        position = body.getPosition();
        getSprite().setRotation(body.getAngle() * MathUtils.radiansToDegrees);

        if (shouldDieNextUpdate && !dead) {
            dead = true;
            body.setType(BodyDef.BodyType.DynamicBody);
            body.setFixedRotation(false);

            // Let's spin a bit :D
            Random random = new Random();
            body.applyAngularImpulse(40000 + (random.nextFloat() * 25000), true);
        }

        if (!dead) {
            // Input
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                engineOn = true;

                // Keep the dino at (near-)constant altitude
                body.applyForceToCenter(0, DINOSAUR_MASS * 7.5f, true);

                // Horizontal acceleration
                if (body.getLinearVelocity().x < MAX_VELOCITY_HORIZONTAL) {
                    body.applyForceToCenter(50000, 0, true);
                }

                // Upwards thruster
                if (Gdx.input.isKeyPressed(Input.Keys.W) && (body.getLinearVelocity().y < MAX_VELOCITY_VERTICAL)) {
                    body.applyForceToCenter(0, 20000, true);
                }

                // Downwards thruster
                if (Gdx.input.isKeyPressed(Input.Keys.S) && (body.getLinearVelocity().y > -MAX_VELOCITY_VERTICAL)) {
                    body.applyForceToCenter(0, -20000, true);
                }

            } else {
                engineOn = false;
                if (body.getLinearVelocity().x > 0) {
                    body.applyForceToCenter(-5000, 0, true);
                }
            }
        }

        super.update();
    }
}
