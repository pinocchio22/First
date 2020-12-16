package kr.ac.daejin.restaurantnews.fragments.main;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.adapter.NotificationAdapter;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentNotificationBinding;
import kr.ac.daejin.restaurantnews.fragments.preference.BoardFragment;
import kr.ac.daejin.restaurantnews.model.PlaceData;
import kr.ac.daejin.restaurantnews.model.Post;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding> {
    private NotificationAdapter notificationAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<String> favorites;
    private List<Post> posts;
    private BoardFragment boardFragment = BoardFragment.newInstance();



    @Override
    public int getLayoutRes() {
        return R.layout.fragment_notification;
    }

    public static NotificationFragment newInstance() {

        Bundle args = new Bundle();

        NotificationFragment fragment = new NotificationFragment();
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
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();


        final ProgressDialog progressDialog = new ProgressDialog(requireContext(), android.R.style.Theme_Material_Dialog_Alert);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                   favorites = (ArrayList<String>) task.getResult().get("favorite");
                   if(favorites.size() != 0) {
                       db.collection("posts")
                               .whereIn("publisher", favorites)
                               .orderBy("timestamp", Query.Direction.DESCENDING)
                               .get()
                               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                       if(task.isSuccessful()) {
                                           for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                               Post post = snapshot.toObject(Post.class);
                                               posts.add(post);
                                           }

                                       } else {
                                           task.getException().printStackTrace();
                                       }
                                       notificationAdapter = new NotificationAdapter(posts);
                                       binding.notiRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                                       binding.notiRecyclerView.setAdapter(notificationAdapter);

                                   }
                               });
                   }
                   progressDialog.dismiss();
                   showToast("알림이 없습니다.");
                }
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        binding.notiRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    final ProgressDialog progressDialog = new ProgressDialog(requireContext(), android.R.style.Theme_Material_Dialog_Alert);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    db.collection("restaurant").whereEqualTo("managementNum", posts.get(position).getPublisher()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                PlaceData placeData = task.getResult().toObjects(PlaceData.class).get(0);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("PlaceData", placeData);
                                boardFragment.setArguments(bundle);
                                progressDialog.dismiss();
                                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, boardFragment).addToBackStack(null).commitAllowingStateLoss();
                            }
                        }
                    });
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