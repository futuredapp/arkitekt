package com.thefuntasty.mvvmsample.domain

import com.thefuntasty.mvvm.crinteractors.BaseFlower
import com.thefuntasty.mvvmsample.data.store.FormStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFormFlowInteractor @Inject constructor(
    private val formStore: FormStore
) : BaseFlower<Unit, Pair<String, String>>() {
    override suspend fun build(args: Unit): Flow<Pair<String, String>> = formStore.getFormFlow()
}
