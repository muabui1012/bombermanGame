package bomberman.components.enemy;

import bomberman.BombermanType;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
* @author kiennguyentuan
 */
public class PassComponent extends OnealComponent {
    public PassComponent() {
        super();
        onCollisionBegin(BombermanType.PASS_E, BombermanType.SURROUND,
                (pass, surround) -> pass.getComponent(PassComponent.class).turn());

        onCollisionBegin(BombermanType.PASS_E, BombermanType.WALL,
                (pass, wall) -> pass.getComponent(PassComponent.class).turn());

        onCollisionBegin(BombermanType.PASS_E, BombermanType.BRICK,
                (pass, brick) -> pass.getComponent(PassComponent.class).turn());

        onCollisionBegin(BombermanType.PASS_E, BombermanType.GRASS,
                (pass, grass) -> pass.getComponent(PassComponent.class).turn());


        setRangeDetectPlayer(120);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
    }


    @Override
    protected void setAnimationMove() {
        animDie = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 70, 70);
        animWalkRight = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 67, 69);
        animWalkLeft = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 64, 66);
        animStop = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 64, 69);
    }

    @Override
    public void enemyDie() {
        int PASS_SCORE = 300;
        showScore(PASS_SCORE);
        inc("score", PASS_SCORE);

        die = true;
        dx = 0;
        dy = 0;
        astar.pause();
        texture.loopNoOverride(animDie);
    }
}
