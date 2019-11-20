package com.thefuntasty.interactors.disposables

import androidx.annotation.VisibleForTesting
import io.reactivex.disposables.CompositeDisposable

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
