package kr.ac.daejin.restaurantnews.fragments.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.activities.MainActivity;
import kr.ac.daejin.restaurantnews.adapter.ListViewAdapter;
import kr.ac.daejin.restaurantnews.base.BaseFragment;
import kr.ac.daejin.restaurantnews.databinding.FragmentPreferencesBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;

public class PreferencesFragment extends BaseFragment<FragmentPreferencesBinding> {

    private ListViewAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isOwner;
    private String[] managementNum = new String[1];
    private PlaceData placeData;

    public PreferencesFragment() {
        // Required empty public constructor
    }

    public static PreferencesFragment newInstance() {

        Bundle args = new Bundle();

        PreferencesFragment fragment = new PreferencesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_preferences;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding.logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(requireContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        //Adapter 생성
        adapter = new ListViewAdapter();
        //리스트뷰 참조 및 Adapter 달기
        binding.listView.setAdapter(adapter);

        final ProgressDialog progressDialog = new ProgressDialog(requireContext(), android.R.style.Theme_Material_Dialog_Alert);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    isOwner = (boolean) task.getResult().get("isOwner");
                    adapter.addItem("내정보", R.drawable.ic_person_black_48dp);
                    adapter.addItem("단골매장", R.drawable.ic_place_black_48dp);
                    adapter.addItem("히스토리", R.drawable.ic_nfc_black_48dp);
                    adapter.addItem("후기", R.drawable.ic_edit_black_48dp);
                    adapter.addItem("설정", R.drawable.ic_tune_black_48dp);
                    if(isOwner) {
                        managementNum[0] = task.getResult().get("restaurantManagementNum").toString();
                    }
                    db.collection("restaurant").whereEqualTo("managementNum", managementNum[0]).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(isOwner){
                                placeData = task.getResult().toObjects(PlaceData.class).get(0);
                                adapter.bundle = new Bundle();
                                adapter.bundle.putParcelable("PlaceData", placeData);
                                adapter.addItem("사장님페이지", R.drawable.ic_home_black_48dp);
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        }
                    });
                }
            }
        });
    }
}