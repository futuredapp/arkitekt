package app.futured.arkitekt.rxusecases.disposables

import app.futured.arkitekt.rxusecases.usecases.FlowableUseCase
import app.futured.arkitekt.rxusecases.usecases.RxMockitoJUnitRunner
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FlowableDisposablesOwnerTest : RxMockitoJUnitRunner() {

    @Test
    fun `test interface can be called properly`() {
        val tempFlowabler = object : FlowableUseCase<String, String>() {
            override fun prepare(args: String) = Flowable.just(args)
        }

        withDisposablesOwner {
            tempFlowabler.execute("Hello") { }
        }

        withDisposablesOwner {
            tempFlowabler.execute("Hello") {
                onNext { }
                onComplete { }
                onError { }
            }
        }

        withDisposablesOwner {
            tempFlowabler.create("").subscribe()
        }
    }

    @Test
    fun `onNext handled properly`() {
        val tempFlowabler = object : FlowableUseCase<String, String>() {
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
        val tempFlowabler = object : FlowableUseCase<Unit, Unit>() {
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
        val tempFlowabler = object : FlowableUseCase<String, String>() {
            override fun prepare(args: String) = Flowable.never<String>()
        }
        val disposablesList = mutableListOf<Disposable>()

        val runExecute: (FlowableDisposablesOwner.() -> Unit) = {
            disposablesList.add(tempFlowabler.execute("Hello") {})
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
        val tempFlowabler = object : FlowableUseCase<String, String>() {
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

    @Test
    fun `onStart should be called before subscription for execute methods`() {
        val tempFlowabler = object : FlowableUseCase<Unit, String>() {
            override fun prepare(args: Unit) = Flowable.just("first", "second")
        }
        val events = mutableListOf<String>()

        withDisposablesOwner {
            tempFlowabler.execute(Unit) {
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
        val tempFlowable = Flowable.just("first", "second")
        val events = mutableListOf<String>()

        withDisposablesOwner {
            tempFlowable.executeStream {
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
    fun `run execute without params`() {
        val tempFlowabler = object : FlowableUseCase<Unit, String>() {
            override fun prepare(args: Unit) = Flowable.just("Hello")
        }

        var capturedString = ""

        withDisposablesOwner {
            tempFlowabler.execute {
                onNext {
                    capturedString = it
                }
            }
        }

        assertEquals(capturedString, "Hello")
    }
}
