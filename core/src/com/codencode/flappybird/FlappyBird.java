package com.codencode.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird[];
	Texture tubes[];
	Texture gameOver;
	float birdX , birdY;
	float screenWidth , screenHeight;
	float velocity = 0;

	int numberOfTubes = 4;
	float tubePos[] , tubeGapY[];
	float distanceBewteenTubes;
	int gameState = 0;
	int birdState = 0;
	int score = 0;
	int flapCount = 0;
	float gap = 300f;
	float tubeSpeed = 3;
	boolean vis[];

	BitmapFont font;
	Circle birdCircle;
	Rectangle topRect[] , bottomRect[];


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver = new Texture("game_over.png");

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		// score font to represent it on the bottom down of the screen
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(2.5f);

		tubes = new Texture[2];
		tubePos = new float[numberOfTubes];
		tubeGapY = new float[numberOfTubes];
		topRect = new Rectangle[numberOfTubes];
		bottomRect = new Rectangle[numberOfTubes];

		tubes[0] = new Texture("toptube.png");
		tubes[1] = new Texture("bottomtube.png");
		distanceBewteenTubes = screenWidth * 0.6f;
		vis = new boolean[numberOfTubes];

		bird = new Texture[2];
		bird[0] = new Texture("bird.png");
		bird[1] = new Texture("bird2.png");
		birdCircle = new Circle();


		init();
	}

	@Override
	public void render () {

        batch.begin();
        batch.draw(background , 0 , 0 , screenWidth , screenHeight);


		flapCount++;
		if(flapCount == 5)
		{
			flapCount = 0;
			birdState ^= 1;
		}

		if(Gdx.input.justTouched())
		{
			if(gameState == 2)
			{
				init();
			}
			gameState = 1;
			if(birdY < screenHeight - bird[0].getWidth())
			velocity = -14;
		}

		if(gameState == 1)
		{
			velocity++;

			if(birdY > velocity)
			{
				birdY -= velocity;
			}


			for(int i=0;i<numberOfTubes;i++)
			{
				if(tubePos[i] < -tubes[0].getWidth()){
					tubePos[i] = tubePos[(i+3)%4] + distanceBewteenTubes;

					vis[i] = false;
					Random random = new Random();
					float deltaY = (float) ((random.nextFloat() - 0.5)*gap);
					tubeGapY[i] = deltaY;
				}
				tubePos[i] -= tubeSpeed;
				batch.draw(tubes[0] ,tubePos[i], screenHeight/2 + 140 + tubeGapY[i]);
				batch.draw(tubes[1] ,tubePos[i], screenHeight/2 - tubes[1].getHeight() - 190 + tubeGapY[i]);

				birdCircle.set(screenWidth/2 , birdY + bird[birdState].getHeight()/2 , bird[birdState].getWidth()/2);

				topRect[i].set(tubePos[i] , screenHeight/2 + 140 + tubeGapY[i] , tubes[0].getWidth() , tubes[0].getHeight());
				bottomRect[i].set(tubePos[i] ,screenHeight/2 - tubes[1].getHeight() - 190 + tubeGapY[i] , tubes[0].getWidth() , tubes[0].getHeight() );

				if(tubePos[i] + tubes[0].getWidth() < screenWidth/2 && !vis[i])
				{
					vis[i] = true;
					score++;
				}

				if(Intersector.overlaps(birdCircle , bottomRect[i]) || Intersector.overlaps(birdCircle , topRect[i]))
				{
					gameState = 2;
				}
			}
		}
		else
		    if(gameState == 2)
            {
                batch.draw(gameOver , screenWidth/2 - gameOver.getWidth()/2 , screenHeight/2 - gameOver.getHeight()/2);
            }

		font.draw(batch , "Score :" +String.valueOf(score) , 70 , screenHeight-50);
		batch.draw(bird[birdState] , birdX, birdY);
        batch.end();


	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	void init()
    {
        score = 0;
        velocity = 0;

        birdX = screenWidth / 2 - bird[0].getWidth()/2;
        birdY = screenHeight/2 - bird[0].getHeight()/2;

        for(int i=0;i<numberOfTubes;i++)
        {
            tubePos[i] = screenWidth + i*distanceBewteenTubes;
            topRect[i] = new Rectangle();
            bottomRect[i] = new Rectangle();
            vis[i] = false;
            if(i % 2 == 0)
                tubeGapY[i] = 100;
        }
    }

}
