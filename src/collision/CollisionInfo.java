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
     * Ball & paddle collision + set paddle glow when collide.
     */
    private void handlePaddleCollision() {
        if (ball.intersects(paddle)) {
            ball.bounceVertical();
            paddle.setGlow();
        }
    }

    /**
     * Ball & paddle collision for coop mode w/ 2 paddles.
     */
    private void handlePaddleCollisionCoop(Paddle paddle1, Paddle paddle2) {
        boolean collided = false;
        if (ball.intersects(paddle1)) {
            ball.bounceVertical();
            paddle1.setGlow();
            collided = true;
        }

        if (ball.intersects(paddle2)) {
            ball.bounceVertical();
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