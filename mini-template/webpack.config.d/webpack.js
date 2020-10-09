config.resolve.modules.push("../../processedResources/js/main");

if (config.devServer) {
    config.devServer.hot = true;
    config.devServer.stats = {
        warnings: false
    };
    config.devServer.clientLogLevel = 'error';
}
