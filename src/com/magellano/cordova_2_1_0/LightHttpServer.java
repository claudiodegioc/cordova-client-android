package com.magellano.cordova_2_1_0;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;

import android.util.Log;

public class LightHttpServer implements Runnable {

	private HttpService httpService = null;
	private BasicHttpProcessor httpproc = null;

	public LightHttpServer() {

		httpproc = new BasicHttpProcessor();

		httpService = new HttpService(httpproc,
				new DefaultConnectionReuseStrategy(),
				new DefaultHttpResponseFactory());

		HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();

		registry.register("/reload", new LightHttpRequestHandler());

		httpService.setHandlerResolver(registry);

	}

	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(1337);

			serverSocket.setReuseAddress(true);

			Log.d("teytew", "wating");

			while (true) {

				final Socket connection = serverSocket.accept();

				final DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();

				serverConnection.bind(connection, new BasicHttpParams());

				httpService.handleRequest(serverConnection,
						new BasicHttpContext());
				serverConnection.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class LightHttpRequestHandler implements HttpRequestHandler {

		public void handle(HttpRequest request, HttpResponse response,
				HttpContext context) throws HttpException, IOException {
			Log.i("test", "relaod function");
			
			HttpEntity entity = new StringEntity("OK");
			
			response.setEntity(entity);
		}

	}

}
