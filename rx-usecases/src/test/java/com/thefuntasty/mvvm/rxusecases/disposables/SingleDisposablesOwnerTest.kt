package com.thefuntasty.mvvm.rxusecases.disposables

import com.thefuntasty.mvvm.rxusecases.base.BaseSingler
import com.thefuntasty.mvvm.rxusecases.base.RxMockitoJUnitRunner
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SingleDisposablesOwnerTest : RxMockitoJUnitRunner() {

    @Test
    fun `test interface can be called properly`() {
        val tempSingler = object : BaseSingler<String, String>() {
            override fun prepare(args: String) = Single.just(args)
        }

        withDisposablesOwner {
            tempSingler.execute("Hello")
        }

        withDisposablesOwner {
            tempSingler.execute("Hello") {
                onSuccess { }
                onError { }
            }
        }

        withDisposablesOwner {
            tempSingler.create("").subscribe()
        }
    }

    @Test
    fun `success handled properly`() {
        val tempSingler = object : BaseSingler<String, String>() {
            override fun prepare(args: String) = Single.just(args)
        }
        var capturedString = ""

        withDisposablesOwner {
            tempSingler.execute("Hello") {
                onSuccess { capturedString = it }
            }
        }

        assertEquals(capturedString, "Hello")
    }

    @Test
    fun `error handled properly`() {
        val tempException = RuntimeException("test exc")
        val tempSingler = object : BaseSingler<Unit, Unit>() {
            override fun prepare(args: Unit) = Single.error<Unit>(tempException)
        }
        var capturedException = Throwable()

        withDisposablesOwner {
            tempSingler.execute(Unit) {
                onError { capturedException = it }
            }
        }

        assertEquals(capturedException, tempException)
    }

    @Test
    fun `previous run disposable should be disposed`() {
        val tempSingler = object : BaseSingler<String, String>() {
            override fun prepare(args: String) = Single.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (SingleDisposablesOwner.() -> Unit) = {
            disposablesList.add(tempSingler.execute("Hello"))
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
        val tempSingler = object : BaseSingler<String, String>() {
            override fun prepare(args: String) = Single.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (SingleDisposablesOwner.() -> Unit) = {
            val singler = tempSingler.execute("Hello") {
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
        val testSingler = object : BaseSingler<Unit, String>() {
            override fun prepare(args: Unit) = Single.fromCallable { "result" }
        }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            testSingler.execute(Unit) {
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
        val testSingle = Single.fromCallable { "result" }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            testSingle.executeStream {
                onStart { events.add("start") }
                onSuccess { events.add(it) }
            }
        }

        assertEquals(2, events.size)
        assertEquals("start", events[0])
        assertEquals("result", events[1])
    }
}
