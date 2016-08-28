package github.polsys.dinosaur;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Base class for all game objects.
 */
abstract class GameObject {

    /**
     * The position of this object.
     */
    public Vector2 position;

    /**
     * Gets the drawable idleSprite for this object.
     * May be null if the object is invisible.
     */
    public Sprite getSprite() { return null; }

    public GameObject() {
        position = new Vector2(0, 0);
    }

    /**
     * Adds the object to the physics world.
     *
     * @param world The Box2D world.
     */
    public void addToWorld(World world) { }

    /**
     * Loads the assets for this object and performs other initialization.
     *
     * @param assetManager The asset manager to load the assets from.
     */
    public void load(AssetManager assetManager) { }

    /**
     * Updates the state of this object.
     */
    public void update() {
        Sprite sprite = getSprite();
        if (sprite != null) {
            sprite.setPosition(position.x - sprite.getOriginX(), position.y - sprite.getOriginY());
        }
    }

    /**
     * Draws this object.
     *
     * @param spriteBatch The SpriteBatch to use for drawing.
     */
    public void draw(SpriteBatch spriteBatch) {
        Sprite sprite = getSprite();
        if (sprite != null) {
            sprite.draw(spriteBatch);
        }
    }

}
