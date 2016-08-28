package github.polsys.dinosaur;

import com.badlogic.gdx.math.Vector2;

/**
 * The leaves are really nasty in your face.
 */
public class PalmTree extends Obstacle {

    @Override
    protected String getAssetFilename() {
        return "Palm.png";
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(5, 10);
    }

}
