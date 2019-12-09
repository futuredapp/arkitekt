package com.thefuntasty.mvvm.rxusecases.base;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulerRule implements TestRule {

    private Scheduler SCHEDULER_INSTANCE = Schedulers.trampoline();

    private Function<Scheduler, Scheduler> schedulerFunction = scheduler -> SCHEDULER_INSTANCE;

    private Function<Callable<Scheduler>, Scheduler> schedulerFunctionLazy = schedulerCallable -> SCHEDULER_INSTANCE;

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.reset();
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerFunctionLazy);

                RxJavaPlugins.reset();
                RxJavaPlugins.setIoSchedulerHandler(schedulerFunction);
                RxJavaPlugins.setNewThreadSchedulerHandler(schedulerFunction);
                RxJavaPlugins.setComputationSchedulerHandler(schedulerFunction);

                base.evaluate();

                RxAndroidPlugins.reset();
                RxJavaPlugins.reset();
            }
        };
    }
}
