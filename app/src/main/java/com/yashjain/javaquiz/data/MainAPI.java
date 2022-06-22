package com.yashjain.javaquiz.data;

import android.app.Application;
import android.content.Context;

import com.yashjain.javaquiz.model.QuestionsList;

import java.text.Collator;
import java.util.stream.Collector;

import io.requestly.android.core.Requestly;
import io.requestly.android.okhttp.api.RQCollector;
import io.requestly.android.okhttp.api.RQInterceptor;
import io.requestly.android.okhttp.api.RetentionManager;
import kotlin.LateinitKt;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainAPI {
    private static final String url="https://java-quiz-sample.herokuapp.com/";
    private static  RQCollector collector=null;
    private static RQInterceptor rqInterceptor=null;
    private static OkHttpClient client=null;
    public static  getService Service=null;

    public static getService get(Context context){
        collector = new RQCollector(context,"1",true);
        rqInterceptor = new RQInterceptor.Builder(context)
                .collector(collector)
                .build();

        client = new OkHttpClient.Builder()
                .addInterceptor(rqInterceptor)
                .build();
        if(Service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Service=retrofit.create(getService.class);
        }
        return Service;
    }

    public interface getService{
        @GET(url)
        Call<QuestionsList> getQuestionsList();

    }

}
