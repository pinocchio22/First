package kr.ac.daejin.restaurantnews.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kr.ac.daejin.restaurantnews.databinding.ItemSearchResultBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    public List<PlaceData> placeDataList;
    private FirebaseFirestore db;

    public SearchAdapter() {
        db = FirebaseFirestore.getInstance();
        placeDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.searchResultName.setText(placeDataList.get(position).getName());
        holder.binding.searchResultAddress.setText(placeDataList.get(position).getNewAddress());
    }

    @Override
    public int getItemCount() {
        return placeDataList.size();
    }

    public void actionSearch (final Context context, final String searchWord, final String option) {
        placeDataList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(context, android.R.style.Theme_Material_Dialog_Alert);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        db.collection("restaurant").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("task", "onComplete: " + task.getResult().size());
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        if(documentSnapshot.get(option).toString() == null) {
                            continue;
                        }
                        if(documentSnapshot.get(option).toString().contains(searchWord)) {
                            Log.d("SearchAdapter", "if문 " + documentSnapshot.get(option).toString());
                            Log.d("SearchAdapter", "if문 id" + documentSnapshot.getId());
                            PlaceData placeData = documentSnapshot.toObject(PlaceData.class);
                            placeDataList.add(placeData);
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    Log.d("SearchAdapter", "Error getting documents", task.getException());
                }
                progressDialog.dismiss();
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSearchResultBinding binding;
        public MyViewHolder(@NonNull ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}