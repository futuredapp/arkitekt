package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.crinteractors.BaseCoroutineInteractor
import com.thefuntasty.mvvmsample.data.store.FormStore
import javax.inject.Inject

class SaveFormInteractor @Inject constructor(
    private val formStore: FormStore
): BaseCoroutineInteractor<Pair<String, String>>() {

    private lateinit var form: Pair<String, String>

    fun init(form: Pair<String, String>) = apply {
        this.form = form
    }

    override suspend fun build(): Pair<String, String> {
        formStore.saveForm(form)
        return form
    }
}
