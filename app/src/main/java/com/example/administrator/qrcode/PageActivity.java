package com.example.administrator.qrcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class PageActivity extends Activity implements TextWatcher {
	private WebView webView;
	private ProgressDialog dialog;
	private TextView tvHome;
	private TextView tvNet;
	private EditText etNet;
	private ImageView etDelete;
	private View etLayout;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		Intent intent = getIntent();
		url = intent.getStringExtra("click");

		setWebView();
		initViews();
		addListeners();
	}

	
	protected void setWebView() {
		webView = (WebView) findViewById(R.id.web_view);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// ������� �ر�dialog
					closeDialog();
				} else {
					// ���ڼ��� ��dialog
					openDialog(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				return true;
			}
		});
		webView.loadUrl(url);
	}
	
	/**
	 * ��ʼ���ؼ�
	 */
	private void initViews() {
		tvHome = (TextView) findViewById(R.id.tv_home);
		tvNet = (TextView) findViewById(R.id.tv_net);
		tvNet.setText(url);
		etNet = (EditText) findViewById(R.id.web_et);
		etLayout = findViewById(R.id.web_et_layout);
		etDelete = (ImageView) findViewById(R.id.web_et_delete);
	}

	
	/**
	 * ���ü����¼�
	 */
	private void addListeners() {
		tvNet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvNet.setVisibility(View.GONE);
				etLayout.setVisibility(View.VISIBLE);
				etNet.setText(url);
				etDelete.setVisibility(View.VISIBLE);
			}
		});
		
		tvHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		etNet.addTextChangedListener(this);
	}

	private void closeDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	private void openDialog(int newProgress) {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setTitle("���ڼ���");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setProgress(newProgress);
			dialog.show();
		} else {
			dialog.setProgress(newProgress);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			webView.goBack();
			return true;
		} else {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.web_et_delete:
			// һ������ı������ݣ�����Լ���ʧ
			etNet.setText("");
			etDelete.setVisibility(View.GONE);
			break;

		case R.id.web_bt:
			// �ٶ������ı������ݣ���������뷨����
			String net = GlobalConsts.BAIDU + etNet.getText().toString();
			webView.loadUrl(net);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		}
	}

	/**
	 * �����ı��������¼�
	 */
	@Override
	public void afterTextChanged(Editable arg0) {
	}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int count) {
		if (count > 0) {
			etDelete.setVisibility(View.VISIBLE);
		}
	}

}
