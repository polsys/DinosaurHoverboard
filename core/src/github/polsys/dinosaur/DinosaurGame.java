package github.polsys.dinosaur;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DinosaurGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture background;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("Dinosaur.png");
        background = new Texture("Background.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.45f, 0.75f, 0.81f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        batch.draw(background, 0, 0);
		batch.draw(img, 200, 200, 400, 400);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
        background.dispose();
	}
}
