package kr.ac.daejin.restaurantnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.base.BaseActivity;
import kr.ac.daejin.restaurantnews.databinding.ActivityMainBinding;
import kr.ac.daejin.restaurantnews.fragments.main.NotificationFragment;
import kr.ac.daejin.restaurantnews.fragments.main.SearchFragment;
import kr.ac.daejin.restaurantnews.fragments.main.MapFragment;
import kr.ac.daejin.restaurantnews.fragments.main.NfcFragment;
import kr.ac.daejin.restaurantnews.fragments.main.PreferencesFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static String TAG = "mainActivity";
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private NotificationFragment notificationFragment = NotificationFragment.newInstance();
    private SearchFragment searchFragment = SearchFragment.newInstance();
    private MapFragment mapFragment = MapFragment.newInstance();
    private NfcFragment nfcFragment = NfcFragment.newInstance();
    private PreferencesFragment preferencesFragment = PreferencesFragment.newInstance();
//    private boolean singleBack = false;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isNotify = getIntent().getBooleanExtra("isNotify", false);
        Log.d(TAG, "isNotify1 : " + isNotify);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            showToast("로그인 되었습니다.");
        } else {
            showToast("로그인 해주세요.");
            startActivity(new Intent(this, LoginActivity.class));
            MainActivity.this.finish();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Log.d(TAG, "isNotify2 : " + isNotify);
        if(isNotify) {
            transaction.add(R.id.frame_layout, notificationFragment).commitAllowingStateLoss();
            binding.bottomNavigationView.setSelectedItemId(R.id.menu1);
        } else {
            transaction.add(R.id.frame_layout, mapFragment).commitAllowingStateLoss();
            binding.bottomNavigationView.setSelectedItemId(R.id.menu3);
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(fragmentManager.getBackStackEntryCount() >= 1) {
                    do {
                        fragmentManager.popBackStack();
                    } while (fragmentManager.getBackStackEntryCount() == 0);
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.menu1: {
                        transaction.replace(R.id.frame_layout, notificationFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu2: {
                        transaction.replace(R.id.frame_layout, searchFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu3: {
                        transaction.replace(R.id.frame_layout, mapFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu4: {
                        transaction.replace(R.id.frame_layout, nfcFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.menu5: {
                        transaction.replace(R.id.frame_layout, preferencesFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }
/*
    @Override
    public void onBackPressed() {
        if (singleBack) {
            super.onBackPressed();
            return;
        }

        this.singleBack = true;
        showToast("Double Back to exit");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                singleBack = false;
            }
        }, 2000);
    }*/
}