package app.futured.arkitekt.rxusecases.disposables

import androidx.annotation.VisibleForTesting
import app.futured.arkitekt.rxusecases.usecases.CompletableUseCase
import app.futured.arkitekt.rxusecases.usecases.FlowableUseCase
import app.futured.arkitekt.rxusecases.usecases.MaybeUseCase
import app.futured.arkitekt.rxusecases.usecases.ObservableUseCase
import app.futured.arkitekt.rxusecases.usecases.SingleUseCase
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
interface DisposablesOwner :
    SingleDisposablesOwner,
    CompletableDisposablesOwner,
    ObservableDisposablesOwner,
    FlowableDisposablesOwner,
    MaybeDisposablesOwner

@VisibleForTesting
internal fun withDisposablesOwner(lambda: DisposablesOwner.() -> Unit): DisposablesOwner {
    return object : DisposablesOwner {
        override val disposables = CompositeDisposable()
    }.apply {
        lambda.invoke(this)
    }
}
