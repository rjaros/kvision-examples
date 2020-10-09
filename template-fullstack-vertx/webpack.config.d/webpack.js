config.resolve.modules.push("../../processedResources/frontend/main");

if (config.devServer) {
    config.devServer.hot = true;
    config.devServer.stats = {
        warnings: false
    };
    config.devServer.clientLogLevel = 'error';
    config.devtool = 'eval-cheap-source-map';
}
