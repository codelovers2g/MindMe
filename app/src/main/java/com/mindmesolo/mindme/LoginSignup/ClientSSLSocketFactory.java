//package com.midmesolo.mindme.LoginSignup;
//
//import android.app.Application;
//import android.net.SSLCertificateSocketFactory;
//import android.net.SSLSessionCache;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import javax.security.cert.CertificateException;
//import javax.security.cert.X509Certificate;
//
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.X509TrustManager;
//import java.security.cert.X509Certificate;
//import java.util.ResourceBundle;
//
///**
// * Created by enest_09 on 1/23/2017.
// */
//
//public class ClientSSLSocketFactory extends SSLCertificateSocketFactory {
//    private SSLContext sslContext;
//
//    /**
//     * @param handshakeTimeoutMillis
//     * @deprecated
//     */
//    public ClientSSLSocketFactory(int handshakeTimeoutMillis) {
//        super(handshakeTimeoutMillis);
//    }
//
//    public static SSLSocketFactory getSocketFactory(){
//        try
//        {
//            X509TrustManager tm = new X509TrustManager() {
//                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
//
//                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
//
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//            };
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[] { tm }, null);
//
//            SSLSocketFactory ssf = ClientSSLSocketFactory.getDefault(10000, new SSLSessionCache(Application.getInstance()));
//
//            return ssf;
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
//        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//    }
//
//    @Override
//    public Socket createSocket() throws IOException {
//        return sslContext.getSocketFactory().createSocket();
//    }
//}
