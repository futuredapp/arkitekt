package com.thefuntasty.interactors

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseInteractor<T : Any> {

    protected open fun getWorkScheduler() = Schedulers.io()

    protected open fun getResultScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
