package com.eldarian.translator.api;

import android.util.Log;

import com.eldarian.translator.app.AppData;
import com.eldarian.translator.database.TranslateBase;

import io.reactivex.functions.Consumer;
import io.reactivex.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YandexTranslateUseCase implements TranslateUseCase {

    private Retrofit retrofit;
    private TranslateApi translateApi;

    public YandexTranslateUseCase(){
        retrofit = new Retrofit.Builder()
                .baseUrl(AppData.API_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        translateApi = retrofit.create(TranslateApi.class);
    }

    @Override
    public void translate(Consumer<TranslateResponse> consumerResponse, Consumer<TranslateBase> consumerBase, final String lang, final String text) {

        Call<TranslateResponse> translateResponseCall = translateApi.getTranslate(lang, text);

        translateResponseCall.enqueue(new Callback<TranslateResponse>() {

            @Override
            public void onResponse(Call<TranslateResponse> call, Response<TranslateResponse> response) {

                TranslateResponse translateResponse = response.body();
                if(translateResponse != null) {

                    TranslateBase translateBase = new TranslateBase();
                    translateBase.lang = lang;
                    translateBase.textIn = text;
                    translateBase.textOut = translateResponse.text[0];

                    Observable<TranslateResponse> translateResponseObservable = Observable.just(translateResponse);
                    translateResponseObservable.subscribe(consumerResponse).dispose();

                    Observable<TranslateBase> translateBaseObservable = Observable.just(translateBase);
                    translateBaseObservable.subscribe(consumerBase).dispose();

                } else {
                    Log.d("EldarianLog", response.toString());
                }
            }

            @Override
            public void onFailure(Call<TranslateResponse> call, Throwable t) {
                Log.d("EldarianLog", "failture " + t);
            }
        });

    }

}