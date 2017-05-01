package com.mindmesolo.mindme.CreateCampaign;

import java.util.ArrayList;

/**
 * Created by eNest on 6/28/2016.
 */
public interface IFragmentToActivity {
    void communicateToFragment2();

    void getAllContacts(ArrayList data);

    void getListData(ArrayList data, ArrayList names);

    void getInterestData(ArrayList data, ArrayList names);

    void getTagData(ArrayList data, ArrayList names);

    void getAllDateFilterContacts(ArrayList data);
}
