package com.ellen.baselibrary.net.okhttp.download;

import android.content.Context;
import android.util.Log;

import com.ellen.baselibrary.net.okhttp.okhttpclient.AutoOkHttpClient;
import com.ellen.baselibrary.net.okhttp.request.AutoRequest;
import com.ellen.baselibrary.net.okhttp.request.RequestInterface;
import com.ellen.baselibrary.net.okhttp.request.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * OkHttp下载文件管理类
 * 1.支持多线程下载
 * 2.支持断点续传
 */
public class OkHttpDownloadManager {

    private Context context;

    public OkHttpDownloadManager(Context context){
        this.context = context;
    }


    /**
     * 断点续传下载
     * @param url 下载的地址
     * @param start 下载的起始点
     * @param fatherPath 下载的文件存储的父目录
     * @param fileName 下载的文件名
     * @param downloadCallback 下载回调
     */
    public void download(String url, final long start, final String fatherPath, final String fileName, final DownloadCallback downloadCallback){
        //1.创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new AutoOkHttpClient.Builder(context)
                .connectionTimeOut(30)
                .setRetryOnConnectionFailure(false)
                .build().getOkHttpClient();
        //2.创建一个Response对象
        RequestParams requestParams = new RequestParams();
        requestParams.put("Range","bytes="+start+"-");
        requestParams.put("Connection","close");
        Request request = new AutoRequest.Builder().url(url)
                .setRequestHeaderParams(requestParams)
                .setRequestBuilderExpandInterface(new RequestInterface.RequestBuilderExpandInterface() {
                    @Override
                    public void expand(Request.Builder builder) {

                    }
                })
                .build().createPostRequest();

        //3.进行异步请求
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadCallback.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;

                File fatherFile = new File(fatherPath);
                if(!fatherFile.exists()){
                    fatherFile.mkdirs();
                }
                File tagetFile = new File(fatherFile,fileName);

                RandomAccessFile randomAccessFile =  new RandomAccessFile(tagetFile, "rw");
                randomAccessFile.seek (start);

                //进行文件的存储
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    Log.e("服务器返回码",response.code()+"");
                    Log.e("下载文件总长度",total+"");
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        randomAccessFile.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        downloadCallback.onDownloading(progress);

                    }
                    //下载完成
                    downloadCallback.onDownloadSuccess(tagetFile);
                    response.body().close();
                } catch (Exception e) {
                    //下载出现异常
                    downloadCallback.onDownloadFailed(e);
                }finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if(randomAccessFile != null){
                            randomAccessFile.close();
                        }
                    } catch (IOException e) {

                    }

                }

            }
        });
   }

   public void download(String url, final String fatherPath, final String fileName, final DownloadCallback downloadCallback){

       //1.创建一个OkHttpClient对象
       OkHttpClient okHttpClient = new AutoOkHttpClient.Builder(context).build().getOkHttpClient();
       //2.创建一个Response对象
       Request request = new AutoRequest.Builder().url(url)
               .build().createPostRequest();
       //3.进行异步请求
       Call call = okHttpClient.newCall(request);
       call.enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               downloadCallback.onDownloadFailed(e);
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               InputStream is = null;
               byte[] buf = new byte[2048];
               int len = 0;
               FileOutputStream fos = null;

               File fatherFile = new File(fatherPath);
               if(!fatherFile.exists()){
                   fatherFile.mkdirs();
               }
               File tagetFile = new File(fatherFile,fileName);

               //进行文件的存储
               try {

                   is = response.body().byteStream();
                   long total = response.body().contentLength();
                   //Log.e("下载文件总长度",total+"");
                   //9143908
                   fos = new FileOutputStream(tagetFile);
                   long sum = 0;
                   while ((len = is.read(buf)) != -1) {
                       fos.write(buf, 0, len);
                       sum += len;
                       int progress = (int) (sum * 1.0f / total * 100);
                       //下载中更新进度条
                       downloadCallback.onDownloading(progress);
                   }
                   fos.flush();
                   //下载完成
                   downloadCallback.onDownloadSuccess(tagetFile);
                   response.body().close();
               } catch (Exception e) {
                   //下载出现异常
                   downloadCallback.onDownloadFailed(e);
               }finally {
                   try {
                       if (is != null) {
                           is.close();
                       }
                       if (fos != null) {
                           fos.close();
                       }
                   } catch (IOException e) {

                   }

               }

           }
       });

   }

}
