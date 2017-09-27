package com.example.arpansharma.bloodbankremastered;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RequestActivity extends android.support.v4.app.Fragment {
    Spinner spinnerBG;
    SearchableSpinner spinnerState, spinnerCity;
    TextInputLayout nameWrapperReq, emailWrapperReq, phoneWrapperReq;
    Button button;
    private ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_request, container, false);
        spinnerBG = view.findViewById(R.id.bloodGroup);
        spinnerState = view.findViewById(R.id.state);
        spinnerCity = view.findViewById(R.id.city);

        nameWrapperReq = view.findViewById(R.id.nameWrapperReq);
        nameWrapperReq.setHint("Name");

        emailWrapperReq = view.findViewById(R.id.emailWrapperReq);
        emailWrapperReq.setHint("Email");

        phoneWrapperReq = view.findViewById(R.id.phoneWrapperReq);
        phoneWrapperReq.setHint("Contact");

        button = view.findViewById(R.id.submitReq);

        progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Requesting ...");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                if (validation()) {
                    final String name = nameWrapperReq.getEditText().getText().toString().trim();
                    final String email = emailWrapperReq.getEditText().getText().toString().trim();
                    final String phone = "+91" + phoneWrapperReq.getEditText().getText().toString().trim();

                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                    progressDialog.show();
                    Window window = progressDialog.getWindow();
                    window.setLayout(900, 400);

                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.dimAmount=0.5f;
                    window.setAttributes(lp);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Constants.URL_REQUEST,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        nameWrapperReq.getEditText().setText("");
                                        emailWrapperReq.getEditText().setText("");
                                        phoneWrapperReq.getEditText().setText("");
                                        spinnerBG.setSelection(0);
                                        spinnerState.setSelection(0);
                                        spinnerCity.setSelection(0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.hide();
                                    //Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                                    if ( error instanceof NoConnectionError) {
                                        Toast.makeText(getActivity(), "Please Check Your Internet Connection",
                                                Toast.LENGTH_LONG).show();

                                    }

                                    else if (error instanceof TimeoutError ||
                                                error instanceof AuthFailureError ||
                                                    error instanceof ServerError ||
                                                        error instanceof NetworkError ||
                                                            error instanceof ParseError) {
                                        Toast toast = Toast.makeText(getActivity(), "Some Error Occurred at Our End,\n" +
                                                "Please Try Again Later", Toast.LENGTH_LONG);
                                        TextView textView = toast.getView().findViewById(android.R.id.message);
                                        if (textView != null)
                                            textView.setGravity(Gravity.CENTER);
                                        toast.show();
                                    }
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Name", name);
                            params.put("Email", email);
                            params.put("Phone", phone);

                            return params;
                        }
                    };
                    RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
                }
                else
                    Toast.makeText(getActivity(), "Some Information is missing", Toast.LENGTH_LONG).show();
            }
        });

        final ArrayAdapter<CharSequence> adapterBG = ArrayAdapter.createFromResource
                (getActivity(), R.array.blood_group, R.layout.spinner_item);
        final ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource
                (getActivity(), R.array.state, R.layout.spinner_item);
        adapterBG.setDropDownViewResource(R.layout.spinner_item);
        spinnerBG.setAdapter(adapterBG);
        adapterState.setDropDownViewResource(R.layout.spinner_item);
        spinnerState.setAdapter(adapterState);


        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int pos, long lng) {

                if (pos == 0) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.AnNic, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 1) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.AP, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 2) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.AR, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 3) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Assam, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 4) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Bihar, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 5) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Chd, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 6) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Chatt, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 7) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.DN, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 8) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.DD, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 9) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Delhi, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 10) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Goa, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 11) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Gujarat, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 12) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Haryana, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 13) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.HP, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 14) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.JK, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 15) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Jhar, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 16) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Karnataka, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 17) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Kerala, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 18) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Lakshya, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 19) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.MP, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 20) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Mhrstra, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 21) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Manipur, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 22) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Meghalaya, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 23) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Mizoram, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 24) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Naga, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 25) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Orissa, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 26) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Pondi, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 27) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Punjab, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 28) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Rajasthan, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 29) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Sikkim, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 30) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.TN, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 31) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.Tripura, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 32) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.UK, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 33) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.UP, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
                else if (pos == 34) {
                    final ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(getActivity(),
                            R.array.WB, R.layout.spinner_item);
                    adapterCity.setDropDownViewResource(R.layout.spinner_item);
                    spinnerCity.setAdapter(adapterCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        return view;
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validation() {

        int flag = 0;

        if(nameWrapperReq.getEditText().getText().toString().trim().isEmpty()) {
            nameWrapperReq.setError("Please Enter Name");
            flag = 1;
        }
        else
            nameWrapperReq.setError(null);

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailWrapperReq.getEditText().getText().toString().trim());
        if(emailWrapperReq.getEditText().getText().toString().isEmpty()) {
            emailWrapperReq.setError("Please Enter E-mail");
            flag = 1;
        }
        else if (!matcher.matches()) {
            emailWrapperReq.setError("Please Enter a Valid E-mail ID");
            flag = 1;
        }
        else
            emailWrapperReq.setError(null);

        int phoneLength = phoneWrapperReq.getEditText().getText().toString().trim().length();
        if(phoneWrapperReq.getEditText().getText().toString().isEmpty()) {
            phoneWrapperReq.setError("Please Enter Contact Number");
            flag = 1;
        }
        else if(phoneLength < 10) {
            phoneWrapperReq.setError("Please Enter a Valid Number");
            flag = 1;
        }
        else
            phoneWrapperReq.setError(null);

        if (flag == 1)
            return false;
        return true;
    }
}
