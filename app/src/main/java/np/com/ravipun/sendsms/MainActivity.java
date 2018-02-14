package np.com.ravipun.sendsms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import np.com.ravipun.sendsms.api.ApiClient;
import np.com.ravipun.sendsms.api.ApiInterface;
import np.com.ravipun.sendsms.model.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    EditText toET, messageET;
    TextView messageAreaTV;
    Button sendButton;
    String FROM_NUMBER = "TestApp", TO_NUMBER = "", MESSAGE = "";
    String displayResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


        sendButton.setOnClickListener(this);

    }

    private void initComponents() {
        toET = findViewById(R.id.et_to_number);
        messageET = findViewById(R.id.et_message);
        sendButton = findViewById(R.id.btn_send);
        messageAreaTV = findViewById(R.id.tv_msg_area);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_send:

                showShortToast("Send button clicked");

                getValuesFromETs();

                if (validateAllFields()) {
                    makeSendSMSApiRequest(FROM_NUMBER, TO_NUMBER, MESSAGE);
                } else {
                    showShortToast("Validation failed");
                }

                break;

            default:
                Log.d(TAG, " I am in default onClick");

        }
    }

    private void makeSendSMSApiRequest(String fromNumber, String toNumber, String message) {
        ApiInterface sendSMSapiInterface =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MessageResponse> call = sendSMSapiInterface.getMessageResponse(Config.ApiKey, Config.ApiSecret,
                fromNumber, toNumber, message);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                try {
                    Log.d(TAG, String.valueOf(response.code()));
                    if (response.code() == 200) {
                        Log.d(TAG, response.body().toString());
                        Log.d(TAG, response.body().getMessages().toString());
                        Log.d(TAG, response.body().getMessageCount());
                        for (int i = 0; i < response.body().getMessageCount().length(); i++) {
                            Log.d(TAG, response.body().getMessages()[i].getTo());
                            Log.d(TAG, response.body().getMessages()[i].getMessageId());
                            Log.d(TAG, response.body().getMessages()[i].getStatus());
                            Log.d(TAG, response.body().getMessages()[i].getRemainingBalance());
                            Log.d(TAG, response.body().getMessages()[i].getMessagePrice());
                            Log.d(TAG, response.body().getMessages()[i].getNetwork());

                            displayResult = "TO: " + response.body().getMessages()[i].getTo() + "\n"
                                    + "Message-id: " + response.body().getMessages()[i].getMessageId() + "\n"
                                    + "Status: " + response.body().getMessages()[i].getStatus() + "\n"
                                    + "Remaining balance: " + response.body().getMessages()[i].getRemainingBalance() + "\n"
                                    + "Message price: " + response.body().getMessages()[i].getMessagePrice() + "\n"
                                    + "Network: " + response.body().getMessages()[i].getNetwork() + "\n\n";

                        }

                        messageAreaTV.setText(displayResult);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                showShortToast("onFailure");
            }
        });
    }

    private boolean validateAllFields() {
        if (!Patterns.PHONE.matcher(TO_NUMBER).matches()) {
            toET.setError("Please enter valid number");
            return false;
        } else if (MESSAGE.length() == 0) {
            messageET.setError("The message is empty");
            return false;
        } else {
            return true;
        }
    }

    private void getValuesFromETs() {
        TO_NUMBER = toET.getText().toString().trim();
        MESSAGE = messageET.getText().toString().trim();
    }

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
