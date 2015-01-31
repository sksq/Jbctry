package com.shubham.rural2g;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shubham on 20-01-2015.
 */
public class WeatherFragment extends ListFragment {

    public static final String TAG = EnterFragment.class.getSimpleName();
    protected List<ParseObject> mTags;

    ParseQuery<ParseObject> queryTags;
    ParseUser userObject;

    ConnectionDetector cd;  //References object which determines whether device is connected to Internet

    String[] allTagsArray;

    String query; //@Gopal

    public WeatherFragment(String query) {
        this.query = query;
        Log.d("TAG", "Inside NewsFragment Constructor and query is :" + query + ".");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userObject = ParseUser.getCurrentUser();

        queryTags = ParseQuery.getQuery("tags");
        queryTags.whereEqualTo("categoryCode", 3);

        cd = new ConnectionDetector(getActivity());
        if (cd.isConnectedToInternet()) {
            Log.d("TAG", "NET CONNECTED");
            queryTags.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> tags, ParseException e) {
                    if (e == null) {

                        unpinAndRepin(tags);

                        toDo(tags);
                    } else {
                        Log.d("TAG", "Inside else when net IS connected and ParseException is not null");
                    }
                }
            });
        } // end of if statement which runs when net IS connected

        else {
            Log.d("TAG", "Net not connected");

            queryTags.fromLocalDatastore();
            queryTags.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> tags, ParseException e) {
                    if (e == null) {
                        toDo(tags);

                    } else {

                        Log.d("TAG", "Inside else when net is NOT connected");

                    }


                }
            });
        } //End of else statement which is executed when net is not connected



    }

    private void toDo(List<ParseObject> tags) {
        mTags = tags; //mTags now refers the whole "tags" table
        allTagsArray = new String[mTags.size()];

        //Following 5 lines extracts all the tag names from "tags" table and stores
        // all the tag Names in allTagsArray in string format
        int i = 0;
        for (ParseObject tag : mTags) {
            allTagsArray[i] = tag.getString("tagName");
            i++;
        }

        KnuthMorrisPratt test = new KnuthMorrisPratt(query);
        ArrayList<String> result = test.search(allTagsArray);
        String[] allTagsArrayDisplay = new String[result.size()];
        allTagsArrayDisplay = result.toArray(allTagsArrayDisplay);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getListView().getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                allTagsArrayDisplay);
        setListAdapter(adapter);

        addCheckMarks();
    }


    private void unpinAndRepin(final List<ParseObject> tags) {

        Log.d("TAG", "In unpinAndRepin");
        ParseObject.unpinAllInBackground(tags, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                ParseObject.pinAllInBackground(tags); // This will pin all the tags from the "tags" table in the background
            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (getListView().isItemChecked(position)) {

//            Log.d("TAG","eldh "+position);
            userObject.addAllUnique("Tags", Arrays.asList(allTagsArray[position]));
            userObject.saveEventually();
        } else {

            ArrayList<String> testStringArrayList = (ArrayList<String>) userObject.get("Tags");
            testStringArrayList.remove(allTagsArray[position]);
            userObject.put("Tags", testStringArrayList);
            userObject.saveEventually();

        }
    }

    private void addCheckMarks() {

        ArrayList<String> testStringArrayList = (ArrayList<String>) userObject.get("Tags");
        Log.d("qw", "TAG");

        ParseObject arr = userObject.getParseObject("Tags");

        for (int i = 0; i < testStringArrayList.size(); i++)
            for (int j = 0; j < allTagsArray.length; j++) {
                if (testStringArrayList.get(i).equals(allTagsArray[j]))
                    getListView().setItemChecked(j, true);
            }
    }
}
