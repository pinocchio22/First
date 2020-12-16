package kr.ac.daejin.restaurantnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.adapter.SearchAdapter;
import kr.ac.daejin.restaurantnews.base.BaseActivity;
import kr.ac.daejin.restaurantnews.databinding.FragmentSearchBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;

public class SearchActivity extends BaseActivity<FragmentSearchBinding> {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.searchRecyclerView.setAdapter(searchAdapter);
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.searchWord.getText().toString().replace(" ", "").equals("")) {
                    showToast("검색어를 입력해주세요.");
                    return;
                }
                searchAdapter.actionSearch(SearchActivity.this, binding.searchWord.getText().toString(), searchOption[0]);
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
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
                    onBackPressed(getIntent(), placeData);
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

    public void onBackPressed(Intent intent, PlaceData placeData) {
        intent.putExtra("select", placeData);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }
}
