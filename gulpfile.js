const {src, dest, watch, series} = require('gulp');
const sass = require('gulp-sass')(require('sass'));
const postcss = require('gulp-postcss');
const cssnano = require('cssnano');
const terser = require('gulp-terser');
const browser_sync = require('browser-sync').create();

// -- gulp tasks
// scss task
// Inside your gulpfile.js scss task
function scssTask() {
    return gulp.src('app/scss/**/*.scss')
        .pipe(sass().on('error', sass.logError))
        // 1. Save to source static folder (for Git & clean builds)
        .pipe(gulp.dest('src/main/resources/static/dist'))
        // 2. Save directly into Spring Boot's live target folder
        .pipe(gulp.dest('target/classes/static/dist'))
        .pipe(browserSync.stream());
}

// js task
function jsTask() {
    return src('app/js/**/*.js', {sourcemaps: true})
        .pipe(terser())
        .pipe(dest('src/main/resources/static/dist', {sourcemaps: '.'}));
}

// browser sync task
function browserSyncServe(cb) {
    browser_sync.init({
        proxy: "localhost:8080", // FIXED: Napojení na tvůj běžící Spring Boot (Tomcat)
        port: 8080,
        notify: false
    });
    cb();
}

function browserSyncReload(cb) {
    browser_sync.reload();
    cb();
}

// watch task
function watchTask() {
    // Sleduje změny v Thymeleaf šablonách
    watch('src/main/resources/templates/**/*.html', browserSyncReload);
    // Sleduje tvůj frontend zdrojový kód, zkompiluje ho a obnoví prohlížeč
    watch(['app/scss/**/*.scss', 'app/js/**/*.js'], series(scssTask, jsTask, browserSyncReload));
}

// default gulp task
exports.default = series(
    scssTask,
    jsTask,
    browserSyncServe,
    watchTask
);