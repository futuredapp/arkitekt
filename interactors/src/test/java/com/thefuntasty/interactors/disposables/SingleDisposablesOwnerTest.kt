package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.interactors.BaseSingler
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
            tempSingler.execute("Hello") { }
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
            disposablesList.add(tempSingler.execute("Hello") {})
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
    fun `run execute without params`() {
        val tempSingler = object : BaseSingler<Unit, String>() {
            override fun prepare(args: Unit) = Single.just("Hello")
        }

        var capturedString = ""

        withDisposablesOwner {
            tempSingler.execute {
                onSuccess {
                    capturedString = it
                }
            }
        }

        assertEquals(capturedString, "Hello")

    }

    @Test
    fun `run executeNoParams without params`() {
        val tempSingler = object : BaseSingler<Unit, String>() {
            override fun prepare(args: Unit) = Single.just("Hello")
        }

        var capturedString = ""

        withDisposablesOwner {
            tempSingler.executeNoArgs {
                onSuccess {
                    capturedString = it
                }
            }
        }

        assertEquals(capturedString, "Hello")
    }
}
