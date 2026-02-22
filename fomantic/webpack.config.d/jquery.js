;(function () {
    const webpack = require('webpack')

    config.plugins.push(new webpack.ProvidePlugin({
        $: ["jquery", "default"],
        jQuery: ["jquery", "default"],
        "window.$": ["jquery", "default"],
        "window.jQuery": ["jquery", "default"]
    }));
})();
