import SwiftUI
import shared

@available(iOS 14.0, *)
struct ContentView: View {
    @StateObject private var viewModel = LaunchesViewModel()
    
    var body: some View {
        List {
            ForEach(viewModel.launches, id: \.id) { launch in
                Text(launch.mission.name)
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
