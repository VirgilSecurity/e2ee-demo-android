package com.android.virgilsecurity.jwtworkexample.data.remote;

import com.android.virgilsecurity.jwtworkexample.data.model.TokenResponse;
import com.android.virgilsecurity.jwtworkexample.data.model.User;
import com.android.virgilsecurity.jwtworkexample.data.model.UsersRepsonse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Danylo Oliinyk on 3/23/18 at Virgil Security.
 * -__o
 */

public class ServiceHelper {

    private JwtExampleService service;

    public ServiceHelper(Retrofit retrofit) {
        this.service = retrofit.create(JwtExampleService.class);
    }

    public Call<TokenResponse> getToken(String googleToken) {
        return service.getToken(googleToken);
    }
}
