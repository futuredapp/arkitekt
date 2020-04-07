package app.futured.arkitekt.rxusecases.test.testutils

import app.futured.arkitekt.rxusecases.disposables.DisposablesOwner
import io.reactivex.disposables.CompositeDisposable

class TestDisposablesOwner : DisposablesOwner {

    override val disposables = CompositeDisposable()
}
