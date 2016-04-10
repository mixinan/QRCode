package com.example.administrator.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.R;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class MainActivity extends Activity {
	private TextView tvResult;
	private ImageView iv;
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvResult = (TextView) findViewById(R.id.textView1);
		iv = (ImageView) findViewById(R.id.imageView1);
		et = (EditText)findViewById(R.id.editText1);
		
	}

	public void scan(View view){
		startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			tvResult.setText(result);
		}
	}


    public void make(View view){
        String string = et.getText().toString();
        if(string.equals("")){
            Toast.makeText(this,"输入不能为空",Toast.LENGTH_LONG).show();
        }else{
            Bitmap bitmap = EncodingUtils.createQRCode(string,500,500,null);
            iv.setImageBitmap(bitmap);
        }
    }
}
