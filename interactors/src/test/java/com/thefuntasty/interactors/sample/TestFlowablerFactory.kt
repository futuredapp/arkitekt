package com.thefuntasty.interactors.sample

import com.thefuntasty.interactors.BaseFlowabler
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class TestFlowablerFactory {
    companion object {
        public val twoEmits = object : BaseFlowabler<String>() {
            override fun create(): Flowable<String> {
                return Flowable.interval(1, TimeUnit.SECONDS)
                    .map { it.toString() }
                    .take(2)
            }
        }

        public val neverCompletes = object : BaseFlowabler<String>() {
            override fun create(): Flowable<String> {
                return Flowable.never()
            }
        }
    }
}
