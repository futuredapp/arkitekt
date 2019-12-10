package com.thefuntasty.mvvm.rxusecases.disposables

import androidx.annotation.VisibleForTesting
import com.thefuntasty.mvvm.rxusecases.usecases.CompletableUseCase
import com.thefuntasty.mvvm.rxusecases.usecases.FlowableUseCase
import com.thefuntasty.mvvm.rxusecases.usecases.MaybeUseCase
import com.thefuntasty.mvvm.rxusecases.usecases.ObservableUseCase
import com.thefuntasty.mvvm.rxusecases.usecases.SingleUseCase
import io.reactivex.disposables.CompositeDisposable

/**
 * This interface gives your class ability to execute [FlowableUseCase], [SingleUseCase],
 * [CompletableUseCase], [ObservableUseCase], [MaybeUseCase] use cases and automatically add
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
