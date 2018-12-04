package com.thefuntasty.interactors.sample

import com.thefuntasty.interactors.BaseFlowabler
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class TestFlowablerFactory {
    companion object {
        val twoEmits = object : BaseFlowabler<String>() {
            override fun prepare(): Flowable<String> {
                return Flowable.interval(1, TimeUnit.SECONDS)
                    .map { it.toString() }
                    .take(2)
            }
        }

        val neverCompletes = object : BaseFlowabler<String>() {
            override fun prepare(): Flowable<String> {
                return Flowable.never()
            }
        }
    }
}
