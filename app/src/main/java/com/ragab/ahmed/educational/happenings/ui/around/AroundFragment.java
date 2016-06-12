package com.ragab.ahmed.educational.happenings.ui.around;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.ui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AroundFragment extends  Fragment{

    private MainActivity mainActivity;

    private int SECTION_NUMBER = 1;

    public AroundFragment() {
        // Required empty public constructor
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;
        mainActivity.onSectionAttached(SECTION_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_around, container, false);

        RecyclerView eventsCards = (RecyclerView)view.findViewById(R.id.around_recycler_view);
        eventsCards.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        eventsCards.setLayoutManager(layoutManager);

        AroundAdapter adapter = new AroundAdapter(fillEvents(), getActivity());
        eventsCards.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.around, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    // For debugging only

    public Event[] fillEvents()
    {
        Event[] events = {new Event("A Huge fire", "Some first just happened and it's bal bla abab", "10 minutes ago", "John Doe", 102.1, 20.3),
                new Event("A Tremendous fire", "Some first just happened and it's bal bla abab", "1 hour ago", "Dwight Schrute", 102.1, 20.3),
                new Event("A Huge fire", "Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab ", "10 minutes ago", "John Doe", 28, 30),
                new Event("A Huge fire", "Some first just happened and it's bal bla abab", "20 minutes ago", "James Pam", 102.1, 20.3),
                new Event("A Magnificient fire", "Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab ", "Now", "John Doe", 102.1, 30.3),
                new Event("A Huge fire", "Some first just happened and it's bal bla abab", "10 minutes ago", "John Doe", 120.1, 20.3),
                new Event("An EVENT EVENT", "Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab Some first just happened and it's bal bla abab ", "10 minutes ago", "John Doe", 102.1, 50.3),
                new Event("A Marvelous thing", "Some first just happened and it's bal bla abab", "10 minutes ago", "Ahmed Ragab", 102.1, 20.3),
                new Event("A Huge fire", "Some first just happened and it's bal bla abab", "seconds ago", "Mostafa Mohammed", 102.1, 20.3)};

        return events;
    }
}
