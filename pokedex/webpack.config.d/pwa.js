const {GenerateSW} = require('workbox-webpack-plugin');
config.plugins.push(new GenerateSW({
    cacheId: 'pokedex',
    globDirectory: '../src/main/web/',
    globPatterns: ['*']
}));
