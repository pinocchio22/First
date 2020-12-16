package kr.ac.daejin.restaurantnews.fragments.main;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentNfcBinding;

public class NfcFragment extends BaseFragment<FragmentNfcBinding> {

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_nfc;
    }

    public NfcFragment() {
         // Required empty public constructor
    }

    public static NfcFragment newInstance() {

        Bundle args = new Bundle();

        NfcFragment fragment = new NfcFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}