package bomberman.components;

import bomberman.BombermanApp;
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
import bomberman.components.enemy.*;


import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author nghia
 */
public class FlameComponent extends Component {
    private final AnimatedTexture texture;

    private static Entity getPlayer() {
        return FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);
    }

    public FlameComponent(int startFrame, int endFrame) {
        PhysicsWorld physics = FXGL.getPhysicsWorld();

        onCollisionBegin(BombermanType.FLAME, BombermanType.WALL, (f, w) -> {
            f.removeFromWorld();
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.SURROUND, (f, w) -> {
            f.removeFromWorld();
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.PLAYER, (f, p) -> {
            getPlayer().getComponent(PlayerComponent.class).die();
            showMessage("Game over!!", () -> getGameController().gotoMainMenu());
            p.removeFromWorld();

        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.ENEMY, (f, e) -> {
            double x = e.getX();
            double y = e.getY();
            getGameTimer().runOnceAfter(() -> {
                e.removeFromWorld();

                getGameTimer().runOnceAfter(() -> {
                    Entity en = spawn("player_break");
                    en.removeFromWorld();

                }, Duration.seconds(2));

            }, Duration.seconds(0.3));


        });


        setCollisionBreak(BombermanType.BRICK, "brick_break");

        setCollisionBreak(BombermanType.ENEMY, "enemy_break");

        onCollisionBegin(BombermanType.FLAME, BombermanType.BALLOOM_E, (f, b) -> {
            double x = b.getX();
            double y = b.getY();
            b.getComponent(BalloomComponent.class).enemyDie();
            getGameTimer().runOnceAfter(() -> {
                //inc("enemy", -1);
                b.removeFromWorld();
                set("enemy", getEnemies());
            }, Duration.seconds(0.3));
            Entity entity = spawn("enemy_break", new SpawnData(x, y));
            getGameTimer().runOnceAfter(entity::removeFromWorld, Duration.seconds(1.5));
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.ONEAL_E, (f, o) -> {
            double x = o.getX();
            double y = o.getY();
            o.getComponent(OnealComponent.class).enemyDie();
            getGameTimer().runOnceAfter(() -> {
                //inc("enemy", -1);
                o.removeFromWorld();
                set("enemy", getEnemies());
            }, Duration.seconds(0.3));
            Entity entity = spawn("enemy_break", new SpawnData(x, y));
            getGameTimer().runOnceAfter(entity::removeFromWorld, Duration.seconds(1.5));
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.DORIA_E, (f, d) -> {
            double x = d.getX();
            double y = d.getY();
            d.getComponent(DoriaComponent.class).enemyDie();
            getGameTimer().runOnceAfter(() -> {
                //inc("enemy", -1);
                d.removeFromWorld();
                set("enemy", getEnemies());
            }, Duration.seconds(0.3));
            Entity entity = spawn("enemy_break", new SpawnData(x, y));
            getGameTimer().runOnceAfter(entity::removeFromWorld, Duration.seconds(1.5));
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.DAHL_E, (f, d) -> {
            double x = d.getX();
            double y = d.getY();
            d.getComponent(DahlComponent.class).enemyDie();
            getGameTimer().runOnceAfter(() -> {
                //inc("enemy", -1);
                d.removeFromWorld();
                set("enemy", getEnemies());
            }, Duration.seconds(0.3));
            Entity entity = spawn("enemy_break", new SpawnData(x, y));
            getGameTimer().runOnceAfter(entity::removeFromWorld, Duration.seconds(1.5));
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.OVAPE_E, (f, o) -> {
            double x = o.getX();
            double y = o.getY();
            o.getComponent(OvapeComponent.class).enemyDie();
            getGameTimer().runOnceAfter(() -> {
                //inc("enemy", -1);
                o.removeFromWorld();
                set("enemy", getEnemies());
            }, Duration.seconds(0.3));

            Entity entity = spawn("enemy_break", new SpawnData(x, y));
            getGameTimer().runOnceAfter(entity::removeFromWorld, Duration.seconds(1.5));
        });

        onCollisionBegin(BombermanType.FLAME, BombermanType.PASS_E, (f, pa) -> {
            double x = pa.getX();
            double y = pa.getY();
            pa.getComponent(PassComponent.class).enemyDie();
            getGameTimer().runOnceAfter(pa::removeFromWorld, Duration.seconds(0.3));

            Entity entity = spawn("enemy_break", new SpawnData(x, y));
            getGameTimer().runOnceAfter(() -> {
//                switch (randomInt()) {
//                    //inc("enemy", -1);
//                    case 1 -> spawn("balloom_enemy", new SpawnData(pa.getX(), pa.getY()));
//                    case 2 -> spawn("dahl_enemy", new SpawnData(pa.getX(), pa.getY()));
//                    case 3 -> spawn("ovape_enemy", new SpawnData(pa.getX(), pa.getY()));
//                    default -> {}
//                }
                spawn("balloom_enemy", new SpawnData(pa.getX(), pa.getY()));
                entity.removeFromWorld();
                set("enemy", getEnemies());
            }, Duration.seconds(1.5));
        });


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

    private int randomInt() {
        return (int) ((Math.random()) * 3 + 1);
    }

    private int getEnemies() {
        return getGameWorld().getGroup(BombermanType.ONEAL_E, BombermanType.PASS_E, BombermanType.BALLOOM_E,
                BombermanType.DAHL_E, BombermanType.DORIA_E, BombermanType.OVAPE_E).getSize();
    }

}
