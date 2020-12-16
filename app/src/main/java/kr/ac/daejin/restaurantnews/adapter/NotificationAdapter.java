package kr.ac.daejin.restaurantnews.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import kr.ac.daejin.restaurantnews.databinding.FragmentNotificationItemBinding;
import kr.ac.daejin.restaurantnews.model.Post;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<Post> posts;

    public NotificationAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentNotificationItemBinding binding = FragmentNotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        String date = fmt.format(posts.get(position).getTimestamp().toDate());
        holder.binding.notiTitle.setText(posts.get(position).getTitle());
        holder.binding.notiName.setText(posts.get(position).getName());
        holder.binding.notiTime.setText(date);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        FragmentNotificationItemBinding binding;
        public MyViewHolder(@NonNull FragmentNotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
