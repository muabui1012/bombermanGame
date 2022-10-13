package bomberman.components;

import bomberman.Constants;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

import java.awt.*;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import bomberman.BombermanType;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.*;
import static bomberman.Constants.*;

import bomberman.components.BombComponent.*;



/**
 * @author nghia
 */
public class PlayerComponent extends Component {

    private static final double ANIM_TIME = 0.3;

    private static final int SIZE_FRAMES = 48;

    private boolean exploreCancel = false;

    private int bombsCount = 0;
    private AnimatedTexture texture;

    private AnimationChannel animIdle;


    private AnimationChannel animWalk;

    private AnimationChannel animIdleDown, animIdleRight, animIdleUp, animIdleLeft;
    private AnimationChannel animWalkDown, animWalkRight, animWalkUp, animWalkLeft;

    private AnimationChannel animDie;

    public PlayerComponent() {
        PhysicsWorld physics = FXGL.getPhysicsWorld();
        physics.setGravity(0, 0);

        onCollisionBegin(BombermanType.PLAYER, BombermanType.SPEEDUP, (p, speed_i) -> {
            speed_i.removeFromWorld();
            inc("speed", SPEED / 3);


        });


        onCollisionBegin(BombermanType.PLAYER, BombermanType.POWERUP, (p, bombs_t) -> {
            bombs_t.removeFromWorld();
            inc("bomb", 1);
        });

        animDie = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(1.5), 12, 14);

        animIdleDown = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 3, 3);
        animIdleRight = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 6, 6);
        animIdleUp = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 0, 0);
        animIdleLeft = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 9, 9);

        animWalkDown = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 3, 5);
        animWalkRight = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 6, 8);
        animWalkUp = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 0, 2);
        animWalkLeft = new AnimationChannel(image("sprites.png"), 16, SIZE_FRAMES, SIZE_FRAMES,
                Duration.seconds(ANIM_TIME), 9, 11);

        texture = new AnimatedTexture(animIdleDown);

    }


    private PhysicsComponent physics;

    private int speed = SPEED * SIZE_BLOCK;

    public enum StatusDirection {
        UP, RIGHT, DOWN, LEFT, STOP,DIE
    }


    StatusDirection statusDirection = StatusDirection.DOWN;

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (physics.getVelocityX() != 0) {
            physics.setVelocityX((int) physics.getVelocityX() * 0.9);
            if (FXGLMath.abs(physics.getVelocityX()) < 1) {
                physics.setVelocityX(0);
            }
        }

        if (physics.getVelocityY() != 0) {
            physics.setVelocityY((int) physics.getVelocityY() * 0.9);
            if (FXGLMath.abs(physics.getVelocityY()) < 1) {
                physics.setVelocityY(0);
            }
        }
        switch (statusDirection) {
            case UP:
                texture.loopNoOverride(animWalkUp);
                break;
            case RIGHT:
                texture.loopNoOverride(animWalkRight);
                break;
            case DOWN:
                texture.loopNoOverride(animWalkDown);
                break;
            case LEFT:
                texture.loopNoOverride(animWalkLeft);
                break;
            case STOP:
                if (texture.getAnimationChannel() == animWalkDown) {
                    texture.loopNoOverride(animIdleDown);
                } else if (texture.getAnimationChannel() == animWalkUp) {
                    texture.loopNoOverride(animIdleUp);
                } else if (texture.getAnimationChannel() == animWalkLeft) {
                    texture.loopNoOverride(animIdleLeft);
                } else if (texture.getAnimationChannel() == animWalkRight) {
                    texture.loopNoOverride(animIdleRight);
                }
                break;
            case DIE:
                texture.loopNoOverride(animDie);
                break;

        }



    }

    public void moveRight() {
        statusDirection = StatusDirection.RIGHT;
        speed = geti("speed") * SIZE_BLOCK;
        physics.setVelocityX(speed);

    }

    public void moveLeft() {
        statusDirection = StatusDirection.LEFT;
        speed = geti("speed") * SIZE_BLOCK;
        physics.setVelocityX(-speed);
    }


    public void moveUp() {
        statusDirection = StatusDirection.UP;
        speed = geti("speed") * SIZE_BLOCK;
        physics.setVelocityY(-speed);
    }

    public void moveDown() {
        statusDirection = StatusDirection.DOWN;
        speed = geti("speed") * SIZE_BLOCK;
        physics.setVelocityY(speed);
    }

    public void stop() {
        if(statusDirection != StatusDirection.DIE) {
            statusDirection = StatusDirection.STOP;
        }
    }

    public void die() {
        statusDirection = StatusDirection.DIE;
    }

    public void placeBomb() {
        bombsCount = geti("bomb");
        if (bombsCount == 0) {
            return;
        }
        bombsCount--;
        inc("bomb", -1);
        int bombX = (int) (entity.getX() % SIZE_BLOCK > SIZE_BLOCK / 2
                ? entity.getX() + SIZE_BLOCK - entity.getX() % SIZE_BLOCK + 1
                : entity.getX() - entity.getX() % SIZE_BLOCK + 1);
        int bombY = (int) (entity.getY() % SIZE_BLOCK > SIZE_BLOCK / 2
                ? entity.getY() + SIZE_BLOCK - entity.getY() % SIZE_BLOCK + 1
                : entity.getY() - entity.getY() % SIZE_BLOCK + 1);

        Entity bomb = spawn("bomb", new SpawnData(bombX, bombY));





        getGameTimer().runOnceAfter(() -> {
            bomb.removeFromWorld();
            bombsCount++;
            inc("bomb", 1);
            bomb.getComponent(BombComponent.class).explode();
        }, Duration.seconds(2.5));

    }

    public boolean isExploreCancel() {
        return exploreCancel;
    }

    public void setExploreCancel(boolean exploreCancel) {
        this.exploreCancel = exploreCancel;
    }

}
