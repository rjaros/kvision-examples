config.resolve.modules.push("../../processedResources/Js/main");
if (!config.devServer && config.output) {
    config.devtool = false
    config.output.filename = "main.bundle.js"
}
if (config.devServer) {
    config.devServer.watchOptions = {
        aggregateTimeout: 1000,
        poll: 500
    };
    config.devServer.stats = {
        warnings: false
    };
    config.devServer.clientLogLevel = 'error';
}
