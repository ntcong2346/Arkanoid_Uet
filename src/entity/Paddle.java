package entity;

import graphics.Assets;
import powerup.PowerUpManager;

import java.awt.*;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Paddle extends MovableObject implements Serializable{
    private int speed;
    private boolean glowing;
    private int glowTimer;
    private boolean isWideActive = false;
    private boolean isLaserActive = false;
    private int originalWidth;

    // Timer cho hiệu ứng Laser
    private transient Timer laserShootTimer;
    private transient Timer laserDeactivationTimer;
    private static final int LASER_DURATION_MS = 4000;

    // --- THÊM MỚI: Timer cho hiệu ứng Wide Paddle ---
    private transient Timer widePaddleDeactivationTimer;
    private static final int WIDE_PADDLE_DURATION_MS = 10000;

    public Paddle(double x, double y, int width, int height, int speed) {
        super(x, y, width, height);
        this.speed = speed;
        this.glowing = false;
        this.glowTimer = 0;
        this.originalWidth = width; // Lưu lại chiều rộng ban đầu khi khởi tạo
    }

    // --- LOGIC CHO LASER ---
    public void activateLaser() {
        isLaserActive = true;
        if (laserShootTimer == null) {
            startShooting();
        }
        resetLaserDeactivationTimer();
    }

    public void deactivateLaser() {
        isLaserActive = false;
        if (laserShootTimer != null) {
            laserShootTimer.cancel();
            laserShootTimer = null;
        }
        if (laserDeactivationTimer != null) {
            laserDeactivationTimer.cancel();
            laserDeactivationTimer = null;
        }
    }

    private void startShooting() {
        if (laserShootTimer != null) return;
        laserShootTimer = new Timer();
        laserShootTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isLaserActive) PowerUpManager.shootDualLasers(Paddle.this);
                else cancel();
            }
        }, 0, 1000);
    }

    private void resetLaserDeactivationTimer() {
        if (laserDeactivationTimer != null) laserDeactivationTimer.cancel();
        laserDeactivationTimer = new Timer();
        laserDeactivationTimer.schedule(new TimerTask() {
            @Override
            public void run() { deactivateLaser(); }
        }, LASER_DURATION_MS);
    }

    // --- LOGIC MỚI CHO WIDE PADDLE ---
    public void activateWidePaddle() {
        if (!isWideActive) {
            isWideActive = true;
            this.width = this.originalWidth * 2;
        }
        resetWidePaddleDeactivationTimer();
    }

    public void deactivateWidePaddle() {
        if (isWideActive) {
            isWideActive = false;
            this.width = this.originalWidth;
        }
        if (widePaddleDeactivationTimer != null) {
            widePaddleDeactivationTimer.cancel();
            widePaddleDeactivationTimer = null;
        }
    }

    private void resetWidePaddleDeactivationTimer() {
        if (widePaddleDeactivationTimer != null) widePaddleDeactivationTimer.cancel();
        widePaddleDeactivationTimer = new Timer();
        widePaddleDeactivationTimer.schedule(new TimerTask() {
            @Override
            public void run() { deactivateWidePaddle(); }
        }, WIDE_PADDLE_DURATION_MS);
    }

    public double getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public void moveLeft() {
        x -= speed;
        if (getLeft() < 0) x = width / 2.0;
    }

    public void moveRight(int panelWidth) {
        x += speed;
        if (getRight() > panelWidth) x = panelWidth - width / 2.0;
    }

    public void setGlow() {
        glowing = true;
        glowTimer = 15;
    }

    public void updateGlow() {
        if (glowing) {
            glowTimer--;
            if (glowTimer <= 0) glowing = false;
        }
    }

    @Override
    public void update() { updateGlow(); }

    @Override
    public void render(Graphics g) {
        int drawX = (int)getLeft();
        int drawY = (int)getTop();
        if (glowing) g.drawImage(Assets.paddleGlow, drawX, drawY, width, height, null);
        else g.drawImage(Assets.paddleNormal, drawX, drawY, width, height, null);
    }

    public Rectangle getBounds(){ return new Rectangle((int)getLeft(), (int)getTop(), width, height); }
    public boolean isWideActive() { return isWideActive; }
    public boolean isLaserActive() { return isLaserActive; }
}