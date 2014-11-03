package server.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import common.MyX509TrustManager;



public class HTTPSCommunication {

	/** 
     * 发送HTTP请求 
     *  
     * @param urlString 
     * @return 响映对象 
     * @throws IOException 
     */  
    public Map<String,String> send(String urlString, String method,  
            Map<String, String> parameters, Map<String, String> propertys)  
            throws IOException {  
        HttpsURLConnection urlConnection = null;  
    
        if (method.equalsIgnoreCase("GET") && parameters != null) {  
            StringBuffer param = new StringBuffer();  
            int i = 0;  
            for (String key : parameters.keySet()) {  
                if (i == 0)  
                    param.append("?");  
                else  
                    param.append("&");  
                param.append(key).append("=").append(parameters.get(key));  
                i++;  
            }  
            urlString += param;  
        }  
        System.out.println(urlString);

        TrustManager[] tm = {new MyX509TrustManager ()};  
    	SSLSocketFactory ssf  = null;
        SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			ssf = sslContext.getSocketFactory(); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}  
     
        URL url = new URL(urlString);  
        urlConnection = (HttpsURLConnection) url.openConnection();  
        urlConnection.setSSLSocketFactory(ssf);
    
        urlConnection.setRequestMethod(method);  
        urlConnection.setDoOutput(true);  
        urlConnection.setDoInput(true);  
        urlConnection.setUseCaches(false);  
    
        if (propertys != null)  
            for (String key : propertys.keySet()) {  
                urlConnection.addRequestProperty(key, propertys.get(key));  
            }  
    
        if (method.equalsIgnoreCase("GET") && parameters != null) {  
            StringBuffer param = new StringBuffer();  
            for (String key : parameters.keySet()) {  
                param.append("&");  
                param.append(key).append("=").append(parameters.get(key));  
            }  
            urlConnection.getOutputStream().write(param.toString().getBytes());  
            urlConnection.getOutputStream().flush();  
            urlConnection.getOutputStream().close();  
        }  
    
     return   makeContent(urlString, urlConnection);  
    }  
    
    /** 
     * 得到响应对象 
     *  
     * @param urlConnection 
     * @return 响应对象 
     * @throws IOException 
     */  
    private Map<String,String> makeContent(String urlString,  
            HttpsURLConnection urlConnection) throws IOException {  
    		
    	
    	Map<String,String> map = new HashMap<String, String>();
        String line  = "";
        try {  
            InputStream in = urlConnection.getInputStream();  
            BufferedReader bufferedReader = new BufferedReader(  
                    new InputStreamReader(in));  
//            httpResponser.contentCollection = new Vector<String>();  
            StringBuffer temp = new StringBuffer();  
            line = bufferedReader.readLine();  
            System.out.println("reply:" + line);
            bufferedReader.close();  
    
            JSONObject json = JSONObject.fromObject(line);
            map= (Map) json;
//            map.put("access_token", json.getString("access_token"));
//            map.put("expires_in", json.getString("expires_in"));
//            map.put("scope", json.getString("scope"));
//            map.put("refresh_token", json.getString("refresh_token"));
            
//            String ecod = urlConnection.getContentEncoding();  
//            if (ecod == null)  
//                ecod = this.defaultContentEncoding;  
//    
//            httpResponser.urlString = urlString;  
//    
//            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();  
//            httpResponser.file = urlConnection.getURL().getFile();  
//            httpResponser.host = urlConnection.getURL().getHost();  
//            httpResponser.path = urlConnection.getURL().getPath();  
//            httpResponser.port = urlConnection.getURL().getPort();  
//            httpResponser.protocol = urlConnection.getURL().getProtocol();  
//            httpResponser.query = urlConnection.getURL().getQuery();  
//            httpResponser.ref = urlConnection.getURL().getRef();  
//            httpResponser.userInfo = urlConnection.getURL().getUserInfo();  
//    
//            httpResponser.content = new String(temp.toString().getBytes(), ecod);  
//            httpResponser.contentEncoding = ecod;  
//            httpResponser.code = urlConnection.getResponseCode();  
//            httpResponser.message = urlConnection.getResponseMessage();  
//            httpResponser.contentType = urlConnection.getContentType();  
//            httpResponser.method = urlConnection.getRequestMethod();  
//            httpResponser.connectTimeout = urlConnection.getConnectTimeout();  
//            httpResponser.readTimeout = urlConnection.getReadTimeout();  
//    
//            return httpResponser;  
        } catch (Exception e) { 
        	System.out.println("Exception...");
        	e.printStackTrace();
        } finally {  
            if (urlConnection != null)  
                urlConnection.disconnect();  
        }  
        return map;
    }  
	
}
