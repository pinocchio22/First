package kr.ac.daejin.restaurantnews.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import kr.ac.daejin.restaurantnews.R;
import kr.ac.daejin.restaurantnews.databinding.ActivityJoinBinding;

public class JoinActivity extends AppCompatActivity {
    private ActivityJoinBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private static final String TAG = "현재";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join);
        binding.setActivity(this);
        mAuth = FirebaseAuth.getInstance();

        binding.birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCal = Calendar.getInstance();
                DatePickerDialog picker = new DatePickerDialog(JoinActivity.this, R.style.MySpinnerDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
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
                    Toast.makeText(getApplicationContext(), "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                } else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", password)) {
                    Toast.makeText(getApplicationContext(), "비밀번호 양식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                } else if(!password.equals(passwordCheck)){
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT);
                } else if(!Patterns.PHONE.matcher(phone).matches()) {
                    Toast.makeText(getApplicationContext(), "올바른 핸드폰 번호가 아닙니다.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                } else if(birth.length() == 0){
                    Toast.makeText(getApplicationContext(), "생년월일을 적어주세요.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "onComplete: " + email);
                                if (task.isSuccessful()) {
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("phone", phone);
                                    userData.put("name", name);
                                    userData.put("gender", gender);
                                    userData.put("birth", birth);
                                    userData.put("isOwner", false);
                                    userData.put("favorite", new ArrayList<String>());

                                    db.collection("users").document(user.getUid()).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + user.getUid());
                                            updateUI(user);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                                } else {
//                                    Toast.makeText(getApplicationContext(), "failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

    }

    private void updateUI(FirebaseUser account) {
        if(account != null) {
            Toast.makeText(this, "성공적으로 회원가입 되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}