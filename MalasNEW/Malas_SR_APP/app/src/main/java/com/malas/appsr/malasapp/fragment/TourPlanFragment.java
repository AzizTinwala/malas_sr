package com.malas.appsr.malasapp.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.malas.appsr.malasapp.BeanClasses.TourPlanBean;
import com.malas.appsr.malasapp.R;
import com.malas.appsr.malasapp.adapter.TourPlanAdapter;
import com.malas.appsr.malasapp.adapter.TourWeekAdapter;
import com.malas.appsr.malasapp.serverconnection.BackgroundWork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TourPlanFragment extends Fragment implements TourWeekAdapter.OnClickListener ,TourPlanAdapter.OnClickListener{
    TextView header;
    ImageView back;
    RecyclerView weekView;
    String[] weeks = new String[]{"Week 1", "Week 2", "Week 3", "Week 4"};
    FloatingActionButton add;
    AlertDialog alertDialog;
    List<TourPlanBean> tpList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        tpList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_tour_plan, container, false);
        header = v.findViewById(R.id.tour_plan_header);
        back = v.findViewById(R.id.tour_back_space);
        weekView = v.findViewById(R.id.tour_week_view);
        add = v.findViewById(R.id.tour_add_plan);

        header.setText("Tour Plan");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        weekView.setLayoutManager(linearLayoutManager);
        weekView.setAdapter(new TourWeekAdapter(weeks, this));
        add.setOnClickListener(v1 -> initalizeTourPlan());
        return v;
    }

    @Override
    public void onClick(int position) {
        weekView.setAdapter(null);
        add.setVisibility(View.VISIBLE);
        header.setText(weeks[position]);

        getTourPlanWeekly(position + 1);
    }

    private void getTourPlanWeekly(int weekNo) {
        tpList.clear();

        BackgroundWork back = new BackgroundWork(getContext());

        back.execute("Get Weekly Tour Plan", String.format("%s", weekNo));
        back.getDailog().setOnDismissListener(it -> {
            JSONObject json;

            try {
                json = new JSONObject(back.getResult());
                if (json.getString("success").equals("true") && json.getInt("status code") == 200) {
                    JSONArray jsonArray = json.optJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j_obj = jsonArray.getJSONObject(i);
                            TourPlanBean tour = new TourPlanBean(j_obj.getString("tour_plan_id"), j_obj.getString("date"),
                                    j_obj.getString("work_type"), j_obj.getString("join_work"), j_obj.getString("town_from_name"),
                                    j_obj.getString("town_to_name"), j_obj.getString("distributor_name"), j_obj.getString("route_name"),
                                    j_obj.getString("updated_by_name"), j_obj.getString("updated_at"), j_obj.getInt("work_type_id")
                                    , j_obj.getInt("join_work_id"), j_obj.getInt("town_from"), j_obj.getInt("town_to"),
                                    j_obj.getInt("distributor_id"), j_obj.getInt("route_id"), j_obj.getInt("updated_by"));

                            tpList.add(tour);
                        }
                        weekView.setAdapter(new TourPlanAdapter(tpList, this));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveTourPlan() {
        tpList.clear();

        BackgroundWork back = new BackgroundWork(getContext());

        back.execute("Insert Tour Plan");
        back.getDailog().setOnDismissListener(it -> {
            JSONObject json;

            try {
                json = new JSONObject(back.getResult());
                if (json.getString("success").equals("true") && json.getInt("status code") == 200) {
                    JSONArray jsonArray = json.optJSONArray("data");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j_obj = jsonArray.getJSONObject(i);
                            TourPlanBean tour = new TourPlanBean(j_obj.getString("tour_plan_id"), j_obj.getString("date"),
                                    j_obj.getString("work_type"), j_obj.getString("join_work"), j_obj.getString("town_from_name"),
                                    j_obj.getString("town_to_name"), j_obj.getString("distributor_name"), j_obj.getString("route_name"),
                                    j_obj.getString("updated_by_name"), j_obj.getString("updated_at"), j_obj.getInt("work_type_id")
                                    , j_obj.getInt("join_work_id"), j_obj.getInt("town_from"), j_obj.getInt("town_to"),
                                    j_obj.getInt("distributor_id"), j_obj.getInt("route_id"), j_obj.getInt("updated_by"));

                            tpList.add(tour);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void initalizeTourPlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setCancelable(true);

        View addview = LayoutInflater.from(getContext()).inflate(R.layout.add_tour_plan_layout, null);
        ImageView backDaily = addview.findViewById(R.id.tour_back_daily);
        TextView date = addview.findViewById(R.id.tour_selected_date);
        AutoCompleteTextView workType = addview.findViewById(R.id.tour_work_type);
        AutoCompleteTextView workingWith = addview.findViewById(R.id.tour_working_with);
        AutoCompleteTextView townFrom = addview.findViewById(R.id.tour_town_from);
        AutoCompleteTextView townTo = addview.findViewById(R.id.tour_town_to);
        Button addEvent = addview.findViewById(R.id.tour_addEvent);

        TextInputLayout WorkType_Input = addview.findViewById(R.id.tour_input_work_type);
        TextInputLayout TownFrom_Input = addview.findViewById(R.id.tour_input_town_from);
        TextInputLayout TownTo_Input = addview.findViewById(R.id.tour_input_town_to);
        TextInputLayout WorkingWith_Input = addview.findViewById(R.id.tour_input_working_with);

        //back button
        backDaily.setOnClickListener(v -> alertDialog.dismiss());

        //save event
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTourPlan();
            }
        });


        //disable focus on Travel Mode
        workingWith.setFocusable(false);
        workingWith.setCursorVisible(false);

        //disable focus on Work Type
        workType.setFocusable(false);
        workType.setFocusable(false);

        builder.setView(addview);

        alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    public void onPlanClick(int position) {

    }

    @Override
    public void onPlanLongClick(int position) {

    }
}