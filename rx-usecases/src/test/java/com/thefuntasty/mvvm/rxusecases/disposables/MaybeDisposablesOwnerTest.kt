package com.thefuntasty.mvvm.rxusecases.disposables

import com.thefuntasty.mvvm.rxusecases.usecases.MaybeUseCase
import com.thefuntasty.mvvm.rxusecases.usecases.RxMockitoJUnitRunner
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MaybeDisposablesOwnerTest : RxMockitoJUnitRunner() {

    @Test
    fun `test interface can be called properly`() {
        val tempMayber = object : MaybeUseCase<String, String>() {
            override fun prepare(args: String) = Maybe.just(args)
        }

        withDisposablesOwner {
            tempMayber.execute("Hello") { }
        }

        withDisposablesOwner {
            tempMayber.execute("Hello") {
                onSuccess { }
                onComplete { }
                onError { }
            }
        }

        withDisposablesOwner {
            tempMayber.create("").subscribe()
        }
    }

    @Test
    fun `success handled properly`() {
        val tempMayber = object : MaybeUseCase<String, String>() {
            override fun prepare(args: String) = Maybe.just(args)
        }
        var capturedString = ""

        withDisposablesOwner {
            tempMayber.execute("Hello") {
                onSuccess { capturedString = it }
            }
        }

        assertEquals(capturedString, "Hello")
    }

    @Test
    fun `error handled properly`() {
        val tempException = RuntimeException("test exc")
        val tempMayber = object : MaybeUseCase<Unit, Unit>() {
            override fun prepare(args: Unit) = Maybe.error<Unit>(tempException)
        }
        var capturedException = Throwable()

        withDisposablesOwner {
            tempMayber.execute(Unit) {
                onError { capturedException = it }
            }
        }

        assertEquals(capturedException, tempException)
    }

    @Test
    fun `previous run disposable should be disposed`() {
        val tempMayber = object : MaybeUseCase<String, String>() {
            override fun prepare(args: String) = Maybe.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (MaybeDisposablesOwner.() -> Unit) = {
            disposablesList.add(tempMayber.execute("Hello") {})
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
        val tempMayber = object : MaybeUseCase<String, String>() {
            override fun prepare(args: String) = Maybe.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (MaybeDisposablesOwner.() -> Unit) = {
            val singler = tempMayber.execute("Hello") {
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
    fun `onStart should be called before subscription for execute method`() {
        val testMayber = object : MaybeUseCase<Unit, String>() {
            override fun prepare(args: Unit) = Maybe.fromCallable { "result" }
        }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            testMayber.execute(Unit) {
                onStart { events.add("start") }
                onSuccess { events.add(it) }
            }
        }

        assertEquals(2, events.size)
        assertEquals("start", events[0])
        assertEquals("result", events[1])
    }

    @Test
    fun `onStart should be called before subscription for executeStream method`() {
        val testMaybe = Maybe.fromCallable { "result" }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            testMaybe.executeStream {
                onStart { events.add("start") }
                onSuccess { events.add(it) }
            }
        }

        assertEquals(2, events.size)
        assertEquals("start", events[0])
        assertEquals("result", events[1])
    }

    @Test
    fun `run execute without params`() {
        val tempMayber = object : BaseMayber<Unit, String>() {
            override fun prepare(args: Unit) = Maybe.just("Hello")
        }

        var capturedString = ""

        withDisposablesOwner {
            tempMayber.execute {
                onSuccess {
                    capturedString = it
                }
            }
        }

        assertEquals(capturedString, "Hello")
    }
}
