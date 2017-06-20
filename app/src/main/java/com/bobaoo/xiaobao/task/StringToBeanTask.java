package com.bobaoo.xiaobao.task;

import android.os.AsyncTask;

import com.google.gson.Gson;

/**
 * Created by star on 15/6/5.
 * <p/>
 * 根据string返回bean
 */
public class StringToBeanTask<T> extends AsyncTask<String, Integer, T> {
    private Class<T> mClassType;
    private ConvertListener<T> mListener;

    public interface ConvertListener<T> {
        void onConvertSuccess(T response);

        void onConvertFailed();
    }

    public StringToBeanTask(Class<T> classType, ConvertListener<T> listener) {
        mClassType = classType;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected T doInBackground(String... params) {
        T t = null;
        if (params.length > 0) {
            String jsonString = params[0];
            try {
                Gson gson = new Gson();
                t = gson.fromJson(jsonString, mClassType);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return t;
    }

    @Override
    protected void onPostExecute(T t) {
        if (t == null) {
            mListener.onConvertFailed();
        } else {
            mListener.onConvertSuccess(t);
        }
    }
}
