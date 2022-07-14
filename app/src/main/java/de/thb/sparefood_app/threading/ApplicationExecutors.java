package de.thb.sparefood_app.threading;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApplicationExecutors {

    private final Executor background;
    private final Executor mainThread;

    public Executor getBackground() {
        return background;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public ApplicationExecutors() {
        this.background = Executors.newSingleThreadExecutor();
        this.mainThread = new MainThreadExecutor();
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
