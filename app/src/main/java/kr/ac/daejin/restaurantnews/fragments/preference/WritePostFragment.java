package kr.ac.daejin.restaurantnews.fragments.preference;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentWritePostBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;
import kr.ac.daejin.restaurantnews.model.Post;

public class WritePostFragment extends BaseFragment<FragmentWritePostBinding> {
    private FirebaseFirestore db;
    private String publisher;
    private String resName;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_write_post;
    }

    public static WritePostFragment newInstance() {
        Bundle args = new Bundle();

        WritePostFragment fragment = new WritePostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        publisher = getArguments().get("publisher").toString();

        db.collection("restaurant").whereEqualTo("managementNum", publisher).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                resName = task.getResult().toObjects(PlaceData.class).get(0).getName();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.postTitle.getText().toString();
                String content = binding.postContent.getText().toString();
                if (title.replace(" ", "").equals("")) {
                   showToast("제목을 작성해주세요.");
                   return;
                } else if (content.replace(" ", "").equals("")) {
                    showToast("내용을 작성해주세요.");
                    return;
                }

                Post post = new Post(publisher, resName, title, content, Timestamp.now(), new ArrayList<String>());

                db.collection("posts").document().set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("글 작성이 완료되었습니다.");
                        requireActivity().onBackPressed();
                        binding.postTitle.setText("");
                        binding.postContent.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("글 작성에 실패했습니다.\n잠시 후 시도해주세요.");
                    }
                });
            }
        });
    }

}
