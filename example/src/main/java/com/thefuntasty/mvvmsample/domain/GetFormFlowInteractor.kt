package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.crinteractors.BaseFlowInteractor
import com.thefuntasty.mvvmsample.data.store.FormStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFormFlowInteractor @Inject constructor(
    private val formStore: FormStore
) : BaseFlowInteractor<Pair<String, String>>() {
    override suspend fun build(): Flow<Pair<String, String>> = formStore.getFormFlow()
}
