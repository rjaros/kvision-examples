const {GenerateSW} = require('workbox-webpack-plugin');
const {addTemplatedURLs} = require("ur-workbox-utils");

config.plugins.push(new GenerateSW({
    cacheId: 'pokedex',
    maximumFileSizeToCacheInBytes: 10 * 1024 * 1024,
    manifestTransforms: [
            addTemplatedURLs({
                "index.html": ["../../../../src/main/web/index.html"]
            })
        ]
}));
