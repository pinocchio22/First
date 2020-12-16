package kr.ac.daejin.restaurantnews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.fragments.preference.BoardFragment;
import kr.ac.daejin.restaurantnews.fragments.preference.BookmarkFragment;
import kr.ac.daejin.restaurantnews.fragments.preference.HistoryFragment;
import kr.ac.daejin.restaurantnews.fragments.preference.InfoChangeFragment;
import kr.ac.daejin.restaurantnews.model.ListViewItem;
import kr.ac.daejin.restaurantnews.fragments.preference.ReviewFragment;
import kr.ac.daejin.restaurantnews.fragments.preference.SettingFragment;

public class ListViewAdapter extends BaseAdapter {

    private ImageView iconImageView;
    private TextView titleTextView;
    private TextView contentTextView;
    private FragmentManager fragmentManager;
    private InfoChangeFragment infoChangeFragment = new InfoChangeFragment();
    private HistoryFragment historyFragment = new HistoryFragment();
    private BookmarkFragment bookmarkFragment = new BookmarkFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private ReviewFragment reviewFragment = new ReviewFragment();
    private BoardFragment boardFragment = BoardFragment.newInstance();
    public Bundle bundle;


    // Adapter에 추가된 게이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public ListViewAdapter() {
    }
    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mypage_item, parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        titleTextView = convertView.findViewById(R.id.title);
        iconImageView = convertView.findViewById(R.id.icon);

        ListViewItem listViewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        iconImageView.setImageResource(listViewItem.getIcon());
        fragmentManager = ((AppCompatActivity) parent.getContext()).getSupportFragmentManager();
        LinearLayout cmdArea = convertView.findViewById(R.id.cmdArea);
        cmdArea.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (position) {
                    case 0 :
                        fragmentTransaction.replace(R.id.frame_layout, infoChangeFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;

                    case 1 :
                        fragmentTransaction.replace(R.id.frame_layout, bookmarkFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;

                    case 2:
                        fragmentTransaction.replace(R.id.frame_layout, historyFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;

                    case 3:
                        fragmentTransaction.replace(R.id.frame_layout, reviewFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;

                    case 4:
                        fragmentTransaction.replace(R.id.frame_layout, settingFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;

                    case 5:
                        boardFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.frame_layout, boardFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;
                }
            }
        });

        return convertView;
    }

    //지정한 위치(position)이 있는 데이터와 관계된 아잉템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(String title, int icon) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setIcon(icon);

        listViewItemList.add(item);
    }
}
