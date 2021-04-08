//
//  DevicesViewModel.swift
//  iosApp
//
//  Created by RD on 06.01.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared

class LaunchesViewModel : BaseViewModel , ObservableObject {
    
    @Published var launches = [LaunchUi]()
    
    private let observeLaunchesUc = ObserveLaunchesUseCase()
    
    override init() {
        super.init()
        self.observeLaunches()
    }
    
    private func observeLaunches() {
        observeLaunchesUc.execute(self, args: nil) { builder in
            builder.onNext { wrapper in
                guard let list = wrapper?.list else { return }
                self.launches = list
            }
            builder.onError { error in
                print(error)
            }
        }
    }
    deinit {
        onDestroy()
    }
}
