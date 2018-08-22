module.exports = function(grunt) {

	grunt.initConfig({
		pot : {
			options : {
				text_domain : 'messages',
				dest : 'translation/',
				keywords : [ 'tr', 'ntr:1,2', 'gettext', 'ngettext:1,2' ],
				encoding : "UTF-8"
			},
			files : {
				src : [ 'src/**/*.kt' ],
				expand : true,
			},
		},

		po2json : {
			options : {
				format : 'jed1.x'
			},
			all : {
				src : [ 'translation/*.po' ],
				dest : 'build/web/js/'
			}
		}
	});

	grunt.loadNpmTasks('grunt-pot');
	grunt.loadNpmTasks('grunt-po2json-remi');
};
