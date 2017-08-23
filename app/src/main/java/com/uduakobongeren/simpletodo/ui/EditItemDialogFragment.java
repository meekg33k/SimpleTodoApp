package com.uduakobongeren.simpletodo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uduakobongeren.simpletodo.R;

/**
 * @author Uduak Obong-Eren
 * @since 8/21/17.
 */

public class EditItemDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText mEditText;
    private ImageButton mSaveEditBtn;
    private ImageButton mCancelEditBtn;
    private String toDoItemDesc;
    private int toDoItemId;
    private int toDoItemPos;


    public EditItemDialogFragment(){

    }

    public static EditItemDialogFragment newInstance(String itemDesc){
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("desc", itemDesc);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.editItemField);
        mSaveEditBtn = view.findViewById(R.id.saveEditBtn);
        mCancelEditBtn = view.findViewById(R.id.cancelEditBtn);
        final Dialog dialog = getDialog();


        //Get toDoItem attributes
        Bundle bundle = this.getArguments();
        toDoItemDesc = bundle.getString("itemDesc");
        toDoItemId = bundle.getInt("itemId");
        toDoItemPos = bundle.getInt("itemPos");

        mEditText.setText(toDoItemDesc);


        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        //Create view listeners
        mEditText.setOnEditorActionListener(this);

        mSaveEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEditText.getText().toString().equals(toDoItemDesc)){
                    EditItemNameDialogListener listener = (EditItemNameDialogListener) getActivity();
                    listener.onFinishEditDialog(mEditText.getText().toString(), toDoItemId, toDoItemPos);
                    dialog.dismiss();
                }
                else {
                    dialog.cancel();
                }
          }
        });

        mCancelEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {

            EditItemNameDialogListener listener = (EditItemNameDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString(), toDoItemId, toDoItemPos);
            dismiss();
            return true;
        }
        return false;
    }


}
