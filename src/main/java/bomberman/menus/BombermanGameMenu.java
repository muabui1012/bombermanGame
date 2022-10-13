package bomberman.menus;

import bomberman.BombermanApp;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.geometry.Pos;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Nghiadeptraihon
 * In game menu.
 * Press esc key to open this menu.
 */

public class BombermanGameMenu extends FXGLMenu
{
public BombermanGameMenu() {
        super(MenuType.GAME_MENU);
        Shape shape = new Rectangle(48 * 17, 48 * 15, Color.DARKBLUE);
        shape.setOpacity(0.5);
        getContentRoot().getChildren().add(shape);

        // UI background
        ImageView iv1 = new ImageView();
        iv1.setImage(new Image("assets/textures/background_demo_1.png"));
        iv1.setX(48 * 17 / 2.0 - 520 / 2.0);
        iv1.setY(100);
        iv1.setEffect(new DropShadow(5, 3.5, 3.5, Color.WHITE));
        iv1.setEffect(new Lighting());
        getContentRoot().getChildren().add(iv1);

        // UI game title
        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.WHITE, 30);
        title.setStroke(Color.WHITESMOKE);
        title.setStrokeWidth(1.5);
        title.setEffect(new Bloom(0.6));
        centerTextBind(title, getAppWidth() / 2.0, 250);


        // UI game version
        var version = getUIFactoryService().newText(getSettings().getVersion(), Color.WHITE, 20);
        centerTextBind(version, getAppWidth() / 2.0, 280);
        getContentRoot().getChildren().addAll(title, version);

        // UI Button
        var menuBox = new VBox(
        2,
        new MenuButton("Resume", this::fireResume),
        new MenuButton("Music", this::setSoundEnabled),
        new MenuButton("Main Menu", this::fireExitToMainMenu),
        new MenuButton("Exit", this::fireExit)
        );

        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateX(getAppWidth() / 2.0 - 80);
        menuBox.setTranslateY(getAppHeight() / 2.0);
        menuBox.setSpacing(20);
        getContentRoot().getChildren().addAll(menuBox);
        }

private void setSoundEnabled() {
        BombermanApp.isSoundEnabled = !BombermanApp.isSoundEnabled;
        getSettings().setGlobalMusicVolume(BombermanApp.isSoundEnabled ? 0.05 : 0.0);
        getSettings().setGlobalSoundVolume(BombermanApp.isSoundEnabled ? 0.4 : 0.0);
        }
}