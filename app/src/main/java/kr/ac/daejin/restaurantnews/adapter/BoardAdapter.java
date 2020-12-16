package kr.ac.daejin.restaurantnews.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.databinding.ItemFragmentBoardBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;
import kr.ac.daejin.restaurantnews.model.Post;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.MyViewHolder> {
    public List<Post> posts;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isLike;
    private List<String> documentsPath;

    public BoardAdapter(Context context, final PlaceData placeData) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();
        documentsPath = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        db.collection("posts").
                whereEqualTo("publisher", placeData.getManagementNum()).
                orderBy("timestamp", Query.Direction.DESCENDING).
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        documentsPath.add(document.getId());
                        posts.add(post);
                    }
                    notifyDataSetChanged();
                } else {
                    task.getException().printStackTrace();
                }
                progressDialog.dismiss();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFragmentBoardBinding binding = ItemFragmentBoardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        String date = fmt.format(posts.get(position).getTimestamp().toDate());
        if(posts.get(position).getLike() != null) {
            isLike = posts.get(position).getLike().contains(mAuth.getUid());
        } else {
            isLike = false;
        }
        if(isLike) {
            holder.binding.icon1.setImageResource(R.drawable.rice_yellow);
        }
        holder.binding.boardItemTitle.setText(posts.get(position).getTitle());
        holder.binding.boardContent.setText(posts.get(position).getContent());
        holder.binding.boardItemTime.setText(posts.get(position).getTimestamp().toString());
        holder.binding.boardItemTime.setText(date);

        holder.binding.icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLike) {
                    posts.get(position).getLike().add(mAuth.getUid());
                    db.collection("posts").document(documentsPath.get(position)).update("like", posts.get(position).getLike()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                isLike = true;
                                holder.binding.icon1.setImageResource(R.drawable.rice_yellow);
                            } else {
                                task.getException().printStackTrace();
                            }
                        }
                    });
                } else {
                    posts.get(position).getLike().remove(mAuth.getUid());
                    db.collection("posts").document(documentsPath.get(position)).update("like", posts.get(position).getLike()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                isLike = false;
                                holder.binding.icon1.setImageResource(R.drawable.rice);
                            } else {
                                task.getException().printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemFragmentBoardBinding binding;
        public MyViewHolder(@NonNull ItemFragmentBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
