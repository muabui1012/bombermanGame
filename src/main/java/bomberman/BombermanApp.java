package bomberman;



import bomberman.menus.BombermanGameMenu;
import bomberman.menus.BombermanMenu;
import bomberman.ui.BombermanHUD;
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


import static bomberman.Constants.SIZE_BLOCK;
import static com.almasb.fxgl.dsl.FXGL.*;
import bomberman.components.PlayerComponent;
import bomberman.components.BombComponent;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;


/**
 * @author nghia
 */

public class BombermanApp extends GameApplication {

    public static final int TILE_SIZE = 48;

    public static final int ENEMY_COUNT = 6;

    private static final int SPEED = 2;

    private static final int WIDTH = 17 * TILE_SIZE;

    private static final int HEIGHT = 15 * TILE_SIZE;

    private static final String TITLE = "BOMBERMAN";

    private static final String VERSION = "1.0";

    public static boolean isSoundEnabled = true;

    private static final int lives = 2;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle(TITLE);
        settings.setVersion(VERSION);
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setIntroEnabled(false);
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new BombermanMenu();
            }

            @NotNull
            @Override
            public FXGLMenu newGameMenu() {
                return new BombermanGameMenu();
            }
        });

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

        FXGL.getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
                getPlayer().getComponent(PlayerComponent.class).placeBomb();
            }
        }, KeyCode.SPACE);

    }


    protected void initAssets() {

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("speed", SPEED);
        vars.put("lives", 1);
        vars.put("time", 300);
        vars.put("score", 0);
        vars.put("enemy", ENEMY_COUNT);
        vars.put("level", 1);
        vars.put("bomb", 1);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new BombermanFactory());

        getInput().setProcessInput(true);



        loadLevel();

        Entity background = spawn("BG");

        CountDown();

        getWorldProperties().<Integer>addListener("time", this::timeUp);

    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physics = getPhysicsWorld();
        physics.setGravity(0, 0);

        onCollisionBegin(BombermanType.PLAYER, BombermanType.FLAME, (p, f) -> onPlayerKilled());
        onCollisionBegin(BombermanType.PLAYER, BombermanType.BALLOOM_E, (p, b) -> onPlayerKilled());
        onCollisionBegin(BombermanType.PLAYER, BombermanType.DAHL_E, (p, dh) -> onPlayerKilled());
        onCollisionBegin(BombermanType.PLAYER, BombermanType.ONEAL_E, (p, o) -> onPlayerKilled());
        onCollisionBegin(BombermanType.PLAYER, BombermanType.DORIA_E, (p, d) -> onPlayerKilled());
        onCollisionBegin(BombermanType.PLAYER, BombermanType.OVAPE_E, (p, o) -> onPlayerKilled());
        onCollisionBegin(BombermanType.PLAYER, BombermanType.PASS_E, (p, pa) -> onPlayerKilled());
    }

    @Override
    protected void initUI() {
        var hud = new BombermanHUD();
        var leftMargin = 0;
        var topMargin = 0;
        getGameTimer().runOnceAfter(() -> FXGL.addUINode(hud.getHUD(), leftMargin, topMargin), Duration.seconds(3));

    }

    protected void onUpdate(double tpf) {
        if (geti("enemy") <= 0){
            showMessage("You WIN!!!", () -> getGameController().gotoMainMenu());
        }

    }


    private static Entity getPlayer() {
        return FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);
    }

    public void loadLevel() {
        setLevelFromMap("bbm_new.tmx");
        setGrid();

    }

    public void onPlayerKilled() {
        // lives--
        if (geti("lives") > 0)
            inc("lives", -1);

        if (geti("lives") <= 0) {
            showMessage("Game over!!", () -> getGameController().gotoMainMenu());
        }

        // score = 0



    }



    private void setGrid() {
        AStarGrid grid = AStarGrid.fromWorld(getGameWorld(), 31, 15, TILE_SIZE, TILE_SIZE,
                (type) -> {
                    if (type == BombermanType.WALL || type == BombermanType.SURROUND || type == BombermanType.BOMB
                            || type == BombermanType.BRICK || type == BombermanType.WALL_BOMB
                    ) {
                        return CellState.NOT_WALKABLE;
                    } else {
                        return CellState.WALKABLE;
                    }
                });
        set("grid", grid);
        AStarGrid _grid = AStarGrid.fromWorld(getGameWorld(), 31, 15,
                SIZE_BLOCK, SIZE_BLOCK, (type) -> {
                    if (type == BombermanType.SURROUND || type == BombermanType.WALL) {
                        return CellState.NOT_WALKABLE;
                    } else {
                        return CellState.WALKABLE;
                    }
                });


        set("_grid", _grid);


    }

    private void CountDown() {
        run(() -> inc("time", -1), Duration.seconds(1));
    }


    private void timeUp(int backup, int now){
        if (now == 0) {
            onPlayerKilled();
        }
    }

    public static void main(String[] args) {
        launch((args));
    }


}
