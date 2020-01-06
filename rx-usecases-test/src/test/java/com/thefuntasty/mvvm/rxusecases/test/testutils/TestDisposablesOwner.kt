package com.thefuntasty.mvvm.rxusecases.test.testutils

import com.thefuntasty.mvvm.rxusecases.disposables.DisposablesOwner
import io.reactivex.disposables.CompositeDisposable

class TestDisposablesOwner : DisposablesOwner {

    override val disposables = CompositeDisposable()
}
