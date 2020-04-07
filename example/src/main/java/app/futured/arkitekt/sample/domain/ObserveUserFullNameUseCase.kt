package app.futured.arkitekt.sample.domain

import com.thefuntasty.mvvm.rxusecases.usecases.ObservableUseCase
import app.futured.arkitekt.sample.data.store.UserStore
import io.reactivex.Observable
import javax.inject.Inject

class ObserveUserFullNameUseCase @Inject constructor(
    private val userStore: UserStore
) : ObservableUseCase<Unit, String>() {

    override fun prepare(args: Unit): Observable<String> =
        userStore.getUser().map { "${it.firstName} ${it.lastName}" }
}
