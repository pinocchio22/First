package kr.ac.daejin.restaurantnews.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.model.ListViewItem;
import kr.ac.daejin.restaurantnews.fragments.setting.DropUserFragment;
import kr.ac.daejin.restaurantnews.fragments.setting.EmailFragment;
import kr.ac.daejin.restaurantnews.fragments.setting.FaqFragment;
import kr.ac.daejin.restaurantnews.fragments.setting.RatingFragment;


public class SettingAdapter extends BaseAdapter implements DialogInterface.OnCancelListener {

    private ImageView iconImageView;
    private TextView titleTextView;
    private FragmentManager fragmentManager;
    private FaqFragment faqFragment = new FaqFragment();
    private EmailFragment emailFragment = new EmailFragment();
    private RatingFragment ratingFragment = new RatingFragment();
    private DropUserFragment dropUserFragment = new DropUserFragment();

    private Context context;
    final String[] words = new String[] {"알림 끄기", "알림 켜기"};

    // Adapter에 추가된 게이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public SettingAdapter(Context context) {
        this.context = context;
    }
    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

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
        LinearLayout cmdArea = (LinearLayout) convertView.findViewById(R.id.cmdArea);
        cmdArea.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (position) {
                    case 0 :
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder
                                .setTitle("알림 설정")
                                .setSingleChoiceItems(words, -1,new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "word:" + words,Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setPositiveButton("저장",null).setNegativeButton("닫기", null).show();
                        break;

                    case 1 :
                        fragmentTransaction.replace(R.id.frame_layout, faqFragment).addToBackStack("FaqFragment").commitAllowingStateLoss();
                        break;

                    case 2:
                        fragmentTransaction.replace(R.id.frame_layout, emailFragment).addToBackStack("EmailFragment").commitAllowingStateLoss();
                        break;

                    case 3:
                        fragmentTransaction.replace(R.id.frame_layout, ratingFragment).addToBackStack("AppReviewFragment").commitAllowingStateLoss();
                        break;

                    case 4:
                        fragmentTransaction.replace(R.id.frame_layout, dropUserFragment).addToBackStack("DropUserFragment").commitAllowingStateLoss();
                        break;

                    case 5:
                        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);
                        //일반 다이얼로그
                        alertDialogBuilder1
                                .setMessage("version:xxx.xxx.xxx")
                                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder1.create();
                        alertDialog.show();
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

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
