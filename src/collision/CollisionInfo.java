package collision;

import entity.Ball;
import entity.Paddle;
import entity.Brick;
import game.GamePanel;
import powerup.PowerUp;
import powerup.PowerUpFactory;
import powerup.PowerUpManager;

import java.awt.*;
import java.util.ArrayList;

public class CollisionInfo {
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Brick> bricks;

    // Maximum angle that ball can bounce (~50 degrees for balanced gameplay).
    private final double maxBounceAngle = Math.toRadians(50);

    public CollisionInfo(Ball ball, Paddle paddle, ArrayList<Brick> bricks) {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;
    }

    public int updateCollision() {
        handleWallCollision();
        handlePaddleCollision();
        return handleBrickCollision();
    }

    public int updateCollisionCoop(Paddle paddle1, Paddle paddle2) {
        handleWallCollision();
        handlePaddleCollisionCoop(paddle1, paddle2);
        return handleBrickCollision();
    }

    /**
     * Ball & wall collision.
     */
    private void handleWallCollision() {
        if (ball.getX() - ball.getRadius() <= 0 || ball.getX() + ball.getRadius() >= GamePanel.WIDTH)
            ball.bounceHorizontal();
        if (ball.getY() - ball.getRadius() <= 0)
            ball.bounceVertical();
    }

    /**
     * Ball bouncing collision handler.
     */
    private void handleBallCollision(Paddle p) {
        // Compute ball's hit location on paddle.
        // Normalize values between -1 and 1 to determine the position.
        // Formula: (ball position - paddle middle position) / (paddle / 2).
        // -1 = ball hit left.
        // 0 = ball hit middle.
        // 1 = ball hit right.
        double ballHitPos = (ball.getX() - p.getX()) / (p.getWidth() / 2.0);

        // Limit values to -1 and 1 to avoid extreme angles.
        ballHitPos = Math.max(-1.0, Math.min(1.0, ballHitPos));

        // Compute bounce angle based on ball's hit position.
        // Hit middle -> 0 degrees -> fly up vertically
        // Hit left or right -> varies +- maxBounceAngle.
        double ballBounceAngle = ballHitPos * maxBounceAngle;
        double speed = ball.getSpeed();

        // Compute ball speed horizontally & vertically based on angle.
        // Breakout default up axis is 0 degrees -> rotate axis 90 degrees -> different sin cos formula.
        ball.setDx(speed * Math.sin(ballBounceAngle)); // Horizontal movement
        ball.setDy(-speed * Math.cos(ballBounceAngle)); // Vertical movement (screen Y increases downward in Java)x
    }
    /**
     * Ball & paddle collision + set paddle glow when collided.
     */
    private void handlePaddleCollision() {
        if (ball.intersects(paddle)) {
            paddle.setGlow();
            handleBallCollision(paddle);
        }
    }

    /**
     * Ball & paddle collision for coop mode w/ 2 paddles.
     */
    private void handlePaddleCollisionCoop(Paddle paddle1, Paddle paddle2) {
        boolean collided = false;
        if (ball.intersects(paddle1)) {
            handleBallCollision(paddle1);
            paddle1.setGlow();
            collided = true;
        }

        if (!collided && ball.intersects(paddle2)) {
            handleBallCollision(paddle2);
            paddle2.setGlow();
        }
    }

    /**
     * Ball & brick collision.
     */
    private int handleBrickCollision() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.getRect().intersects(brick.getRect())) {
                Rectangle ballRect = ball.getRect();
                Rectangle brickRect = brick.getRect();

                // Xác định vùng va chạm
                Rectangle intersectZone = brickRect.intersection(ballRect);

                if (intersectZone.width >= intersectZone.height) {
                    // Chiều rộng vùng va chạm > chiều cao => va chạm vào phần trên/dưới gạch => bật lại dọc
                    ball.bounceVertical();
                } else {
                    // Chiều rộng vùng va chạm < chiều cao => va chạm vàp phần trái/phải gạch => bật lại ngang
                    ball.bounceHorizontal();
                }
                // Dịch chuyển quá bóng ra khỏi gạch 1 ít để tránh bị kẹt
                ball.move();
                return brick.takeHit(bricks);
            }
        }
        return 0;
    }
}