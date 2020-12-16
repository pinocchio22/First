package kr.ac.daejin.restaurantnews.fragments.preference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.adapter.SettingAdapter;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentSettingBinding;

public class SettingFragment extends BaseFragment<FragmentSettingBinding> {

    private SettingAdapter adapter;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Adapter 생성
        adapter = new SettingAdapter(requireContext());
        //리스트뷰 참조 및 Adapter 달기
        binding.listView.setAdapter(adapter);

        adapter.addItem("알림설정", R.drawable.ic_launcher_background);
        adapter.addItem("FAQ", R.drawable.ic_launcher_background);
        adapter.addItem("e-mail 문의", R.drawable.ic_launcher_background);
        adapter.addItem("앱 평가", R.drawable.ic_launcher_background);
        adapter.addItem("회원탈퇴", R.drawable.ic_launcher_background);
        adapter.addItem("버전정보", R.drawable.ic_launcher_background);

        adapter.notifyDataSetChanged();
    }
}