package com.example.carpool52;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Button loginBtn;
    private Button signupBtn;
    private EditText email;
    private EditText password;
    FirebaseAuth fAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        email = view.findViewById(R.id.editEmailAddressSign);
        password = view.findViewById(R.id.editPasswordSign);
        fAuth = FirebaseAuth.getInstance();

        loginBtn = view.findViewById(R.id.checkLoginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Var_email = email.getText().toString().trim();
                String Var_password = password.getText().toString().trim();

                if(TextUtils.isEmpty(Var_email)){
                    Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Var_password)){
                    Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_LONG).show();
                    return;
                }

                fAuth.signInWithEmailAndPassword(Var_email, Var_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_LONG).show();
                            NavDirections action1 = LoginFragmentDirections.actionLoginFragmentToListFragment(); //init to velaki
                            Navigation.findNavController(view).navigate(action1); //allagh fragment
                        }
                        else{
                            Toast.makeText(getActivity(), "Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        signupBtn = view.findViewById(R.id.gotoSignupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action2 = LoginFragmentDirections.actionLoginFragmentToSignupFragment(); //init to velaki
                Navigation.findNavController(view).navigate(action2); //allagh fragment
            }
        });

        return view;
    }
}