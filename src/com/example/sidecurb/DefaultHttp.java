package com.example.sidecurb;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class DefaultHttp implements HttpClient {
	
	private static HttpClient mClient;
    private DefaultHttp() {};
    public static HttpClient getInstance() {
        if(mClient==null)
            mClient = new DefaultHttpClient();
        return mClient;
    }

	@Override
	public HttpResponse execute(HttpUriRequest arg0) throws IOException,
			ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse execute(HttpUriRequest arg0, HttpContext arg1)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse execute(HttpHost arg0, HttpRequest arg1)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpResponse execute(HttpHost arg0, HttpRequest arg1,
			HttpContext arg2) throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpUriRequest arg0,
			ResponseHandler<? extends T> arg1, HttpContext arg2)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2) throws IOException,
			ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2, HttpContext arg3)
			throws IOException, ClientProtocolException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public ClientConnectionManager getConnectionManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public HttpParams getParams() {
		// TODO Auto-generated method stub
		return null;
	}

}
