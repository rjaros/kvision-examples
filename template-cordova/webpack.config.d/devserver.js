if (defined.PRODUCTION === false || defined.PRODUCTION === 'false') {
    const path = require('path');

    config.devServer = {
        watchOptions: {
            aggregateTimeout: 5000,
            poll: 500
        },
        contentBase: [path.join(__dirname, "../src/main/web"), path.join(__dirname, "../platforms/android/platform_www")],
        host: "10.0.2.2"
    }
}
