package kr.ac.daejin.restaurantnews.fragments.preference;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.adapter.BoardAdapter;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentBoardBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;
import kr.ac.daejin.restaurantnews.model.Post;

public class BoardFragment extends BaseFragment<FragmentBoardBinding> {
    private BoardAdapter boardAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PlaceData placeData;
    private List<String> favorites;
    private boolean isFavorite;
    private String managementNum;
    private boolean isOwner;
    private WritePostFragment writePostFragment = WritePostFragment.newInstance();

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_board;
    }

    public static BoardFragment newInstance() {
        Bundle args = new Bundle();

        BoardFragment fragment = new BoardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        placeData = this.getArguments().getParcelable("PlaceData");

        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("board", "onComplete: " + mAuth.getUid());
                    favorites = (ArrayList<String>) task.getResult().get("favorite");
                    isOwner = (boolean) task.getResult().get("isOwner");
                    isFavorite = favorites.contains(placeData.getManagementNum());
                    if(isFavorite) {
                        binding.icon1.setImageResource(R.drawable.rice_yellow);
                    }
                    if(isOwner) {
                        managementNum = task.getResult().get("restaurantManagementNum").toString();
                        if (placeData.getManagementNum().equals(managementNum)) {
                            binding.btnWritePost.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    task.getException().printStackTrace();
                }
            }
        });
        binding.boardTitle.setText(placeData.getName());
        binding.boardCategory.setText(placeData.getCategory());
        binding.boardAddress.setText(placeData.getNewAddress());
        binding.boardPostNum.setText(String.valueOf(placeData.getPostNum()));

        if(placeData.getTel().equals("")){
            binding.boardTel.setText("등록된 전화번호가 없습니다.");
        } else {
            binding.boardTel.setText(placeData.getTel());
        }

        binding.icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorite) {
                    isFavorite = false;
                    binding.icon1.setImageResource(R.drawable.rice);
                    showToast("북마크가 해제되었습니다.");
                    favorites.remove(placeData.getManagementNum());
                    subscribeToTopicForPush(placeData.getManagementNum(), true);

                } else {
                    isFavorite = true;
                    binding.icon1.setImageResource(R.drawable.rice_yellow);
                    showToast("북마크가 설정되었습니다.");
                    if(!favorites.contains(placeData.getManagementNum())) {
                        favorites.add(placeData.getManagementNum());
                    }
                    subscribeToTopicForPush(placeData.getManagementNum(), false);
                }
                db.collection("users").document(mAuth.getUid()).update("favorite", favorites).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d("favorite db", "onComplete: Success");
                        } else {
                            task.getException().printStackTrace();
                        }
                    }
                });
            }
        });

        binding.btnWritePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("publisher", managementNum);
                writePostFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, writePostFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        boardAdapter = new BoardAdapter(requireContext(), placeData);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(boardAdapter);

        final GestureDetector gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        binding.recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    Post post = boardAdapter.posts.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", post);
                    // fragment 아니면 dialog 나오는 코드 나오도록 //
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

    private void subscribeToTopicForPush(final String publisher, boolean isSubscribed){
        if(!isSubscribed){
            FirebaseMessaging.getInstance().subscribeToTopic(publisher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = getString(R.string.msg_subscribed, publisher);
                    if(!task.isSuccessful()){
                        msg = getString(R.string.msg_subscribe_failed);
                    }
                    Log.d("subscribe", msg);
                }
            });
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(publisher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = getString(R.string.msg_unsubscribed, publisher);
                    if(!task.isSuccessful()){
                        msg = getString(R.string.msg_unsubscribe_failed);
                    }
                    Log.d("unsubscribe", msg);
                }
            });
        }
    }


}
