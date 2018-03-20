package com.angels.web.webkit;

import com.angels.web.widget.ACWebView;

import android.app.Activity;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 2015/8/12.
 */
public class ACWebChromeClient extends WebChromeClient {
	private ACWebView webView;

	public ACWebChromeClient(ACWebView webView) {
		this.webView = webView;
	}

	@Override
	public void onCloseWindow(WebView window) {
		super.onCloseWindow(window);
		Log.i("ac", "onCloseWindow" + window);
		if (((ACWebView) window).getWebViewListener() != null) {
			((ACWebView) window).getWebViewListener().closeWindow(null);
		}
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		webView.onProgressChanged(newProgress);
		super.onProgressChanged(view, newProgress);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		if (webView.getTvTitle() != null)
			webView.getTvTitle().setText(title);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
		Log.i("ac", "onJsAlert");
		return super.onJsAlert(view, url, message, result);
	}

	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message,
			JsResult result) {
		Log.i("ac", "onJsBeforeUnload");
		return super.onJsBeforeUnload(view, url, message, result);
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			JsResult result) {
		Log.i("ac", "onJsConfirm");
		return super.onJsConfirm(view, url, message, result);
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
		Log.i("ac", "onJsPrompt");
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}
}
