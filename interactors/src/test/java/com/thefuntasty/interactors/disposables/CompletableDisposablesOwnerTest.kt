package com.thefuntasty.interactors.disposables

import com.thefuntasty.interactors.base.RxMockitoJUnitRunner
import com.thefuntasty.interactors.interactors.BaseCompletabler
import com.thefuntasty.interactors.interactors.BaseFlowabler
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CompletableDisposablesOwnerTest : RxMockitoJUnitRunner() {

    @Test
    fun `test interface can be called properly`() {
        val tempCompletabler = object : BaseCompletabler<Unit>() {
            override fun prepare(args: Unit) = Completable.fromCallable { }
        }

        withDisposablesOwner {
            tempCompletabler.execute { }
        }

        withDisposablesOwner {
            tempCompletabler.execute(Unit) {
                onComplete { }
                onError { }
            }
        }

        withDisposablesOwner {
            tempCompletabler.create(Unit).subscribe()
        }
    }

    @Test
    fun `complete handled properly`() {
        val tempCompletabler = object : BaseCompletabler<String>() {
            override fun prepare(args: String) = Completable.fromCallable { args }
        }
        var capturedString = ""

        withDisposablesOwner {
            tempCompletabler.execute("Hello") {
                onComplete { capturedString = "captured" }
            }
        }

        assertEquals(capturedString, "captured")
    }

    @Test
    fun `error handled properly`() {
        val tempException = RuntimeException("test exc")
        val tempCompletabler = object : BaseCompletabler<Unit>() {
            override fun prepare(args: Unit) = Completable.error(tempException)
        }
        var capturedException = Throwable()

        withDisposablesOwner {
            tempCompletabler.execute(Unit) {
                onError { capturedException = it }
            }
        }

        assertEquals(capturedException, tempException)
    }

    @Test
    fun `previous run disposable should be disposed`() {
        val tempCompletabler = object : BaseCompletabler<String>() {
            override fun prepare(args: String) = Completable.never()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (CompletableDisposablesOwner.() -> Unit) = {
            disposablesList.add(tempCompletabler.execute("Hello") {})
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
        val tempCompletabler = object : BaseCompletabler<String>() {
            override fun prepare(args: String) = Completable.never()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (CompletableDisposablesOwner.() -> Unit) = {
            val singler = tempCompletabler.execute("Hello") {
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
        val tempCompletabler = object : BaseCompletabler<Unit>() {
            override fun prepare(args: Unit) = Completable.complete()
        }

        var capturedString = ""

        withDisposablesOwner {
            tempCompletabler.execute {
                onComplete {
                    capturedString = "Hello"
                }
            }
        }

        assertEquals(capturedString, "Hello")
    }
}
