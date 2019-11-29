package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.crinteractors.BaseFlowUsecase
import com.thefuntasty.mvvmsample.data.store.FormStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFormFlowUsecase @Inject constructor(
    private val formStore: FormStore
) : BaseFlowUsecase<Unit, Pair<String, String>>() {
    override suspend fun build(args: Unit): Flow<Pair<String, String>> = formStore.getFormFlow()
}
