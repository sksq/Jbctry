package com.shubham.rural2g;

import android.app.AlertDialog;
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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shubham on 20-01-2015.
 */
public class EnterFragment extends ListFragment {

    public static final String TAG = EnterFragment.class.getSimpleName();
    protected List<ParseObject> mTags;

    ParseQuery<ParseObject> queryTags;
    ParseUser userObject;

    String[] allTagsArray;

//<<<<<<< HEAD
    final String LABEL = "label";
//=======
    String query; //@Gopal

    public EnterFragment(String query) {
        this.query = query;
        Log.d("TAG", "Inside EnterFragment Constructor and query is :"+ query + ".");
    }
//>>>>>>> 8dfced9f4bbc30fe030077d4b62d30550f126a1f

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enter, container, false);
//        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        userObject = ParseUser.getCurrentUser();

        queryTags = ParseQuery.getQuery("tags");

        // Query for new results from the network.
//        queryTags.findInBackground(new FindCallback<ParseObject>() {
//            public void done(final List<ParseObject> scores, ParseException e) {
//                // Remove the previously cached results.
//                ParseObject.unpinAllInBackground("", new DeleteCallback() {
//                    public void done(ParseException e) {
//                        // Cache the new results.
//                        ParseObject.pinAllInBackground("", scores);
//                    }
//                });
//            }
//        });

        queryTags.whereEqualTo("categoryCode", 1);
        queryTags.fromLocalDatastore();
        queryTags.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> tags, ParseException e) {

                if (e == null) {
                    ParseObject.unpinAllInBackground(LABEL, new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseObject.pinAllInBackground(LABEL, tags);
                        }
                    });

                    mTags = tags;
                    allTagsArray = new String[mTags.size()];

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

                } else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (getListView().isItemChecked(position)) {

            queryTags.fromLocalDatastore();
            userObject.addAllUnique("Tags", Arrays.asList(allTagsArray[position]));
            userObject.pinInBackground();

        } else {

            queryTags.fromLocalDatastore();
            ArrayList<String> testStringArrayList = (ArrayList<String>) userObject.get("Tags");
            testStringArrayList.remove(allTagsArray[position]);
            userObject.put("Tags", testStringArrayList);
            userObject.pinInBackground();

        }
    }

    private void addCheckMarks() {

        queryTags.fromLocalDatastore();
        ArrayList<String> testStringArrayList = (ArrayList<String>) userObject.get("Tags");

        ParseObject arr = userObject.getParseObject("Tags");

        for (int i = 0; i < testStringArrayList.size(); i++)
            for (int j = 0; j < allTagsArray.length; j++) {
                if (testStringArrayList.get(i).equals(allTagsArray[j]))
                    getListView().setItemChecked(j, true);
            }
    }
}
