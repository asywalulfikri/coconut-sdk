package app.beelabs.com.codebase.base;


import app.beelabs.com.codebase.di.IApi;
import app.beelabs.com.codebase.di.component.AppComponent;
import okhttp3.Interceptor;


/**
 * Created by arysuryawan on 8/18/17.
 */

public class BaseApi {
    private String apiDomain;
    private static BaseApi baseApi;

    public static BaseApi getInstance() {
        if (baseApi == null)
            baseApi = new BaseApi();
        return baseApi;
    }
//
//    public String getApiDomain() {
//        return apiDomain;
//    }
//
//    public void setApiDomain(String apiDomain) {
//        this.apiDomain = apiDomain;
//    }
//
//    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp) {
//        return setupApi(appComponent, clazz, false, timeout, true, null);
//    }
//
//    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, Interceptor interceptor) {
//        return setupApi(appComponent, clazz, allowUntrusted, timeout, enableLoggingHttp, null, interceptor);
//    }
//
//    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, String PedePublicKeyRSA) {
//        IApi api = appComponent.getApi();
//        return api.initApiService(getApiDomain(), allowUntrusted, clazz, timeout, enableLoggingHttp, PedePublicKeyRSA);
//    }
//
//    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, String PedePublicKeyRSA, Interceptor interceptor) {
//        IApi api = appComponent.getApi();
//        return api.initApiService(getApiDomain(), allowUntrusted, clazz, timeout, enableLoggingHttp, PedePublicKeyRSA, interceptor);
//    }
//
//    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, String PedePublicKeyRSA) {
//        IApi api = appComponent.getApi();
//        return api.initApiService(getApiDomain(), allowUntrusted, clazz, timeout, enableLoggingHttp, PedePublicKeyRSA);
//    }
//
//    public Object setupApiWithInterceptors(String domain, AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, Interceptor[] interceptors){
//        this.apiDomain = domain;
//        return appComponent.getApi().initApiService(domain, allowUntrusted, clazz, timeout, enableLoggingHttp, interceptors);
//    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp) {
        return setupApi(appComponent, clazz, allowUntrusted, timeout, enableLoggingHttp, (String) null);
    }

    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, Interceptor interceptor) {
        return setupApi(appComponent, clazz, allowUntrusted, timeout, enableLoggingHttp, null, interceptor);
    }

    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, Interceptor[] appInterceptor, Interceptor[] netInterceptor) {
        return setupApi(appComponent, clazz, allowUntrusted, timeout, enableLoggingHttp, null, appInterceptor, netInterceptor);
    }


    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, String PedePublicKeyRSA) {
        IApi api = appComponent.getApi();
        return api.initApiService(getApiDomain(), allowUntrusted, clazz, timeout, enableLoggingHttp, PedePublicKeyRSA);
    }

    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, String PedePublicKeyRSA, Interceptor interceptor) {
        IApi api = appComponent.getApi();
        return api.initApiService(getApiDomain(), allowUntrusted, clazz, timeout, enableLoggingHttp, PedePublicKeyRSA, interceptor);
    }

    public Object setupApi(AppComponent appComponent, Class clazz, boolean allowUntrusted, int timeout, boolean enableLoggingHttp, String PedePublicKeyRSA, Interceptor[] appInterceptor, Interceptor[] netInterceptor) {
        IApi api = appComponent.getApi();
        return api.initApiService(getApiDomain(), allowUntrusted, clazz, timeout, enableLoggingHttp, PedePublicKeyRSA, appInterceptor, netInterceptor);
    }
}











