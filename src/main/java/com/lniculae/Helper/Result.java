package com.lniculae.Helper;

public class Result<T> {
    T some;
    Error err;
    boolean ok;

    public Result(T some) {
        this.some = some;
        ok = true;
    }

    public Result(Error err) {
        this.err = err;
        ok = false;
    }

    public static class Empty {}
    
    public static Result<Empty> None = new Result<>(new Empty());

    public boolean Ok() {
        return ok;
    }

    public T Some() {
        return some;
    }
    
    public Error Err() {
        return err;
    }
}
