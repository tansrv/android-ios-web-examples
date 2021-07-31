import SwiftUI

struct Status: Codable {
    var status: Int
}

struct ContentView: View {

    @State var username: String = ""
    @State var password: String = ""
    @State var message: String  = ""
    @State var showingAlert     = false

    /* your host  */
    let tan = Tanserver(host: "tanserver.org", port: 2579)
    
    var body: some View {

        VStack {
            TextField("Enter your username", text: $username)
                .textFieldStyle(RoundedBorderTextFieldStyle())

            TextField("Enter your password", text: $password)
                .textFieldStyle(RoundedBorderTextFieldStyle())

            HStack {
                Button(action: {
                    let jsonObject: [String: Any] = [
                        "username": username,
                        "password": password
                    ]

                    let jsonData = try! JSONSerialization.data(withJSONObject: jsonObject, options: [])

                    tan.getJSON(userApi: "login",
                                jsonString: String(data: jsonData, encoding: .utf8)!,
                                completion: { data, err in

                        if err != nil {
                            return
                        }

                        let decoder = JSONDecoder()

                        let res = try! decoder.decode(Status.self, from: data!)

                        if res.status == 0 {
                            message = "Hi, \(username)"
                        } else {
                            message = "User could not be found."
                        }
                    })
                    
                    showingAlert = true
                }) {
                    Text("Log in")
                }
                .padding()
                .alert(isPresented: $showingAlert) {
                    Alert(title: Text(message))
                }

                Button(action: {
                    let jsonObject: [String: Any] = [
                        "username": username,
                        "password": password
                    ]

                    let jsonData = try! JSONSerialization.data(withJSONObject: jsonObject, options: [])

                    tan.getJSON(userApi: "register",
                                jsonString: String(data: jsonData, encoding: .utf8)!,
                                completion: { data, err in

                        if err != nil {
                            return
                        }

                        let decoder = JSONDecoder()

                        let res = try! decoder.decode(Status.self, from: data!)

                        if res.status == 0 {
                            message = "Registration success! Now you can log in."
                        } else {
                            message = "This username already exists."
                        }
                    })

                    showingAlert = true
                }) {
                    Text("Register")
                }
                .padding()
                .alert(isPresented: $showingAlert) {
                    Alert(title: Text(message))
                }
            }
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {

    static var previews: some View {
        ContentView()
    }
}
