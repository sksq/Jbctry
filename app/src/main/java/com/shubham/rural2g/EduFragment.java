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
public class EduFragment extends ListFragment {

    public static final String TAG = EnterFragment.class.getSimpleName();
    protected List<ParseObject> mTags;

    ParseQuery<ParseObject> queryTags;
    ParseUser userObject;

    String[] allTagsArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edu, container, false);
//        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        userObject = ParseUser.getCurrentUser();

        queryTags = ParseQuery.getQuery("tags");
        queryTags.whereEqualTo("categoryCode", 0);
//        queryTags.fromLocalDatastore();
        queryTags.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tags, ParseException e) {
                if (e == null) {
                    mTags = tags;
                    allTagsArray = new String[mTags.size()];

//                    Log.e(TAG, tags.size()+"");
                    int i = 0;
                    for (ParseObject tag : mTags) {
                        allTagsArray[i] = tag.getString("tagName");
                        i++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_multiple_choice,
                            allTagsArray);
                    setListAdapter(adapter);

                    addCheckmarks();

                } else {

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

//            Log.d("TAG","eldh "+position);
            userObject.addAllUnique("Tags", Arrays.asList(allTagsArray[position]));
            userObject.saveInBackground();
        }
        else {

            ArrayList<String> testStringArrayList = (ArrayList<String>)userObject.get("Tags");
            testStringArrayList.remove(allTagsArray[position]);
            userObject.put("Tags",testStringArrayList);
            userObject.saveInBackground();

        }
    }

    private void addCheckmarks() {

        ArrayList<String> testStringArrayList = (ArrayList<String>)userObject.get("Tags");
        Log.d("qw", "TAG");

        ParseObject arr =  userObject.getParseObject("Tags");

        for (int i = 0; i < testStringArrayList.size(); i++)
            for (int j = 0; j < allTagsArray.length; j++) {
                if (testStringArrayList.get(i).equals(allTagsArray[j]))
                    getListView().setItemChecked(j, true);
            }
    }
}
