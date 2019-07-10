module.exports = function (grunt) {
    grunt.initConfig({
        pot: {
            options: {
                text_domain: 'messages',
                dest: '../src/frontendMain/resources/i18n/',
                keywords: ['tr', 'ntr:1,2', 'gettext', 'ngettext:1,2'],
                encoding: "UTF-8"
            },
            files: {
                src: ['../src/frontendMain/kotlin/**/*.kt'],
                expand: true,
            },
        }
    });

    grunt.loadNpmTasks('grunt-pot');
};
