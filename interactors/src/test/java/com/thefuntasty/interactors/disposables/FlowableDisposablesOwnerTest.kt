package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.interactors.BaseFlowabler
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.RuntimeException

class FlowableDisposablesOwnerTest : RxMockitoJUnitRunner() {

    @Test
    fun `test interface can be called properly`() {
        val tempFlowabler = object : BaseFlowabler<String, String>() {
            override fun prepare(args: String) = Flowable.just(args)
        }

        withDisposablesOwner {
            tempFlowabler.execute("Hello")
        }

        withDisposablesOwner {
            tempFlowabler.execute("Hello") {
                onNext { }
                onComplete { }
                onError { }
            }
        }

        withDisposablesOwner {
            tempFlowabler executeWith ""
        }

        withDisposablesOwner {
            tempFlowabler.create("").subscribe()
        }
    }

    @Test
    fun `onNext handled properly`() {
        val tempFlowabler = object : BaseFlowabler<String, String>() {
            override fun prepare(args: String) = Flowable.just(args)
        }
        var capturedString = ""

        withDisposablesOwner {
            tempFlowabler.execute("Hello") {
                onNext { capturedString = it }
            }
        }

        assertEquals(capturedString, "Hello")
    }

    @Test
    fun `error handled properly`() {
        val tempException = RuntimeException("test exc")
        val tempFlowabler = object : BaseFlowabler<Unit, Unit>() {
            override fun prepare(args: Unit) = Flowable.error<Unit>(tempException)
        }
        var capturedException = Throwable()

        withDisposablesOwner {
            tempFlowabler.execute(Unit) {
                onError { capturedException = it }
            }
        }

        assertEquals(capturedException, tempException)
    }

    @Test
    fun `previous run disposable should be disposed`() {
        val tempFlowabler = object : BaseFlowabler<String, String>() {
            override fun prepare(args: String) = Flowable.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (FlowableDisposablesOwner.() -> Unit) = {
            disposablesList.add(tempFlowabler.execute("Hello"))
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
        val tempFlowabler = object : BaseFlowabler<String, String>() {
            override fun prepare(args: String) = Flowable.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (FlowableDisposablesOwner.() -> Unit) = {
            val singler = tempFlowabler.execute("Hello") {
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