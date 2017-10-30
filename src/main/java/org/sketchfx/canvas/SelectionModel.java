package org.sketchfx.canvas;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.sketchfx.util.RxEventBus;

import java.util.function.Consumer;

class SelectionModel<T> {

    private ObservableSet<T> selection = FXCollections.observableSet();

    private Observable<T> additions = JavaFxObservable.additionsOf(selection);
    private Observable<T> removals = JavaFxObservable.removalsOf(selection);

    public Observable<T> getAdditions() {
        return additions;
    }

    public Observable<T> getRemovals() {
        return removals;
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
