package com.magellano.cordova_2_1_0;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedByInterruptException;

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

import android.content.res.Resources.Theme;
import android.util.Log;

public class ReloadHttpServer implements Runnable {

	final static private String TAG = "ReloadHttpServer";

	private HttpService httpService = null;
	private BasicHttpProcessor httpproc = null;
	private Thread thread = null;

	public ReloadHttpServer() {
		httpproc = new BasicHttpProcessor();

		httpService = new HttpService(httpproc,
				new DefaultConnectionReuseStrategy(),
				new DefaultHttpResponseFactory());

		HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();

		registry.register("/reload", new LightHttpRequestHandler());

		httpService.setHandlerResolver(registry);

	}

	synchronized public void start() {
		if (thread == null) {
			Log.i(TAG, "Start server");
			thread = new Thread(this);
			thread.start();
		}
	}

	synchronized public void stop() {
		if (thread != null) {
			Log.i(TAG, "Stop thread");
			thread.interrupt();
		}
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(1337);
			
			Log.i(TAG, "Open server socket on port " + serverSocket.getLocalPort());

			serverSocket.setReuseAddress(true);
			serverSocket.setSoTimeout(5000);

			while (!Thread.currentThread().isInterrupted()) {
				Log.d(TAG, "Waiting for new connection");

				try {
					final Socket connection = serverSocket.accept();

					final DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();

					serverConnection.bind(connection, new BasicHttpParams());

					httpService.handleRequest(serverConnection,
							new BasicHttpContext());
					serverConnection.close();
				} catch (SocketTimeoutException e) {
					// ignore and keep looping
				} catch (InterruptedIOException e) {
					// got signal while waiting for connection request
					break;
				}
			}

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (serverSocket != null) {
				try {
					Log.i(TAG, "Close server socket on port " + serverSocket.getLocalPort());
					serverSocket.close();
				} catch (IOException e) {
				}
			}
		}
	}

	class LightHttpRequestHandler implements HttpRequestHandler {

		public void handle(HttpRequest request, HttpResponse response,
				HttpContext context) throws HttpException, IOException {
			Log.i(TAG, "Reload action called");

			HttpEntity entity = new StringEntity("OK");

			response.setEntity(entity);
		}

	}

}
