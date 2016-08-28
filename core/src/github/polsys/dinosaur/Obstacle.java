package github.polsys.dinosaur;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Anything that is not a dinosaur or a pickup.
 */
public abstract class Obstacle extends GameObject {

    private Sprite sprite;

    protected Vector2 getSize() { return Vector2.Zero; }

    protected String getAssetFilename() { return null; }

    @Override
    public Sprite getSprite() {
        return sprite;
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
