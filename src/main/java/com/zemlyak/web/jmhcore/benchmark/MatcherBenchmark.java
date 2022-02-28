package com.zemlyak.web.jmhcore.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.ThreadLocal.withInitial;

public class MatcherBenchmark {
    private static final Pattern LANG_PATTERN = Pattern.compile("^([a-z]{2}-[a-z]{2}|[a-z]{2}_[a-z]{2})$");
    private static final ThreadLocal<Matcher> LANG_MATCHER = withInitial(() -> LANG_PATTERN.matcher(""));

    @Benchmark
    @Fork(value = 1, warmups = 3)
    @BenchmarkMode(Mode.Throughput)
    public boolean benchMatcherCreation(Const constants) {
        return LANG_PATTERN.matcher(constants.lang.toLowerCase(Locale.ENGLISH)).find();
    }

    @Benchmark
    @Fork(value = 1, warmups = 3)
    @BenchmarkMode(Mode.Throughput)
    public boolean benchMatcherThreadLocalInit(Const constants) {
        return LANG_MATCHER.get().reset(constants.lang.toLowerCase(Locale.ENGLISH)).find();
    }

    @State(Scope.Benchmark)
    public static class Const {
        public String lang = "EN-en";
    }
}
