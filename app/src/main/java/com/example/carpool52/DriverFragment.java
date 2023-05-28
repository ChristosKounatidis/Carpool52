package com.example.carpool52;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverFragment newInstance(String param1, String param2) {
        DriverFragment fragment = new DriverFragment();
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

    EditText descriptionTxt;
    Button postBtn;
    FirebaseFirestore db;
    FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        descriptionTxt = view.findViewById(R.id.descriptionText);
        postBtn = view.findViewById(R.id.postBtn);
        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Var_description = descriptionTxt.getText().toString().trim();
                String Var_email = fAuth.getCurrentUser().getEmail().toString().trim();
                Query query = db.collection("Users").whereEqualTo("email",Var_email);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        String Var_uid = querySnapshot.getDocuments().get(0).getId();
                        DocumentReference countersDocRef = db.collection("Counters").document("matches");
                        countersDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // Get the current maximum ID
                                int currentMaxId = documentSnapshot.getLong("maxId").intValue();
                                int newId = currentMaxId + 1;
                                try {
                                    Matches matches = new Matches(newId,Var_description,0,db.document("/Users/"+Var_uid),null,null,null);

                                    MainActivity.db.collection("Matches").
                                            document(""+newId).
                                            set(matches).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    countersDocRef.update("maxId", newId);//UPDATE w/ the new max ID
                                                    Toast.makeText(getActivity(), "Ad Posted", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                }catch (Exception e){
                                    String message = e.getMessage();
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    };
                });
            }
        });

        return view;
    }
}