package com.biswadeep.loadtexts_firebase;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onitemlongclick(view, getAdapterPosition());
                return false;
            }
        });
    }

    public void setData(Context ctx, String name, String gender, String age) {
        TextView textView= itemView.findViewById(R.id.text_row_id);
        textView.setText("Name : "+ name + "\nGender : " + gender + "\nAge : " + age);
    }

    private ViewHolder.clickListener mClickListener;

    public interface clickListener {
        void onitemlongclick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.clickListener clickListener) {
        mClickListener= clickListener;
    }

}
