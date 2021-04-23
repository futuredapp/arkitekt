//
//  DevicesViewModel.swift
//  iosApp
//
//  Created by RD on 06.01.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared

class LaunchesViewModel: BaseViewModel, ObservableObject {

    @Published var launches = [LaunchUi]()
    @Published var devices = [Device]()

    private let observeDevicesUc = ObserveDeviceUseCase()
    private let getLaunchesUseCase = GetLaunchesUseCase()
    private let observeLaunchesUc = ObserveLaunchesUseCase()

    override init() {
        super.init()
//        self.observeLaunches()
//        self.observeDevice()
//        self.getDevice()
    }


    func onStart() {
//        self.devices = []
        self.observeDevice()
        self.launches = []
        self.observeLaunches()
    }

    func onStop() {
        self.devices = []
        self.launches = []
        observeDevicesUc.job.get()?.cancel(cause: nil)
//        onDestroy()
    }

   private func observeLaunches() {
       observeLaunchesUc.execute(self, args: nil) { builder in
           builder.onNext { wrapper in
               guard let list = wrapper?.list else {
                   return
               }
               self.launches = list
           }
           builder.onError { error in
               print(error)
           }
       }
   }

    private func observeDevice() {
        observeDevicesUc.execute(self, args: nil) {
            $0.onNext { wrapper in
                guard let list = wrapper?.list else {
                    return
                }
                self.devices = list

            }
            $0.onError { error in
                print(error)
            }
        }
    }

    private func getLaunches() {
       getLaunchesUseCase.execute(self, arg: KotlinUnit()) {
           $0.onSuccess { wrapper in
               guard let list = wrapper?.list else {
                   return
               }
               self.launches = list
           }
           $0.onError { error in
               print(error)
           }
       }
    }

    deinit {
        onDestroy()
    }
}
