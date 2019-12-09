package com.thefuntasty.mvvm.usecases.disposables

import androidx.annotation.VisibleForTesting
import com.thefuntasty.mvvm.usecases.base.BaseCompletabler
import com.thefuntasty.mvvm.usecases.base.BaseFlowabler
import com.thefuntasty.mvvm.usecases.base.BaseMayber
import com.thefuntasty.mvvm.usecases.base.BaseObservabler
import com.thefuntasty.mvvm.usecases.base.BaseSingler
import io.reactivex.disposables.CompositeDisposable

/**
 * This interface gives your class ability to execute [BaseFlowabler], [BaseSingler],
 * [BaseCompletabler], [BaseObservabler], [BaseMayber] interactors and automatically add
 * resulting disposables to one composite disposable. You may find handy to implement
 * this interface in custom Presenters, ViewHolders etc.
 *
 * It is your responsibility to clear this composite disposable when all
 * running tasks should be stopped.
 */
interface DisposablesOwner : SingleDisposablesOwner, CompletableDisposablesOwner,
    ObservableDisposablesOwner, FlowableDisposablesOwner, MaybeDisposablesOwner

@VisibleForTesting
internal fun withDisposablesOwner(lambda: DisposablesOwner.() -> Unit): DisposablesOwner {
    return object : DisposablesOwner {
        override val disposables = CompositeDisposable()
    }.apply {
        lambda.invoke(this)
    }
}
