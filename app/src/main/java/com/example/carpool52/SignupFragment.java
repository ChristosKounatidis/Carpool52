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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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

    private Button buttonSignup;
    private Button button2;
    private EditText Email;
    private EditText Password;
    private EditText Name;
    FirebaseFirestore db;
    FirebaseAuth fAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        Email = view.findViewById(R.id.editEmailAddressSign);
        Name = view.findViewById(R.id.editName);
        Password = view.findViewById(R.id.editPasswordSign);
        buttonSignup = view.findViewById(R.id.checkSignup);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Var_email = Email.getText().toString().trim();
                String Var_password = Password.getText().toString().trim();
                String Var_name = Name.getText().toString().trim();
                //
                if(TextUtils.isEmpty(Var_email)){
                    Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Var_password)){
                    Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if(Var_password.length() < 6){
                    Toast.makeText(getActivity(), "Password Must be >= 6 Characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(Var_name)){
                    Toast.makeText(getActivity(), "Name is required", Toast.LENGTH_LONG).show();
                    return;
                }
                //Create authentication instance
                fAuth.createUserWithEmailAndPassword(Var_email,Var_password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_LONG).show();
                    }
                });

                // Retrieve the current maximum ID
                DocumentReference countersDocRef = db.collection("Counters").document("users");
                countersDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Get the current maximum ID
                        int currentMaxId = documentSnapshot.getLong("maxId").intValue();
                        int newId = currentMaxId + 1;
                        //create USER
                        try {
                            Users user = new Users(newId,Var_name,Var_email,0,0);
                            MainActivity.db.collection("Users").
                            document(""+newId).
                            set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    countersDocRef.update("maxId", newId);//UPDATE w/ the new max ID
                                    NavDirections action = SignupFragmentDirections.actionSignupFragmentToLoginFragment(); //init to velaki
                                    Navigation.findNavController(view).navigate(action); //allagh fragment NEXT STEP
                                    Toast.makeText(getActivity(), "Sign Up Complete", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        catch (Exception e) {
                            String message = e.getMessage();
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        button2 = view.findViewById(R.id.gotoLogin);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = SignupFragmentDirections.actionSignupFragmentToLoginFragment(); //init to velaki
                Navigation.findNavController(view).navigate(action); //allagh fragment
            }
        });

        return view;
    }
}