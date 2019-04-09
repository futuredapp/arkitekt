package com.thefuntasty.interactors

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseInteractor<T : Any> {

    internal var currentDisposable: Disposable? = null

    /**
     * Scheduler used in subscribeOn as a work scheduler.
     */
    protected open val workScheduler = Schedulers.io()

    /**
     * Scheduler used in observeOn as a result scheduler.
     */
    protected open val resultScheduler: Scheduler = AndroidSchedulers.mainThread()
}
