package github.polsys.dinosaur;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Anything that is not a dinosaur or a pickup.
 */
public abstract class Obstacle extends GameObject {

    private Sprite sprite;
    protected Body body;

    protected Vector2 getSize() { return Vector2.Zero; }

    protected String getAssetFilename() { return null; }

    public boolean isLethal() {
        return true;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void addToWorld(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        Vector2 size = getSize();
        shape.setAsBox(size.x / 2, size.y / 2);

        body.createFixture(shape, 0);
        shape.dispose();
    }

    @Override
    public void load(AssetManager assetManager) {
        String assetFilename = getAssetFilename();
        if (assetFilename != null) {
            Texture texture = assetManager.get(assetFilename);
            sprite = new Sprite(texture);
            Vector2 size = getSize();
            sprite.setSize(size.x, size.y);
            sprite.setOriginCenter();
        }
    }

}
