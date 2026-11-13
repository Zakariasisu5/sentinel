package com.sentinel.util;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.function.Function;

public final class TableSupport {
    private TableSupport() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> TableColumn<T, String> stringColumn(
            String title,
            Function<T, String> valueProvider,
            double minWidth
    ) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(valueProvider.apply(cell.getValue())));
        column.setMinWidth(minWidth);
        column.setPrefWidth(minWidth * 1.4);
        return column;
    }

    public static <T> void configure(TableView<T> table) {
        table.getStyleClass().add("data-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setFixedCellSize(42);
    }
}
