package com.thefuntasty.mvvm.usecases.disposables

import com.thefuntasty.mvvm.usecases.base.RxMockitoJUnitRunner
import com.thefuntasty.mvvm.usecases.base.BaseObservabler
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

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

    @Test
    fun `onStart should be called before subscription for execute methods`() {
        val testObservabler = object : BaseObservabler<Unit, String>() {
            override fun prepare(args: Unit) = Observable.just("first", "second")
        }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            testObservabler.execute(Unit) {
                onStart { events.add("start") }
                onNext { events.add(it) }
                onComplete { events.add("complete") }
            }
        }

        assertEquals(4, events.size)
        assertEquals("start", events[0])
        assertEquals("first", events[1])
        assertEquals("second", events[2])
        assertEquals("complete", events[3])
    }

    @Test
    fun `onStart should be called before subscription for execute stream method`() {
        val testObservable = Observable.just("first", "second")
        val events = mutableListOf<String>()

        withDisposablesOwner {
            testObservable.executeStream {
                onStart { events.add("start") }
                onNext { events.add(it) }
                onComplete { events.add("complete") }
            }
        }

        assertEquals(4, events.size)
        assertEquals("start", events[0])
        assertEquals("first", events[1])
        assertEquals("second", events[2])
        assertEquals("complete", events[3])
    }
}
