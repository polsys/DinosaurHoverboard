package github.polsys.dinosaur;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class DinosaurGame extends ApplicationAdapter {

    private static final float VIEWPORT_WIDTH = 30;
    private static final float VIEWPORT_HEIGHT = 30 * (9f / 16);

    private AssetManager assetManager;
	private SpriteBatch batch;
    private Camera camera;
    private Matrix4 defaultProjectionMatrix;

    private Dinosaur player;
    private Launchpad launchpad;
	private Texture background;
	
	@Override
	public void create () {
	    assetManager = new AssetManager();
        assetManager.load("Dinosaur.png", Texture.class);
        assetManager.load("Dinosaur_Jet.png", Texture.class);
        assetManager.load("Launchpad.png", Texture.class);
        assetManager.finishLoading();

		batch = new SpriteBatch();
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        defaultProjectionMatrix = batch.getProjectionMatrix().cpy();

        background = new Texture("Background.png");

        player = new Dinosaur();
        player.position = new Vector2(4, 10.5f);
        player.load(assetManager);

        launchpad = new Launchpad();
        launchpad.position = new Vector2(4, 4);
        launchpad.load(assetManager);
	}

	@Override
	public void render () {
	    // Update
        player.update();
        launchpad.update();
        camera.position.set(player.position.x + 8, VIEWPORT_HEIGHT / 2, 0);
        camera.update();

        // Render
		Gdx.gl.glClearColor(0.45f, 0.75f, 0.81f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Background - manually controlled translation
        batch.setProjectionMatrix(defaultProjectionMatrix);
        batch.draw(background, 0, 0);

        // Objects - automatic translation
        batch.setProjectionMatrix(camera.combined);
		player.draw(batch);
        launchpad.draw(batch);

		batch.end();
	}
	
	@Override
	public void dispose () {
	    assetManager.dispose();
		batch.dispose();
        background.dispose();
	}
}
