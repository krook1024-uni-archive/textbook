package com.krook1024.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class SnakeEngine extends SurfaceView implements Runnable {
    // Game thread
    private Thread thread = null;

    // To hold a reference to the Activity
    private Context context;

    // Movement
    public enum Heading {UP, RIGHT, DOWN, LEFT}

    private Heading heading = Heading.RIGHT; // Head RIGHT by def.

    // Screen size
    private int screenX, screenY;

    // Apple
    private int appleX, appleY;

    // Size of a segment
    private int blockSize;

    // Playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int numBlocksHigh;

    // Control pausing between updates
    private long nextFrameTime;

    // 10 FPS
    private final long FPS = 10;

    private final long MILLI_PER_SECOND = 1000;

    // Player points
    private int score;

    // Location of the snake
    private int[] snakeXs;
    private int[] snakeYs;
    private int snakeLength;

    // Is the player currently playing?
    private volatile boolean isPlaying;

    // Canvas for painting
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    // Paint
    private Paint paint;

    public SnakeEngine(Context context, Point size) {
        super(context);

        context = context;

        screenX = size.x;
        screenY = size.y;

        blockSize = screenX / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenY / blockSize;

        surfaceHolder = getHolder();
        paint = new Paint();

        snakeXs = new int[200];
        snakeYs = new int[200];

        newGame();
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (updateRequired()) {
                update();
                draw();
            }
        }
    }

    public boolean updateRequired() {
        if (nextFrameTime <= System.currentTimeMillis()) {
            nextFrameTime = System.currentTimeMillis() + MILLI_PER_SECOND / FPS;
            return true;
        }
        return false;
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void newGame() {

        snakeLength = 1;
        snakeXs[0] = NUM_BLOCKS_WIDE / 2;
        snakeYs[0] = numBlocksHigh / 2;

        spawnApple();

        score = 0;

        nextFrameTime = System.currentTimeMillis();
    }

    public void spawnApple() {
        Random random = new Random();
        appleX = random.nextInt(NUM_BLOCKS_WIDE - 1) + 1;
        appleY = random.nextInt(numBlocksHigh - 1) + 1;
    }

    private void eatApple() {
        ++snakeLength;
        ++score;
        spawnApple();
    }

    private void moveSnake() {
        // Pop last elem
        for (int i = snakeLength; i > 0; i--) {
            snakeXs[i] = snakeXs[i - 1];
            snakeYs[i] = snakeYs[i - 1];
        }

        switch (heading) {
            case UP:
                --snakeYs[0];
                break;
            case RIGHT:
                ++snakeXs[0];
                break;

            case DOWN:
                ++snakeYs[0];
                break;

            case LEFT:
                --snakeXs[0];
                break;
        }
    }

    private boolean detectDeath() {
        boolean dead = false;

        // Screen edge death
        if (snakeXs[0] == -1) dead = true;
        if (snakeXs[0] >= numBlocksHigh) dead = true;
        if (snakeYs[0] == -1) dead = true;
        if (snakeYs[0] == numBlocksHigh) dead = true;


        // Eaten itself death
        for (int i = snakeLength - 1; i > 0; --i) {
            if ((i > 4) && (snakeXs[0] == snakeXs[i]) && (snakeYs[0] == snakeYs[i])) {
                dead = true;
            }
        }

        return dead;
    }

    public void update() {
        // Found apple?
        if (snakeXs[0] == appleX && snakeYs[0] == appleY) {
            eatApple();
        }

        moveSnake();

        if (detectDeath())
            newGame();
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // Background: Nord
            canvas.drawColor(Color.argb(255, 56, 66, 82));

            // Snake's color: Nord #A3BE8C
            paint.setColor(Color.argb(255, 163, 190, 140));

            // Font size
            paint.setTextSize(90);
            canvas.drawText("Score: " + score, 10, 70, paint);

            // Draw the snake block-by-block
            for (int i = 0; i < snakeLength; ++i) {
                canvas.drawRect(snakeXs[i] * blockSize,
                        (snakeYs[i] * blockSize),
                        (snakeXs[i] * blockSize) + blockSize,
                        (snakeYs[i] * blockSize) + blockSize,
                        paint);
            }

            // Apple color: Nord red
            paint.setColor(Color.argb(255, 191, 97, 106));

            // Draw apple
            canvas.drawRect(appleX * blockSize,
                    (appleY * blockSize),
                    (appleX * blockSize) + blockSize,
                    (appleY * blockSize) + blockSize,
                    paint);

            // Unlock canvas
            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            double x = motionEvent.getX();
            double y = motionEvent.getY();

            if (heading == Heading.UP || heading == Heading.DOWN) {
                if (x >= screenX / 2)
                    heading = Heading.RIGHT;
                else
                    heading = Heading.LEFT;
            } else {
                if (y <= screenY / 2)
                    heading = Heading.UP;
                else
                    heading = Heading.DOWN;
            }

        }

        return true;
    }
}
