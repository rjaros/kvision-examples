;(function() {
    config.module.rules.push({
        test: /tailwind\.css$/,
        use: [ '@tailwindcss/webpack' ]
    });
})();
