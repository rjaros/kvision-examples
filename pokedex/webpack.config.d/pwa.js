const {GenerateSW} = require('workbox-webpack-plugin');
const {addTemplatedURLs} = require("ur-workbox-utils");

config.plugins.push(new GenerateSW({
    cacheId: 'pokedex',
    manifestTransforms: [
            addTemplatedURLs({
                "index.html": ["../../../../src/main/web/index.html"]
            })
        ]
}));
