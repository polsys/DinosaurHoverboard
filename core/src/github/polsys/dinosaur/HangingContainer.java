package github.polsys.dinosaur;

import com.badlogic.gdx.math.Vector2;

/**
 * Full of anvils. This time in the sky.
 */
public class HangingContainer extends Obstacle {

    @Override
    protected String getAssetFilename() {
        return "Container_Hanging.png";
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(10, 5);
    }

}
