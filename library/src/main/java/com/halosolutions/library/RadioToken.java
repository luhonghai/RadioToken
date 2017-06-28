package com.halosolutions.library;

import android.content.Context;

import com.halosolutions.library.data.AuthToken;
import com.halosolutions.library.data.RadioService;
import com.halosolutions.library.log.TimberHttpLoggingInterceptor;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import jp.radiko.k.k;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by luhonghai on 6/24/17.
 */

public class RadioToken {

    private static final int CONNECT_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;

    private final Context context;

    public RadioToken(Context context) {
        this.context = context;
    }

    public Observable<AuthToken> request(Double lat, Double lon) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(requestToken(lat, lon));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private AuthToken requestToken(Double lat, Double lon) throws Exception {
        TimberHttpLoggingInterceptor interceptor = new TimberHttpLoggingInterceptor();
        interceptor.setLevel(TimberHttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        RadioService radioService =  new Retrofit.Builder()
                                    .baseUrl(RadioService.BASE_URL)
                                    .client(client)
                                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                    //.addConverterFactory(GsonConverterFactory.create())
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .build()
                                    .create(RadioService.class);
        Call<String> auth1Call = radioService.auth1();
        Response<String> auth1Response = auth1Call.execute();
        Headers headers = auth1Response.headers();
        String authToken = headers.get("x-radiko-authtoken").trim();
        String keyOffset = headers.get("x-radiko-keyoffset").trim();
        String keyLength = headers.get("x-radiko-keylength").trim();
        Timber.d("token: " + authToken + " offset " + keyOffset + " length " + keyLength);
        if (authToken.length() > 0 && keyOffset.length() > 0 && keyLength.length() > 0) {
            String partialKey = generatePartialKey(Integer.parseInt(keyOffset), Integer.parseInt(keyLength), false);
            String location = null;
            if (lat != null && lon != null) {
                location = lat + "," + lon + ",gps";
            }
            Call<String> auth2Call = radioService.auth2(authToken, partialKey, location);
            Response<String> auth2Response = auth2Call.execute();
            String resBody = auth2Response.body().trim();
            Timber.i("Auth2 response " + resBody);
            if (auth2Response.isSuccessful()) {
                AuthToken token = new AuthToken();
                token.setRaw(resBody);
                if (resBody.startsWith("JP")) {
                    token.setAreaId(resBody.split(",")[0]);
                }
                token.setToken(authToken);
                return token;
            } else {
                throw new Exception("Could not verify token at auth2");
            }
        } else {
            throw new Exception("No auth token found at auth1");
        }
    }

    private String generatePartialKey(int offset, int length, boolean useNewVersion) throws Exception {
        if (useNewVersion) {
            return new String(Base64.encodeBase64(k.getKeyNative2(new byte[length], offset, length)), "UTF-8");
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getResources().openRawResource(R.raw.xbin);
                byte[] data = IOUtils.toByteArray(inputStream);
                data = k.getKeyNative(context, data);
                if (offset + length > data.length) {
                    throw new Exception("Invalid offset and length");
                }
                byte[] bytes = new byte[length];
                System.arraycopy(data, offset, bytes, 0, length);
                return new String(Base64.encodeBase64(bytes), "UTF-8");
            } finally {
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                    }
            }
        }
    }
}
