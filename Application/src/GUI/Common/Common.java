package GUI.Common;

import Interfaces.Item;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public abstract class Common {

    protected static ErrorWindow errorWindow = new ErrorWindow();

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

    public static StringConverter datePickerFormat(DatePicker dpcStartDate) {
            StringConverter datePickerFormat = new StringConverter<LocalDate>() {
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                {
                    dpcStartDate.setPromptText(pattern.toLowerCase());
                }

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    try {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        }
                    } catch (DateTimeParseException e) {
                        // Sæt en visuel fejlindikator, fx rød kant
                        dpcStartDate.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                    }
                    return null;
                }
            };
            return datePickerFormat;
    }
}
