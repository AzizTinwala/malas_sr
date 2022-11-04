package com.malas.appsr.malasapp;

/**
 * Created by Krishna on 9/19/2016.
 */
public class ExtraCode {
    /*  edtcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.industry_list_dialogbox);
                etserach = (EditText) dialog.findViewById(R.id.edittext_dialog);
                ListView listView_states = (ListView) dialog.findViewById(R.id.dialogbox_listview);
                final Button btSelect = (Button) dialog.findViewById(R.id.dialog_button_industry);
                dialog.show();


                mCityAdapter = new CityAdapter(RegistrationActivity.this, citylist);
                listView_states.setAdapter(mCityAdapter);


                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mCityAdapter.filter(st);
                        } else {
                            mCityAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });


                btSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        itemPositioncity = Constant.itempostion;
                        Searchlist = CityAdapter.listforSearch;

                        String industry = "";

                        for (int j = 0; j < itemPositioncity.size(); j++) {

                            if (j == 0) {

                                int p = itemPositioncity.get(j);
                                String ind = Searchlist.get(p).getCname();
                                industry = ind;
                            }
                            if (j > 0) {
                                int p = itemPositioncity.get(j);
                                String ind = Searchlist.get(p).getCname();
                                industry = industry + "," + ind;
                            }

                        }
                        edtcity.setText(industry);
                        dialog.dismiss();

                    }
                });


            }
        });*/


      /*  edtDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.industry_list_dialogbox);
                etserach = (EditText) dialog.findViewById(R.id.edittext_dialog);
                ListView listView_states = (ListView) dialog.findViewById(R.id.dialogbox_listview);
                final Button btSelect = (Button) dialog.findViewById(R.id.dialog_button_industry);
                dialog.show();


                mDistrictAdapter = new DistrictAdapter(RegistrationActivity.this, districtlist);
                listView_states.setAdapter(mDistrictAdapter);


                etserach.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String st = etserach.getText().toString();
                        if (!s.equals("") && s.length() > 0) {
                            mDistrictAdapter.filter(st);
                        } else {
                            mDistrictAdapter.filter(st);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });


                btSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        itemPositionDistrict = Constant.itempostionDistrict;
                        SearchlistDistrict = DistrictAdapter.listforSearch;

                        String industry = "";

                        for (int j = 0; j < itemPositionDistrict.size(); j++) {

                            if (j == 0) {

                                int p = itemPositionDistrict.get(j);
                                String ind = SearchlistDistrict.get(p).getDistrictName();
                                industry = ind;
                            }
                            if (j > 0) {
                                int p = itemPositionDistrict.get(j);
                                String ind = SearchlistDistrict.get(p).getDistrictName();
                                industry = industry + "," + ind;
                            }

                        }
                        edtDistrict.setText(industry);
                        dialog.dismiss();

                    }
                });


            }
        });
*/
}
