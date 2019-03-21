var gulp = require('gulp');
var del = require('del');
var nconf = require('nconf');
var browserify = require('gulp-browserify');
var sass = require('gulp-sass');
var Q = require('q');

var argv = require('yargs').argv;

var workingPath = argv.basedir;
console.log("basedir: " + workingPath);

nconf.use('file', { file: workingPath + '/config.json' });

gulp.task('clean',  function() {
	return del(jsDst);
});

gulp.task('js', function() {
	var pages = nconf.get('js-pages');
	var promises = [];
	var jsSrc = workingPath + '/../src/javascript';
	var jsDst = workingPath + '/../build/war-template/javascript'
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

gulp.task('css', function() {
	var pages = nconf.get('css-pages');
	var promises = [];
	var srcDir = workingPath + '/../src/sass';
	var dstDir = workingPath + '/../build/war-template/css';
	pages.forEach((p) => {
		var srcFile = srcDir + '/' + p.name + '.scss';
		console.log({ name: p.name, src: srcFile, dst: dstDir });
		var promise = gulp.src(srcFile)
			.pipe(sass({ file: srcFile }).on('error', sass.logError))
			.pipe(gulp.dest(dstDir));
		promises.push(promise);
	});
	return Q.all(promises);
});