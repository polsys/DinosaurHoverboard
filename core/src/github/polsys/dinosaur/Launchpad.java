package github.polsys.dinosaur;

import com.badlogic.gdx.math.Vector2;

/**
 * Start here, land here.
 */
public class Launchpad extends Obstacle {

    @Override
    protected String getAssetFilename() {
        return "Launchpad.png";
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(8, 8);
    }

    @Override
    public boolean isLethal() {
        return false;
    }
}
