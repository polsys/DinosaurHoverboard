package github.polsys.dinosaur;

import com.badlogic.gdx.math.Vector2;

/**
 * Do not crash - might hurt.
 */
public class AnvilStack extends Obstacle {

    @Override
    protected String getAssetFilename() {
        return "Anvils.png";
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(3.5f, 7);
    }

}
