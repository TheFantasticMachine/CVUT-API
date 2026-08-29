// gulpfile.js
const gulp = require('gulp');
const sass = require('gulp-sass')(require('sass'));
const browserSync = require('browser-sync').create();

// 1. SCSS Compilation Task
function scssTask() {
    return gulp.src('app/scss/**/*.scss')
        .pipe(sass().on('error', sass.logError))
        // Output to source static resources
        .pipe(gulp.dest('src/main/resources/static/dist'))
        // Output directly to active target classes (for instant 8080 updates)
        .pipe(gulp.dest('target/classes/static/dist'))
        .pipe(browserSync.stream());
}

// 2. JavaScript Copy/Process Task
function jsTask() {
    return gulp.src('app/js/**/*.js')
        .pipe(gulp.dest('src/main/resources/static/dist'))
        .pipe(gulp.dest('target/classes/static/dist'))
        .pipe(browserSync.stream());
}

// 3. Watcher Task (Watches SCSS and JS for changes)
function watchTask() {
    gulp.watch('app/scss/**/*.scss', scssTask);
    gulp.watch('app/js/**/*.js', jsTask);
}

// Default export: builds everything once and starts watching
exports.scss = scssTask;
exports.js = jsTask;
exports.watch = watchTask;
exports.default = gulp.series(
    gulp.parallel(scssTask, jsTask),
    watchTask
);