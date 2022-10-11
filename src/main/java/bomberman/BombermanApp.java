package bomberman;



import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;

import java.util.*;

import com.almasb.fxgl.physics.PhysicsWorld;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import static com.almasb.fxgl.dsl.FXGL.*;
import bomberman.components.PlayerComponent;


/**
 * @author nghia
 */

public class BombermanApp extends GameApplication {

    public static final int TILE_SIZE = 48;

    private static final int SPEED = 5;

    private static final int WIDTH = 31 * TILE_SIZE;

    private static final int HEIGHT = 15 * TILE_SIZE;

    private static final String TITLE = "BOMBERMAN";

    private static final String VERSION = "1.0";

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle(TITLE);
        settings.setVersion(VERSION);
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setIntroEnabled(false);
        settings.setMainMenuEnabled(false);

    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).moveRight();
            }

            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();

            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).moveLeft();
            }
            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();

            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).moveUp();
            }
            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();

            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).moveDown();
            }
            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();

            }
        }, KeyCode.S);



    }


    protected void initAssets() {

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("speed", SPEED);

    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new BombermanFactory());

        getInput().setProcessInput(true);

        FXGL.setLevelFromMap("bbm.tmx");

        setGrid();


    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physics = getPhysicsWorld();
        physics.setGravity(0, 0);
    }

    protected void initUI() {

    }

    protected void onUpdate(double tpf) {

    }


    private static Entity getPlayer() {
        return FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);
    }

    private void setGrid() {
        AStarGrid grid = AStarGrid.fromWorld(getGameWorld(), 31, 15, TILE_SIZE, TILE_SIZE,
                (type) -> {
                    if (type == BombermanType.WALL) {
                        return CellState.NOT_WALKABLE;
                    } else {
                        return CellState.WALKABLE;
                    }
                });
        set("grid", grid);



    }


    public static void main(String[] args) {
        launch((args));
    }


}
