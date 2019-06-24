var fs = require('fs');
var path = require('path');

module.exports = function(ctx) {

    var configFile = path.join(ctx.opts.projectRoot, "config.xml");

    var config = fs.readFileSync(configFile, 'utf8');

    var config2 = config.replace('<content src="index.html" />','<content src="http://10.0.2.2:8088/" />');

    if (config2 != config) {
        fs.writeFileSync(configFile, config2);
    }
};
