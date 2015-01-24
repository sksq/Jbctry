package com.shubham.rural2g;
import java.util.ArrayList;

/**
 * Created by Gopal on 24-01-2015.
 */
public class KnuthMorrisPratt {
    /** Failure array **/
    private int[] failure;
    private String pattern;
    /** Constructor **/
    public KnuthMorrisPratt(String pat)
    {
        pattern = pat;
        /** pre construct failure array for a pattern **/
        failure = new int[pat.length()];
        if(pat.length() != 0) {
            fail(pat);
            /** find match **/
        }

    }
    public ArrayList<String> search(String[] allTags) {
        ArrayList<String> result = new ArrayList<String>();
        for(String item: allTags) {
            if(posMatch(item.toLowerCase(), pattern.toLowerCase()) != -1) {
                result.add(item);
            }
        }
        return result;
    }
    /** Failure function for a pattern **/
    private void fail(String pat)
    {
        int n = pat.length();
        failure[0] = -1;
        for (int j = 1; j < n; j++)
        {
            int i = failure[j - 1];
            while ((pat.charAt(j) != pat.charAt(i + 1)) && i >= 0)
                i = failure[i];
            if (pat.charAt(j) == pat.charAt(i + 1))
                failure[j] = i + 1;
            else
                failure[j] = -1;
        }
    }
    /** Function to find match for a pattern **/
    public int posMatch(String text, String pat)
    {
        int i = 0, j = 0;
        int lens = text.length();
        int lenp = pat.length();
        while (i < lens && j < lenp)
        {
            if (text.charAt(i) == pat.charAt(j))
            {
                i++;
                j++;
            }
            else if (j == 0)
                i++;
            else
                j = failure[j - 1] + 1;
        }
        return ((j == lenp) ? (i - lenp) : -1);
    }}

