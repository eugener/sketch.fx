package org.sketchfx.util;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxEventBus {

    private static RxEventBus instance = new RxEventBus();

    public static RxEventBus getInstance() {
        return instance;
    }

    private final Relay<Object> relay;

    private RxEventBus() {
        relay = PublishRelay.create().toSerialized();
    }

    public void post(Object event) {
        relay.accept(event);
    }

    public <T> Disposable receive(final Class<T> clazz, Consumer<T> consumer) {
        return receive(clazz).subscribe(consumer);
    }

    public <T> Observable<T> receive(final Class<T> clazz) {
        return receive().ofType(clazz);
    }

    public Observable<Object> receive() {
        return relay;
    }


}
