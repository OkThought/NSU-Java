package bogush;

abstract class TimeFilter implements Filter {
    long timeBound;
    TimeFilter (long bound) {
        timeBound = bound;
    }
}
