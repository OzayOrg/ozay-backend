/**
 * Load required modules.
 */
var gulp = require('gulp'),
    config = require('./config.json'),
    download = require('gulp-download'),
    del = require('del');

/**
 * Default task for gulp.
 */
gulp.task('default', ['fetch']);

/**
 * Empty javascript and css directories.
 */
gulp.task('clean',['clean:js', 'clean:css']);

/**
 * Empty javascript directory.
 */
gulp.task('clean:js', function() {
    return del(config.scripts_path);
});

/**
 * Empty css directory.
 */
gulp.task('clean:css', function() {
    return del(config.styles_path);
});

/**
 * Fetch javascript and css files combined.
 */
gulp.task('fetch', ['fetch:js', 'fetch:css']);

/**
 * Fetch on javascript files.
 */
gulp.task('fetch:js', function() {
    config.scripts_url = config.scripts_url.replace('{version}', config.version);

    for(var i = 0; i < config.scripts.length; i++){
        var path = config.scripts[i].split('/');
        var scripts_path = path.length > 1 ? config.scripts_path + '/' + path[0] : config.scripts_path;
        download(config.scripts_url + config.scripts[i])
            .pipe(gulp.dest(scripts_path));
    }

});

/**
 * Fetch only css files.
 */
gulp.task('fetch:css', function() {
    config.styles_url = config.styles_url.replace('{version}', config.version);

    for(var i = 0; i < config.styles.length; i++){
        var path = config.styles[i].split('/');
        var styles_path = path.length > 1 ? config.styles_path + '/' + path[0] : config.styles_path;
        download(config.styles_url + config.styles[i])
            .pipe(gulp.dest(styles_path));
    }
});