package github.polsys.dinosaur;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;

public class DinosaurGame extends ApplicationAdapter {

    private AssetManager assetManager;
    private GameWorld world;

    @Override
	public void create () {
	    assetManager = new AssetManager();

	    world = new GameWorld();
        world.create(assetManager);
	}

	@Override
	public void render () {
	    world.update();
        world.render();
	}
	
	@Override
	public void dispose () {
	    assetManager.dispose();
        world.dispose();
    }
}
