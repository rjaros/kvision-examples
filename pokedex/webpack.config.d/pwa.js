const {GenerateSW} = require('workbox-webpack-plugin');
config.plugins.push(new GenerateSW({
    cacheId: 'pokedex'
}));
