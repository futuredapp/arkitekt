package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.crinteractors.BaseUseCase
import com.thefuntasty.mvvmsample.data.store.FormStore
import javax.inject.Inject

class SaveFormUseCase @Inject constructor(
    private val formStore: FormStore
) : BaseUseCase<SaveFormUseCase.Data, Pair<String, String>>() {

    data class Data(val form: Pair<String, String>)

    override suspend fun build(args: Data): Pair<String, String> {
        formStore.saveForm(args.form)
        return args.form
    }
}
