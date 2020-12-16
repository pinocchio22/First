package kr.ac.daejin.restaurantnews.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import kr.ac.daejin.restaurantnews.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isNotify = false;
        // 이제 functions에서 넘길때 값을 보고 isNotify 바꿔주면 될듯.
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("getIntent", "Key: " + key + " Value: " + value);
                if(key.equals("from")) {
                    isNotify = true;
                }
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isNotify", isNotify);
        startActivity(intent);
        finish();
    }
}