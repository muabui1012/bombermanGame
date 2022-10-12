package bomberman.components;

import bomberman.BombermanType;
import bomberman.Constants;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import bomberman.Constants.*;
import bomberman.BombermanType.*;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author nghia
 */
public class FlameComponent extends Component {
    private final AnimatedTexture texture;

    public FlameComponent(int startFrame, int endFrame) {
        PhysicsWorld physics = FXGL.getPhysicsWorld();

        onCollisionBegin(BombermanType.FLAME, BombermanType.WALL, (f, w) -> {
            f.removeFromWorld();
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.SURROUND, (f, w) -> {
            f.removeFromWorld();
        });

        setCollisionBreak(BombermanType.BRICK, "brick_break");


        AnimationChannel animFlame = new AnimationChannel(FXGL.image("sprites.png"),
                16, Constants.SIZE_BLOCK, Constants.SIZE_BLOCK, Duration.seconds(0.4), startFrame, endFrame);

        texture = new AnimatedTexture(animFlame);
        texture.loop();


    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    private void setCollisionBreak(BombermanType type, String nameTypeBreakAnim) {
        onCollisionBegin(BombermanType.FLAME, type, (f, t) -> {
            int cellX = (int)((t.getX() + 24) / Constants.SIZE_BLOCK);
            int cellY = (int)((t.getY() + 24) / Constants.SIZE_BLOCK);

            AStarGrid grid = geto("grid");
            grid.get(cellX, cellY).setState(CellState.WALKABLE);
            set("grid", grid);

            Entity bBreak = spawn(nameTypeBreakAnim, new SpawnData(t.getX(), t.getY()));
            t.removeFromWorld();
            getGameTimer().runOnceAfter(bBreak::removeFromWorld, Duration.seconds(1));
        });
    }

}
