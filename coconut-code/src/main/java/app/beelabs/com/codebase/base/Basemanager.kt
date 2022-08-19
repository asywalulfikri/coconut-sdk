package app.beelabs.com.codebase.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import app.beelabs.com.codebase.base.request.RequestInterceptor
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.datatheorem.android.trustkit.pinning.OkHttp3Helper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by arysuryawan on 11/10/17.
 */
open class BaseManager {
    protected fun getHttpClient(
        allowUntrustedSSL: Boolean,
        timeout: Int,
        enableLoggingHttp: Boolean,
        customInterceptors: Array<Interceptor?>?
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (allowUntrustedSSL) {
            allowUntrustedSSL(httpClient)
        }
        try {
            val sc = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)
           // httpClient.sslSocketFactory(TLS12SocketFactory(sc.socketFactory))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        httpClient.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)

        // interceptor logging HTTP request
        if (enableLoggingHttp) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }
        if (customInterceptors != null) {
            for (interceptor in customInterceptors) {
                if (interceptor != null) {
                    httpClient.addInterceptor(interceptor)
                }
            }
        }
        return httpClient.build()
    }

    protected fun getHttpClient(
        allowUntrustedSSL: Boolean,
        timeout: Int,
        enableLoggingHttp: Boolean,
        PedePublicKeyRSA: String?,
        appInterceptor: Interceptor?
    ): OkHttpClient {
        val interceptors = arrayOfNulls<Interceptor>(1)
        interceptors[0] = appInterceptor
        return getHttpClient(
            allowUntrustedSSL,
            timeout,
            enableLoggingHttp,
            PedePublicKeyRSA,
            interceptors,
            null
        )
    }

    protected fun getHttpClient(
        context: Context, allowUntrustedSSL: Boolean,
        timeout: Int,
        enableLoggingHttp: Boolean,
        PedePublicKeyRSA: String?,
        appInterceptor: Interceptor?
    ): OkHttpClient {
        val interceptors = arrayOfNulls<Interceptor>(1)
        interceptors[0] = appInterceptor
        return getHttpClient(
            context,
            allowUntrustedSSL,
            timeout,
            enableLoggingHttp,
            PedePublicKeyRSA,
            interceptors,
            null
        )
    }

    @SuppressLint("NewApi")
    protected fun getHttpClient(
        allowUntrustedSSL: Boolean,
        timeout: Int,
        enableLoggingHttp: Boolean,
        PedePublicKeyRSA: String?,
        appInterceptor: Array<Interceptor?>?,
        netInterceptor: Array<Interceptor?>?
    ): OkHttpClient {

//        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .sslSocketFactory(OkHttp3Helper.getSSLSocketFactory(), OkHttp3Helper.getTrustManager())
            .addInterceptor(OkHttp3Helper.getPinningInterceptor())
            .followRedirects(false)
            .followSslRedirects(false)
        if (allowUntrustedSSL) {
            allowUntrustedSSL(httpClient)
            try {
                val trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                }
                val trustManager = trustManagers[0] as X509TrustManager
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, arrayOf<TrustManager>(trustManager), null)
               // httpClient.sslSocketFactory(TLS12SocketFactory(sc.socketFactory))
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }
        }
        httpClient.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)

        // interceptor RSA for body encryption
        httpClient.addInterceptor(RequestInterceptor(PedePublicKeyRSA))

        // interceptor logging HTTP request
        if (enableLoggingHttp) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }
        if (appInterceptor != null) {
            for (interceptor in appInterceptor) {
                if (interceptor != null) {
                    httpClient.addInterceptor(interceptor)
                }
            }
        }
        if (netInterceptor != null) {
            for (interceptor in netInterceptor) {
                if (interceptor != null) {
                    httpClient.addNetworkInterceptor(interceptor)
                }
            }
        }
        return httpClient.build()
    }

    @SuppressLint("NewApi")
    protected fun getHttpClient(
        allowUntrustedSSL: Boolean,
        timeout: Int,
        enableLoggingHttp: Boolean,
        PedePublicKeyRSA: String?
    ): OkHttpClient {

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .sslSocketFactory(OkHttp3Helper.getSSLSocketFactory(), OkHttp3Helper.getTrustManager())
            .addInterceptor(OkHttp3Helper.getPinningInterceptor())
            .followRedirects(false)
            .followSslRedirects(false)
        if (allowUntrustedSSL) {
            allowUntrustedSSL(httpClient)
            try {
                val trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                }
                val trustManager = trustManagers[0] as X509TrustManager
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, arrayOf<TrustManager>(trustManager), null)
               // httpClient.sslSocketFactory(TLS12SocketFactory(sc.socketFactory))
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }
        }
        httpClient.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)

        // interceptor RSA for body encryption
        httpClient.addInterceptor(RequestInterceptor(PedePublicKeyRSA))

        // interceptor logging HTTP request
        if (enableLoggingHttp) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }
        return httpClient.build()
    }

    private fun allowUntrustedSSL(httpClient: OkHttpClient.Builder) {
        Log.w("", "**** Allow untrusted SSL connection ****")
        val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }
        })
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        try {
            sslContext!!.init(null, trustAllCerts, SecureRandom())
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        httpClient.sslSocketFactory(
            sslContext!!.socketFactory,
            trustAllCerts[0] as X509TrustManager
        )
        val hostnameVerifier =
            HostnameVerifier { hostname, session ->
                Log.d("", "Trust Host :$hostname")
                true
            }
        httpClient.hostnameVerifier(hostnameVerifier)
    }

    @SuppressLint("NewApi")
    protected fun getHttpClient(context: Context, allowUntrustedSSL: Boolean, timeout: Int,
        enableLoggingHttp: Boolean, PedePublicKeyRSA: String?,
        appInterceptor: Array<Interceptor?>?, netInterceptor: Array<Interceptor?>?): OkHttpClient {

        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        @Suppress("MagicNumber")
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(collector)
            .maxContentLength(250_000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .sslSocketFactory(OkHttp3Helper.getSSLSocketFactory(), OkHttp3Helper.getTrustManager())
            .addInterceptor(OkHttp3Helper.getPinningInterceptor())
            .followRedirects(false)
            .followSslRedirects(false)

        if (allowUntrustedSSL) {
            allowUntrustedSSL(httpClient)
            try {
                val trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + Arrays.toString(
                        trustManagers
                    )
                }
                val trustManager = trustManagers[0] as X509TrustManager
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, arrayOf<TrustManager>(trustManager), null)
                //httpClient.sslSocketFactory(TLS12SocketFactory(sc.socketFactory))
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }
        }
        httpClient.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)

        // interceptor RSA for body encryption
        httpClient.addInterceptor(RequestInterceptor(PedePublicKeyRSA))

        // interceptor logging HTTP request
        if (enableLoggingHttp) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
            httpClient.addInterceptor(chuckerInterceptor)
        }
        if (appInterceptor != null) {
            for (interceptor in appInterceptor) {
                if (interceptor != null) {
                    httpClient.addInterceptor(interceptor)
                }
            }
        }
        if (netInterceptor != null) {
            for (interceptor in netInterceptor) {
                if (interceptor != null) {
                    httpClient.addNetworkInterceptor(interceptor)
                }
            }
        }
        return httpClient.build()
    }
}