package com.example.hqb98.mj.callback;

import java.io.IOException;

import okhttp3.Request;

public abstract class ResultCallback {
    public abstract void onError(Request request,Exception e);
    public abstract void onResponse(String str)throws IOException;
}
