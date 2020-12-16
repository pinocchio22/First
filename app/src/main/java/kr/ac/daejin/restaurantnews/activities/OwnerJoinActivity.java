package kr.ac.daejin.restaurantnews.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.base.BaseActivity;
import kr.ac.daejin.restaurantnews.databinding.ActivityOwnerJoinBinding;
import kr.ac.daejin.restaurantnews.model.PlaceData;

public class OwnerJoinActivity extends BaseActivity<ActivityOwnerJoinBinding> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private static final String TAG = "Login";
    private Map<String, Object> userData;
    private PlaceData placeData;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_owner_join;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding.birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCal = Calendar.getInstance();
                DatePickerDialog picker = new DatePickerDialog(OwnerJoinActivity.this, R.style.MySpinnerDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        binding.birth.setText(i + "-" + (i1 + 1) + "-" + i2);
                    }
                }, myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal.get(Calendar.DAY_OF_MONTH));
                if (!binding.birth.getText().toString().equals("")) {
                    String[] birthStr = binding.birth.getText().toString().split("-");
                    int year = Integer.parseInt(birthStr[0]);
                    int month = Integer.parseInt(birthStr[1]);
                    int day = Integer.parseInt(birthStr[2]);
                    picker.updateDate(year, month-1, day);
                }
                picker.show();
            }
        });

        binding.restaurantId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OwnerJoinActivity.this, SearchActivity.class),1);
            }
        });

        binding.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = binding.email.getText().toString();
                final String password = binding.pw.getText().toString();
                final String passwordCheck = binding.pwCheck.getText().toString();
                final String name = binding.name.getText().toString();
                final String phone = binding.phone.getText().toString();
                final String birth = binding.birth.getText().toString();
                RadioButton button = findViewById(binding.genderGroup.getCheckedRadioButtonId());
                final String gender = button.getText().toString();


                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showToast("이메일 형식이 아닙니다.");
                    return;
                } else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", password)) {
                    showToast("비밀번호 양식을 지켜주세요.");
                    return;
                } else if(!password.equals(passwordCheck)){
                    showToast("비밀번호가 다릅니다.");
                    return;
                } else if(!Patterns.PHONE.matcher(phone).matches()) {
                    showToast("올바른 핸드폰 번호가 아닙니다.\n '-' 제외하고 적어주세요.");
                    return;
                } else if(birth.length() == 0){
                    showToast("생년월일을 적어주세요.");
                    return;
                } else if(binding.restaurantId.getText().toString().replace(" ", "").equals("")) {
                    showToast("가게를 선택해주세요.");
                    return;
                }

                userData = new HashMap<>();
                userData.put("phone", phone);
                userData.put("name", name);
                userData.put("gender", gender);
                userData.put("birth", birth);
                userData.put("isOwner", true);
                userData.put("favorite", new ArrayList<>());
                userData.put("restaurantManagementNum", placeData.getManagementNum());

                final String[] path = new String[1];
                Log.d(TAG, "onComplete: " + placeData.getManagementNum());
                db.collection("restaurant").whereEqualTo("managementNum", placeData.getManagementNum()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                path[0] = document.getId();
                            }
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException().getMessage());
                        }
                    }
                });

                createUserEmail(email, password, userData, path);
            }
        });
    }
    public void createUserEmail(final String email, final String password, final Map<String, Object> userData, final String[] path){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(OwnerJoinActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "onComplete: " + email);
                            if (task.isSuccessful()) {
                                final FirebaseUser user = mAuth.getCurrentUser();
//                                        .getResult().getDocuments().get(0).getId();
                                Log.d(TAG, "onComplete: " + path[0]);
                                db.collection("restaurant").document(path[0]).update("isJoin", true);
                                db.collection("users")
                                        .document(user.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        updateUI(user);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                        showToast("DB problem");
                                    }
                                });

                            } else {
                                updateUI(null);
                            }
                        }
                    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 || requestCode==RESULT_OK){
            if(data.getParcelableExtra("select") != null) {
                placeData = data.getParcelableExtra("select");
                binding.restaurantId.setText(placeData.getManagementNum());
            }
        }
    }

    private void updateUI(FirebaseUser account) {
        if(account != null) {
            showToast("성공적으로 회원가입이 되었습니다. 다시 로그인해주세요.");
            mAuth.signOut();
            onBackPressed();
        } else {
            showToast("회원가입에 실패했습니다.");
        }
    }
}
