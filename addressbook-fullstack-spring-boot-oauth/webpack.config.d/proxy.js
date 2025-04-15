if (config.devServer) {
    config.devServer.proxy = [
        {
            context: ["/rpc/*", "/rpcsse/*"],
            target: 'http://localhost:8080'
        },
        {
            context: ["/login", "/logout", "/oauth2/authorization/google"],
            target: 'http://localhost:8080'
        },
        {
            context: ["/rpcws/*"],
            target: 'http://localhost:8080',
            ws: true
        }
    ]
}
