package com.fsmflying.common.thread.pool;

import java.util.concurrent.ForkJoinTask;

public class Startup {


    public static class ForkTaskTest01<T> extends ForkJoinTask<T> {

        private T[] array;
        private int start;
        private int end;

        public ForkTaskTest01(T[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;

        }

        @Override
        public T getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(T value) {

        }

        @Override
        protected boolean exec() {
            return false;
        }
    }

    public static void main(String[] args) {

    }
}
