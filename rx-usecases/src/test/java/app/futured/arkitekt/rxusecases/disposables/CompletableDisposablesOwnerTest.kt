package app.futured.arkitekt.rxusecases.disposables

import app.futured.arkitekt.rxusecases.usecases.CompletableUseCase
import app.futured.arkitekt.rxusecases.usecases.RxMockitoJUnitRunner
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CompletableDisposablesOwnerTest : RxMockitoJUnitRunner() {

    @Test
    fun `test interface can be called properly`() {
        val tempCompletabler = object : CompletableUseCase<Unit>() {
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
        val tempCompletabler = object : CompletableUseCase<String>() {
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
        val tempCompletabler = object : CompletableUseCase<Unit>() {
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
        val tempCompletabler = object : CompletableUseCase<String>() {
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
        val tempCompletabler = object : CompletableUseCase<String>() {
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
    fun `onStart should be called before subscription for execute method`() {
        val tempCompletabler = object : CompletableUseCase<String>() {
            override fun prepare(args: String) = Completable.fromCallable { args }
        }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            tempCompletabler.execute("Hello") {
                onStart { events.add("start") }
                onComplete { events.add("complete") }
            }
        }

        assertEquals(2, events.size)
        assertEquals("start", events[0])
        assertEquals("complete", events[1])
    }

    @Test
    fun `onStart should be called before subscription for executeStream method`() {
        val tempCompletable = Completable.fromCallable { Unit }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            tempCompletable.executeStream {
                onStart { events.add("start") }
                onComplete { events.add("complete") }
            }
        }

        assertEquals(2, events.size)
        assertEquals("start", events[0])
        assertEquals("complete", events[1])
    }

    @Test
    fun `run execute without params`() {
        val tempCompletabler = object : CompletableUseCase<Unit>() {
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
