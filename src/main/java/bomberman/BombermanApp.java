package bomberman;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;

import javafx.scene.input.KeyCode;




/**
 * @author nghia
 */
public class BombermanApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("BomberMan");
        settings.setVersion("0.1");
        settings.setWidth(800);
        settings.setHeight(800);
        settings.setIntroEnabled(false);
        settings.setMainMenuEnabled(false);

    }

    @Override
    protected void initInput() {
    }

    protected void initAssets() {

    }

    @Override
    protected void initGame() {
    }

    @Override
    protected void initPhysics() {

    }

    protected void initUI() {

    }

    protected void onUpdate(double tpf) {

    }

    public static void main(String[] args) {
        launch((args));
    }


}
