package GUI.Common;

import Interfaces.Item;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;

import java.util.List;

public abstract class Common {

	public static Border getBorder(int top, int rigth, int bottom, int left) {
		BorderStroke borderStroke = new BorderStroke(
				Color.BLACK,
				BorderStrokeStyle.SOLID,
				null, // rounded corners
				new BorderWidths(top, rigth, bottom, left) // top, right, bottom, left
		);

		Border border = new Border(borderStroke);
		return border;
	}

	public static <T extends Item> void useSpecifiedListView(ListView<T> listView) {
		// We need to show specified text in the list aka different from toSting.
		listView.setCellFactory(lv -> new ListCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setText(null); // Handle empty cells
				} else {
					// Add new info text.
					setText(item.getListInfo());
				}
			}
		});
	}

	public static String insertLfIntoSting(String s, int size) {
		StringBuilder sb = new StringBuilder();

		char[] c = s.toCharArray();

		for (int i = 0; i < c.length; i++) {
			if (i % size == 0 && i != 0) {
				if (c[i] == 32) {
					sb.append("\n");
					sb.append(c[i]);
				} else {
					sb.append('-');
					sb.append("\n");
					sb.append(c[i]);
				}
			} else {
				sb.append(c[i]);
			}
		}

		return sb.toString();
	}
}
