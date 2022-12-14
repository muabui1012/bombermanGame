package bomberman.components.enemy;

import bomberman.BombermanType;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static bomberman.components.enemy.G_Const.ENEMY_SPEED_BASE;
import static com.almasb.fxgl.dsl.FXGL.*;

public class DahlComponent extends BasicEnemy {
    public DahlComponent() {
        super();
        onCollisionBegin(BombermanType.DAHL_E, BombermanType.WALL,
                (dahl, wall) -> dahl.getComponent(DahlComponent.class).turn());

        onCollisionBegin(BombermanType.DAHL_E, BombermanType.BOMB,
                (dahl, bom) -> dahl.getComponent(DahlComponent.class).turn());

        onCollisionBegin(BombermanType.DAHL_E, BombermanType.SURROUND,
                (dahl, surround) -> dahl.getComponent(DahlComponent.class).turn());

        onCollisionBegin(BombermanType.DAHL_E, BombermanType.BRICK,
                (dahl, brick) -> dahl.getComponent(DahlComponent.class).turn());



    }

    @Override
    protected void setAnimationMove() {
        animDie = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 54, 54);
        animWalkRight = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 51, 53);
        animWalkLeft = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 48, 50);
        animStop = new AnimationChannel(image("sprites.png"), 16, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 48, 53);
    }

    @Override
    protected double randomDirectionorVorVelocity() {
        double coefficient = 0;
//        while (coefficient < 0.7) {
//            coefficient = Math.random();
//        }
        coefficient = Math.random()*0.3 + 0.7;
        double speed = ENEMY_SPEED_BASE * (coefficient + (Math.random() > 0.5 ? 0 : 0.8));
        return Math.random() > 0.5 ? speed : -speed;
    }

    @Override
    public void enemyDie() {
        super.enemyDie();

        int DAHL_SCORE = 150;
        showScore(DAHL_SCORE);
        inc("score", DAHL_SCORE);
    }
}
