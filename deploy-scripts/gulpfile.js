var gulp = require('gulp');
var del = require('del');
var nconf = require('nconf');
var browserify = require('gulp-browserify');
var Q = require('q');

var argv = require('yargs').argv;

var workingPath = argv.basedir;
var jsSrc = workingPath + '/../WebContent/javascript';
var jsDst = workingPath + '/../WebContent/js';


nconf.use('file', { file: workingPath + '/config.json' });

gulp.task('clean',  function() {
	return del(jsDst);
});

gulp.task('js', function() {
	var pages = nconf.get('pages');
	console.log('pages: ' + pages);
	var promises = [];
	pages.forEach((p) => {
		var srcFile = jsSrc + '/' + p.name + '.js';
		console.log({ name: p.name, src: srcFile, dst: jsDst });
		var promise = gulp.src(srcFile)
			.pipe(browserify({
				insertGlobals: true,
				debug: true
			}))
			.pipe(gulp.dest(jsDst));
		promises.push(promise);
	});

	return Q.all(promises);
});