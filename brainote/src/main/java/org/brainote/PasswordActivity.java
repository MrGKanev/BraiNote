

package org.brainote;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import com.afollestad.materialdialogs.MaterialDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import org.brainote.db.DbHelper;
import org.brainote.models.Note;
import org.brainote.models.ONStyle;
import org.brainote.models.PasswordValidator;
import org.brainote.utils.Constants;
import org.brainote.utils.Security;

import java.util.List;


public class PasswordActivity extends BaseActivity {

    private ViewGroup crouton_handle;
    private EditText passwordCheck;
    private EditText password;
    private EditText question;
    private EditText answer;
    private EditText answerCheck;
    private PasswordActivity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);
        int screenHeight = (int) (metrics.heightPixels * 0.80);
        setContentView(org.brainote.R.layout.activity_password);
        getWindow().setLayout(screenWidth, screenHeight);
        mActivity = this;
        setActionBarTitle(getString(org.brainote.R.string.title_activity_password));
        initViews();
    }


    private void initViews() {
        crouton_handle = (ViewGroup) findViewById(org.brainote.R.id.crouton_handle);
        password = (EditText) findViewById(org.brainote.R.id.password);
        passwordCheck = (EditText) findViewById(org.brainote.R.id.password_check);
        question = (EditText) findViewById(org.brainote.R.id.question);
        answer = (EditText) findViewById(org.brainote.R.id.answer);
        answerCheck = (EditText) findViewById(org.brainote.R.id.answer_check);

        findViewById(org.brainote.R.id.password_remove).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getString(Constants.PREF_PASSWORD, null) != null) {
                    requestPassword(mActivity, new PasswordValidator() {
                        @Override
                        public void onPasswordValidated(boolean passwordConfirmed) {
                            if (passwordConfirmed) {
                                updatePassword(null, null, null);
                            }
                        }
                    });
                } else {
                    Crouton.makeText(mActivity, org.brainote.R.string.password_not_set, ONStyle.WARN, crouton_handle).show();
                }
            }
        });

        findViewById(org.brainote.R.id.password_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    final String passwordText = password.getText().toString();
                    final String questionText = question.getText().toString();
                    final String answerText = answer.getText().toString();
                    if (prefs.getString(Constants.PREF_PASSWORD, null) != null) {
                        requestPassword(mActivity, new PasswordValidator() {
                            @Override
                            public void onPasswordValidated(boolean passwordConfirmed) {
                                if (passwordConfirmed) {
                                    updatePassword(passwordText, questionText, answerText);
                                }
                            }
                        });
                    } else {
                        updatePassword(passwordText, questionText, answerText);
                    }
                }
            }
        });

        findViewById(org.brainote.R.id.password_forgotten).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getString(Constants.PREF_PASSWORD, "").length() == 0) {
                    Crouton.makeText(mActivity, org.brainote.R.string.password_not_set, ONStyle.WARN, crouton_handle).show();
                    return;
                }
                // Inflate layout
                View layout = getLayoutInflater().inflate(org.brainote.R.layout.password_reset_dialog_layout, null);
                final EditText answerEditText = (EditText) layout.findViewById(org.brainote.R.id.reset_password_answer);

                new MaterialDialog.Builder(mActivity)
                        .title(prefs.getString(Constants.PREF_PASSWORD_QUESTION, ""))
                        .customView(layout, false)
                        .autoDismiss(false)
                        .contentColorRes(org.brainote.R.color.text_color)
                        .positiveText(org.brainote.R.string.ok)
                        .callback(new MaterialDialog.SimpleCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                // When positive button is pressed answer correctness is checked
                                String oldAnswer = prefs.getString(Constants.PREF_PASSWORD_ANSWER, "");
                                String answer = answerEditText.getText().toString();
                                // The check is done on password's hash stored in preferences
                                boolean result = Security.md5(answer).equals(oldAnswer);

                                if (result) {
                                    removePassword();
                                } else {
                                    answerEditText.setError(getString(org.brainote.R.string.wrong_answer));
                                }
                            }
                        }).build().show();
            }
        });
    }


    /**
     * Removes the lock from all notes
     */
    private void unlockAllNotes() {
        List<Note> lockedNotes = DbHelper.getInstance(getApplicationContext()).getNotesWithLock(true);
        for (Note lockedNote : lockedNotes) {
            lockedNote.setLocked(false);
            DbHelper.getInstance(getApplicationContext()).updateNote(lockedNote, false);
        }
    }


    private void updatePassword(String passwordText, String questionText, String answerText) {
        if (passwordText == null) {
            if (prefs.getString(Constants.PREF_PASSWORD, "").length() == 0) {
                Crouton.makeText(mActivity, org.brainote.R.string.password_not_set, ONStyle.WARN, crouton_handle).show();
                return;
            }
            new MaterialDialog.Builder(mActivity)
                    .content(org.brainote.R.string.agree_unlocking_all_notes)
                    .positiveText(org.brainote.R.string.ok)
                    .callback(new MaterialDialog.SimpleCallback() {
                        @Override
                        public void onPositive(MaterialDialog materialDialog) {
                            removePassword();
                        }
                    }).build().show();
        } else if (passwordText.length() == 0) {
            Crouton.makeText(mActivity, org.brainote.R.string.empty_password, ONStyle.WARN, crouton_handle).show();

        } else {
            prefs.edit()
                    .putString(Constants.PREF_PASSWORD, Security.md5(passwordText))
                    .putString(Constants.PREF_PASSWORD_QUESTION, questionText)
                    .putString(Constants.PREF_PASSWORD_ANSWER, Security.md5(answerText))
                    .apply();
            Crouton.makeText(mActivity, org.brainote.R.string.password_successfully_changed, ONStyle.CONFIRM, crouton_handle).show();
        }
    }


    private void removePassword() {
        unlockAllNotes();
        passwordCheck.setText("");
        password.setText("");
        question.setText("");
        answer.setText("");
        answerCheck.setText("");
        prefs.edit()
                .remove(Constants.PREF_PASSWORD)
                .remove(Constants.PREF_PASSWORD_QUESTION)
                .remove(Constants.PREF_PASSWORD_ANSWER)
                .remove("settings_password_access")
                .apply();
        Crouton.makeText(mActivity, org.brainote.R.string.password_successfully_removed,
                ONStyle.ALERT, crouton_handle).show();
    }


    /**
     * Checks correctness of form data
     *
     * @return
     */
    private boolean checkData() {
        boolean res = true;

        if (password.getText().length() == passwordCheck.getText().length()
                && passwordCheck.getText().length() == 0) {
            return true;
        }

        boolean passwordOk = password.getText().toString().length() > 0;
        boolean passwordCheckOk = passwordCheck.getText().toString().length() > 0 && password.getText().toString()
                .equals(passwordCheck.getText().toString());
        boolean questionOk = question.getText().toString().length() > 0;
        boolean answerOk = answer.getText().toString().length() > 0;
        boolean answerCheckOk = answerCheck.getText().toString().length() > 0 && answer.getText().toString().equals
                (answerCheck.getText().toString());

        if (!passwordOk || !passwordCheckOk || !questionOk || !answerOk || !answerCheckOk) {
            res = false;
            if (!passwordOk) {
                password.setError(getString(org.brainote.R.string.settings_password_not_matching));
            }
            if (!passwordCheckOk) {
                passwordCheck.setError(getString(org.brainote.R.string.settings_password_not_matching));
            }
            if (!questionOk) {
                question.setError(getString(org.brainote.R.string.settings_password_question));
            }
            if (!answerOk) {
                answer.setError(getString(org.brainote.R.string.settings_answer_not_matching));
            }
            if (!answerCheckOk) {
                answerCheck.setError(getString(org.brainote.R.string.settings_answer_not_matching));
            }
        }
        return res;
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

}
