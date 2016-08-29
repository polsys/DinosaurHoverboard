package github.polsys.dinosaur;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DinosaurGame extends ApplicationAdapter {

    private AssetManager assetManager;
    private GameWorld world;

    private Texture startScreen;
    private Texture endScreen;
    private SpriteBatch batch;

    private enum GameState {
        START,
        GAMEPLAY,
        END
    }
    private GameState state;

    @Override
	public void create () {
	    assetManager = new AssetManager();
        assetManager.load("Start.png", Texture.class);
        assetManager.load("End.png", Texture.class);
        assetManager.finishLoading();

        startScreen = assetManager.get("Start.png");
        endScreen = assetManager.get("End.png");
        batch = new SpriteBatch();

        state = GameState.START;
	}

	@Override
	public void render () {
	    switch (state) {
            case START:
                if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
                    playGame();
                drawStartScreen();
                break;
            case GAMEPLAY:
                updateAndDrawGame();
                break;
            case END:
                if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
                    playGame();
                drawEndScreen();
                break;
        }
	}

    private void playGame() {
        if (world != null)
            world.dispose();
        world = new GameWorld();
        world.create(assetManager);

        state = GameState.GAMEPLAY;
    }

    private void drawStartScreen() {
        batch.begin();
        batch.draw(startScreen, 0, 0);
        batch.end();
    }

    private void drawEndScreen() {
        batch.begin();
        batch.draw(endScreen, 0, 0);
        batch.end();
    }

    private void updateAndDrawGame() {
        if (world.shouldRestart()) {
            playGame();
        }
        if (world.isGameOver()) {
            state = GameState.END;
        }

        world.update();
        world.render();
    }

    @Override
	public void dispose () {
	    assetManager.dispose();
        world.dispose();
    }
}
