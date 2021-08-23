package com.example.cookbook.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookbook.Adapters.RecipeListAdapter;
import com.example.cookbook.Models.RecipeInfo;
import com.example.cookbook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class RecentlyViewedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView1;
    FirebaseDatabase db1;
    DatabaseReference root1;
    private RecipeListAdapter adapter1;
    private ArrayList<RecipeInfo> list1;
    private RecipeInfo model1;
    private SwipeRefreshLayout swipeRefreshLayout;


    public RecentlyViewedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recently_viewed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.RecentlyViewRecipe);

        recyclerView1 = view.findViewById(R.id.recentRecyclerView);
        db1 = FirebaseDatabase.getInstance();

        swipeRefreshLayout = view.findViewById(R.id.recentSwipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(this);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        list1 = new ArrayList<>();
        adapter1 = new RecipeListAdapter(getContext(),list1);

        getRecentRecipes();


    }

    private void getRecentRecipes() {

        root1 = db1.getReference().child("Recent Viewed Item").child(FirebaseAuth.getInstance().getUid());

        root1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //for (DataSnapshot ds: dataSnapshot.getChildren()){
                    model1 = dataSnapshot.getValue(RecipeInfo.class);
                    list1.add(model1);
                    //}

                }
                recyclerView1.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRefresh() {

        list1.clear();
        adapter1.notifyDataSetChanged();
        getRecentRecipes();

    }
}
