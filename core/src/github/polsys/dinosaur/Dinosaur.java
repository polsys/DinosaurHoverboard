package github.polsys.dinosaur;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * THE DINOSAUR!!!
 */
class Dinosaur extends GameObject {

    private static final float DINOSAUR_SIZE = 5f;

    private Sprite idleSprite;
    private Sprite jetSprite;
    private boolean engineOn;

    @Override
    public Sprite getSprite() {
        return engineOn ? jetSprite : idleSprite;
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

        // Input
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            // TODO: Apply real force
            engineOn = true;
            position = position.add(0.1f, 0);
        }
        else {
            engineOn = false;
        }

        super.update();
    }
}
