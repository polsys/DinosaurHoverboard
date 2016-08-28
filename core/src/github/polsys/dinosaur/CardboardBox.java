package github.polsys.dinosaur;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Just hope it isn't an anvil delivery.
 */
public class CardboardBox extends Obstacle {

    @Override
    protected String getAssetFilename() {
        return "Box.png";
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(2f, 2f);
    }

    @Override
    public boolean isLethal() {
        return false;
    }

    @Override
    public void addToWorld(World world) {
        // Overridden because the boxes are dynamic bodies

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.setAwake(false); // Avoid initial jitter

        PolygonShape shape = new PolygonShape();
        Vector2 size = getSize();
        shape.setAsBox(size.x / 2, size.y / 2);

        body.createFixture(shape, 5);
        shape.dispose();
    }

    @Override
    public void update() {
        getSprite().setPosition(body.getPosition().x - getSprite().getOriginX(), body.getPosition().y - getSprite().getOriginY());
        getSprite().setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

}
