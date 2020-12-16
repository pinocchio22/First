package kr.ac.daejin.restaurantnews.fragments.main;

import android.os.Bundle;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.daejin.restaurantnews.fragments.preference.BoardFragment;
import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.adapter.SearchAdapter;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentSearchBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;

public class SearchFragment extends BaseFragment<FragmentSearchBinding> {
    private BoardFragment boardFragment = BoardFragment.newInstance();

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_search;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] searchOption = {"name"};
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) binding.spinner.getItemIdAtPosition(position)) {
                    case 0 :
                        searchOption[0] = "name";
                        break;
                    case 1 :
                        searchOption[0] = "newAddress";
                        break;
                    case 2:
                        searchOption[0] = "category";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchAdapter searchAdapter = new SearchAdapter();

        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.searchRecyclerView.setAdapter(searchAdapter);
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.searchWord.getText().toString().replace(" ", "").equals("")){
                    showToast("검색어를 입력해주세요.");
                    return;
                }
                searchAdapter.actionSearch(requireContext(), binding.searchWord.getText().toString(), searchOption[0]);
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        binding.searchRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    PlaceData placeData = searchAdapter.placeDataList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("PlaceData", placeData);
                    boardFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, boardFragment).addToBackStack(null).commitAllowingStateLoss();
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }


}