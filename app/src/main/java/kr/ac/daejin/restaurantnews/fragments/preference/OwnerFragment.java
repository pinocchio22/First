package kr.ac.daejin.restaurantnews.fragments.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentOwnerBinding;

public class OwnerFragment extends BaseFragment<FragmentOwnerBinding> {

    public OwnerFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_owner;
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

}
