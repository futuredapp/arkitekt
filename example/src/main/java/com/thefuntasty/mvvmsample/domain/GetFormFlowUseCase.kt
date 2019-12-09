package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.crusecases.FlowUseCase
import com.thefuntasty.mvvmsample.data.store.FormStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFormFlowUseCase @Inject constructor(
    private val formStore: FormStore
) : FlowUseCase<Unit, Pair<String, String>>() {
    override suspend fun build(args: Unit): Flow<Pair<String, String>> = formStore.getFormFlow()
}
