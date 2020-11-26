config.resolve.modules.push("../../processedResources/frontend/main");

if (config.devServer) {
    config.devServer.hot = true;
    config.devtool = 'eval-cheap-source-map';
}
