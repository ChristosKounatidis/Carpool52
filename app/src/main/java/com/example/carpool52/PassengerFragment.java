package com.example.carpool52;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerFragment newInstance(String param1, String param2) {
        PassengerFragment fragment = new PassengerFragment();
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

    private LinearLayout containerLayout;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passenger, container, false);
        containerLayout = view.findViewById(R.id.container_layout);
        db = FirebaseFirestore.getInstance();

        int spacingBetweenSets = getResources().getDimensionPixelSize(R.dimen.spacing_between_sets);
        containerLayout.setPadding(0, spacingBetweenSets, 0, 0);

        CollectionReference matchesRef = db.collection("Matches");
        Query query = matchesRef.whereEqualTo("status", 0);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String description = document.getString("description");

                        DocumentReference userRef = (DocumentReference) document.get("u1");
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                if (userTask.isSuccessful()) {
                                    DocumentSnapshot userDoc = userTask.getResult();
                                    if (userDoc.exists()) {
                                        String email = userDoc.getString("email");
                                        String name = userDoc.getString("name");
                                        Long rating = userDoc.getLong("rating");

                                        LinearLayout setLayout = new LinearLayout(getContext());
                                        setLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        setLayout.setOrientation(LinearLayout.VERTICAL);

                                        GradientDrawable borderDrawable = new GradientDrawable();
                                        int borderColor = ContextCompat.getColor(getContext(), R.color.border_color);
                                        borderDrawable.setStroke(2, borderColor);
                                        setLayout.setBackground(borderDrawable);

                                        TextView textViewEmail = new TextView(getContext());
                                        textViewEmail.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewEmail.setText("Email: " + email);

                                        TextView textViewName = new TextView(getContext());
                                        textViewName.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewName.setText("Name: " + name);

                                        TextView textViewRating = new TextView(getContext());
                                        textViewRating.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewRating.setText("Rating: " + rating);

                                        TextView textViewDescription = new TextView(getContext());
                                        textViewDescription.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        textViewDescription.setText("Description: " + description);

                                        setLayout.addView(textViewEmail);
                                        setLayout.addView(textViewName);
                                        setLayout.addView(textViewRating);
                                        setLayout.addView(textViewDescription);

                                        containerLayout.addView(setLayout);
                                    }
                                } else {
                                    Log.d("Firestore", "Error getting user document: ", userTask.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });

        return view;
    }
}