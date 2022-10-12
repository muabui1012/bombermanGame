package bomberman.components.enemy;


import bomberman.BombermanType;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author kien
 */
public class BalloomComponent extends BasicEnemy {

    public BalloomComponent() {
        super();
        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.WALL,
                (balloom, wall) -> balloom.getComponent(BalloomComponent.class).turn());

        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.BOMB,
                (balloom, bom) -> balloom.getComponent(BalloomComponent.class).turn());

        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.SURROUND,
                (balloomb, surround) -> balloomb.getComponent(BalloomComponent.class).turn());

        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.BRICK,
                (balloom, brick) -> balloom.getComponent(BalloomComponent.class).turn());



    }

    @Override
    protected void setAnimationMove() {
        animDie = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 22, 22);
        animWalkRight = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 19, 21);
        animWalkLeft = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 16, 18);
        animStop = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 16, 22);
    }

    @Override
    public void enemyDie() {
        super.enemyDie();

        int BALLOOM_SCORE = 100;
        showScore(BALLOOM_SCORE);
        inc("score", BALLOOM_SCORE);
    }
}
