package com.example.manumaheshwari.vigo;

/**
 * Created by ManuMaheshwari on 30/06/15.
 */



        import android.util.Log;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.util.ArrayList;
        import java.util.List;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.client.utils.URLEncodedUtils;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.protocol.HTTP;
        import org.apache.http.util.EntityUtils;

public class ServiceHandler {


    public final static int GET = 1;
    public final static int POST = 2;

    StringBuilder sb;





    public ServiceHandler() {

    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        String response = "";

        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();



            // Checking http request method type
            if (method == POST) {

                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded ");

                if (params != null) {

                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                    }
                    catch (UnsupportedEncodingException e) {
                        // writing error to Log
                        e.printStackTrace();
                    }
                    // Making HTTP Request
                    try {

                         HttpResponse httpResponse = httpClient.execute(httpPost);

                        // writing response to log
                        Log.d("Http Response:  ", httpResponse.toString());

                        HttpEntity httpEntity = httpResponse.getEntity();
                        response = EntityUtils.toString(httpEntity);



                    } catch (ClientProtocolException e) {
                        // writing exception to log
                        e.printStackTrace();

                    } catch (IOException e) {
                        // writing exception to log
                        e.printStackTrace();
                    }

                }


            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            }





        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

}