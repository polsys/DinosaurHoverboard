package github.polsys.dinosaur;

import com.badlogic.gdx.math.Vector2;

/**
 * Full of anvils.
 */
public class Container extends Obstacle {

    @Override
    protected String getAssetFilename() {
        return "Container.png";
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(10, 5);
    }

}
