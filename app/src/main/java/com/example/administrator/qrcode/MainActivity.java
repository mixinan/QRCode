package com.example.administrator.qrcode;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.R;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

public class MainActivity extends Activity {
    private ImageView iv;
    private EditText et;
    private TextView tv;
    private String result;
    private ToastUtil toast = new ToastUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.imageView);
        et = (EditText)findViewById(R.id.editText);
    }

    public void scan(View view){
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            result = bundle.getString("result");
            tv.setText(result);
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

    public void doClick(View view) {
        if(!TextUtils.isEmpty(result)){
            switch (view.getId()) {

                case R.id.btCopy:
                    copy(result, getBaseContext());
                    toast.showToast(MainActivity.this, "已经复制到剪贴板");
                    break;

                case R.id.btGo:
                    if (result.contains("http") ||
                            result.endsWith(".com") ||
                            result.endsWith(".cn") ||
                            result.endsWith(".cc") ||
                            result.endsWith(".html")) {
                        if (NetworkUtil.getNetWorkType(MainActivity.this) == NetworkUtil.NONETWORK) {
                            toast.showToast(MainActivity.this, "没有联网");
                        } else openTheNet(result);
                    } else toast.showToast(MainActivity.this, "这不是网址吧？");
                    break;

                case R.id.btDial:
                    String telRegex = "[1][358]\\d{9}";
                    if (result.matches(telRegex)) {
                        dialTheNumber(result);
                    } else toast.showToast(MainActivity.this, "这不是电话吧？");
                    break;
            }
        }else
            toast.showToast(MainActivity.this,"内容为空");
    }


    public static void copy(String string, Context context) {
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(string.trim());
    }

    private void dialTheNumber(String str) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + str));
        startActivity(intent);
    }

    protected void openTheNet(String str) {
        Intent openUrl_Intent = new Intent(this, WebActivity.class);
        openUrl_Intent.putExtra("click", str);
        startActivity(openUrl_Intent);
    }


    private long lastBackTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackTime < 1500) {
            finish();
        }
        lastBackTime = System.currentTimeMillis();
        toast.showToast(this, "再按一次退出");
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
