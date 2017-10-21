package org.sketchfx.canvas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

import java.util.function.Consumer;

class SelectionModel<T> {

    private ObservableSet<T> selection = FXCollections.observableSet();

    SelectionModel(Consumer<T> itemRemoved, Consumer<T> itemAdded ) {

        selection.addListener( (SetChangeListener.Change<? extends T> change) -> {

            if ( change.wasRemoved() ) {
                itemRemoved.accept(change.getElementRemoved());

            }
            if ( change.wasAdded() ) {
                itemAdded.accept(change.getElementAdded());
            }

        });

    }

    public void clear() {
        selection.clear();
    }

    public void add( T element ) {
        selection.add(element);
    }

    public void add( T element, boolean preClear ) {
        if ( preClear ) {
            clear();
        }
        add(element);
    }


}
