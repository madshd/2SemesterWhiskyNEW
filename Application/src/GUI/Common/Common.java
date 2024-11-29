package GUI.Common;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;

public abstract class Common {

    public static Border getBorder(int top, int rigth, int bottom, int left) {
        BorderStroke borderStroke = new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                null,                                    // rounded corners
                new BorderWidths(top, rigth, bottom, left)          // top, right, bottom, left
        );

        Border border = new Border(borderStroke);
        return border;
    }
}
