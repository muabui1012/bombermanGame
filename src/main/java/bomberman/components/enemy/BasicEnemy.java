package bomberman.components.enemy;

import bomberman.BombermanType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;

/**
 * @author kien
 */

public abstract class BasicEnemy extends Component {
    private double lastX = 0;
    private double lastY = 0;

    private enum TurnDirection {
        BLOCK_LEFT, BLOCK_RIGHT, BLOCK_UP, BLOCK_DOWN
    }

    protected double dx = G_Const.ENEMY_SPEED_BASE;
    protected double dy = 0;

    protected AnimatedTexture texture;
    protected static final double ANIM_TIME = 0.5;
    protected static final int SIZE_FLAME = 48;

    protected AnimationChannel animWalkRight;
    protected AnimationChannel animWalkLeft;
    protected AnimationChannel animDie;
    protected AnimationChannel animStop;

    protected int rangeDetectPlayer = 60;

    protected abstract void setAnimationMove();

    public BasicEnemy() {
        setAnimationMove();

        texture = new AnimatedTexture(animWalkRight);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    //tpf ??
    @Override
    public void onUpdate(double tpf) {
        entity.setScaleUniform(0.9);
        entity.translateX(dx * tpf);
        entity.translateY(dy * tpf);

        setAnimationStage();
    }

    protected void setAnimationStage() {
        double tmp_dx = entity.getX() - lastX;
        double tmp_dy = entity.getY() - lastY;

        lastX = entity.getX();
        lastY = entity.getY();

        if (tmp_dx == 0 && tmp_dy == 0) {
            return;
        }

        if (Math.abs(tmp_dx) > Math.abs(tmp_dy)) {
            if (tmp_dx < 0) {
                texture.loopNoOverride(animWalkLeft);
            } else {
                texture.loopNoOverride(animWalkRight);
            }
        } else {
            if (tmp_dy < 0) {
                texture.loopNoOverride(animWalkLeft);
            } else {
                texture.loopNoOverride(animWalkRight);
            }
        }
    }

    protected double randomDirectionorVorVelocity() {
        return Math.random() > 0.5 ? G_Const.ENEMY_SPEED_BASE : -G_Const.ENEMY_SPEED_BASE;
    }

    protected void setTurnEnemy(TurnDirection ahead) {
        if (ahead == TurnDirection.BLOCK_LEFT) {
            entity.translateX(3);
            dx = 0;
            dy = randomDirectionorVorVelocity();
        }
        if (ahead == TurnDirection.BLOCK_RIGHT) {
            entity.translateX(-3);
            dx = 0;
            dy = randomDirectionorVorVelocity();
        }
        if (ahead == TurnDirection.BLOCK_UP) {
            entity.translateY(3);
            dx = randomDirectionorVorVelocity();
            dy = 0;
        }
        if (ahead == TurnDirection.BLOCK_DOWN) {
            entity.translateY(-3);
            dx = randomDirectionorVorVelocity();
            dy = 0;
        }
    }

    protected void turn() {
        if (dx < 0) {
            setTurnEnemy(TurnDirection.BLOCK_LEFT);
        }
        else
        if (dx > 0) {
            setTurnEnemy(TurnDirection.BLOCK_RIGHT);
        }
        else
        if (dy < 0) {
            setTurnEnemy(TurnDirection.BLOCK_UP);
        }
        else
        if (dy > 0) {
            setTurnEnemy(TurnDirection.BLOCK_DOWN);
        }
    }

    protected boolean isDetectPlayer() {
        BoundingBoxComponent bbox = entity.getBoundingBoxComponent();
        List<Entity> list = FXGL.getGameWorld().getEntitiesInRange(bbox.range(rangeDetectPlayer,rangeDetectPlayer));
        for(Entity entity : list) {
            if (entity.isType(BombermanType.BOMB)) {
                return false;
            }
        }
        for(Entity entity : list) {
            if (entity.isType(BombermanType.PLAYER)) {
                return true;
            }
        }
        return false;
    }
    protected void showScore(int score) {
        Label labelScore = new Label();
        labelScore.setText(score + "!");
        labelScore.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 15));
        labelScore.setTextFill(Color.WHITE);
        FXGL.addUINode(labelScore, entity.getX() + 24, entity.getY() + 24);
        FXGL.getGameTimer().runOnceAfter(() -> FXGL.removeUINode(labelScore), Duration.seconds(2));
    }

    public void enemyDie() {
        dx = 0;
        dy = 0;
        texture.loopNoOverride(animDie);
    }

    public void enemyStop() {
        dx = 0;
        dy = 0;
        texture.loopNoOverride(animStop);
    }

    protected void setRangeDetectPlayer(int rangeDetectPlayer) {
        this.rangeDetectPlayer = rangeDetectPlayer;
    }
}
