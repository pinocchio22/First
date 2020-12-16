package kr.ac.daejin.restaurantnews.fragments.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentEmailBinding;


public class EmailFragment extends BaseFragment<FragmentEmailBinding> {

    private TextView textView;

    public EmailFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_email;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = getView().findViewById(R.id.btn_email);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setFlags(email.FLAG_ACTIVITY_NEW_TASK);
                email.setType("text/plain");
                email.setPackage("com.google.android.gm");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jin950523@naver.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "문의하기");
                email.putExtra(Intent.EXTRA_TEXT,"내용");
                startActivity(email);
            }
        });
    }
}
