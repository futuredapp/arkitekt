package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.interactors.BaseObservabler
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.RuntimeException

class ObservableDisposablesOwnerTest : RxMockitoJUnitRunner() {

    @Test
    fun `test interface can be called properly`() {
        val tempObservabler = object : BaseObservabler<String, String>() {
            override fun prepare(args: String) = Observable.just(args)
        }

        withDisposablesOwner {
            tempObservabler.execute("Hello")
        }

        withDisposablesOwner {
            tempObservabler.execute("Hello") {
                onNext { }
                onError { }
            }
        }

        withDisposablesOwner {
            tempObservabler executeWith ""
        }

        withDisposablesOwner {
            tempObservabler.create("").subscribe()
        }
    }

    @Test
    fun `onNext handled properly`() {
        val tempObservabler = object : BaseObservabler<String, String>() {
            override fun prepare(args: String) = Observable.just(args)
        }
        var capturedString = ""

        withDisposablesOwner {
            tempObservabler.execute("Hello") {
                onNext { capturedString = it }
            }
        }

        assertEquals(capturedString, "Hello")
    }

    @Test
    fun `check onNext successfully called multiple times`() {
        val tempObservabler = object : BaseObservabler<Unit, String>() {
            override fun prepare(args: Unit) = Observable.just("1", "2", "3")
        }

        val capturedData = StringBuffer()
        var interactorFinished = false

        withDisposablesOwner {
            tempObservabler.execute(Unit) {
                onNext { capturedData.append(it) }
                onComplete { interactorFinished = true }
            }
        }

        assertEquals("123", capturedData.toString())
        assertTrue(interactorFinished)
    }

    @Test
    fun `error handled properly`() {
        val tempException = RuntimeException("test exc")
        val tempObservabler = object : BaseObservabler<Unit, Unit>() {
            override fun prepare(args: Unit) = Observable.error<Unit>(tempException)
        }
        var capturedException = Throwable()

        withDisposablesOwner {
            tempObservabler.execute(Unit) {
                onError { capturedException = it }
            }
        }

        assertEquals(capturedException, tempException)
    }

    @Test
    fun `previous run disposable should be disposed`() {
        val tempObservabler = object : BaseObservabler<String, String>() {
            override fun prepare(args: String) = Observable.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (ObservableDisposablesOwner.() -> Unit) = {
            disposablesList.add(tempObservabler.execute("Hello"))
        }

        withDisposablesOwner {
            runExecute()
            runExecute()
        }

        assertTrue(disposablesList.size == 2)
        assertTrue(disposablesList[0].isDisposed)
        assertTrue(!disposablesList[1].isDisposed)
    }

    @Test
    fun `previous run disposable not disposed when requested`() {
        val tempObservabler = object : BaseObservabler<String, String>() {
            override fun prepare(args: String) = Observable.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (ObservableDisposablesOwner.() -> Unit) = {
            val singler = tempObservabler.execute("Hello") {
                disposePrevious(false)
            }
            disposablesList.add(singler)
        }

        withDisposablesOwner {
            runExecute()
            runExecute()
        }

        assertTrue(disposablesList.size == 2)
        assertTrue(!disposablesList[0].isDisposed)
        assertTrue(!disposablesList[1].isDisposed)
    }
}