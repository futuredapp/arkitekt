import SwiftUI
import shared

@available(iOS 14.0, *)
struct ContentView: View {
    @StateObject private var viewModel = LaunchesViewModel()
    
    var body: some View {
        VStack {
            HStack {
                Button(action: {
                    viewModel.onStart()
                }) {
                    Text("Start")
                }
                Button(action: {
                    viewModel.onStop()
                }) {
                    Text("Stop")
                }
            }
            List {
                ForEach(viewModel.launches, id: \.id) { launch in
                    Text(launch.mission.name)
                }
            }
            List {
                ForEach(viewModel.devices, id: \.name) { device in
                    Text(device.name)
                }
            }
        }
    }
}

@available(iOS 14.0, *)
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
