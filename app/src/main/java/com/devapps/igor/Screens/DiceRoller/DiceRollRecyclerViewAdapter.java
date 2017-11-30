package com.devapps.igor.Screens.DiceRoller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devapps.igor.R;
import com.devapps.igor.Screens.DiceRoller.DiceRollerFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DiceRoll} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class DiceRollRecyclerViewAdapter extends RecyclerView.Adapter<DiceRollRecyclerViewAdapter.ViewHolder> {

    private final List<DiceRoll> mValues;
    private final OnListFragmentInteractionListener mListener;

    public DiceRollRecyclerViewAdapter(List<DiceRoll> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dice_roll, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mResultView.setText("" + mValues.get(position).getResult());
        holder.mSendTime.setText("" + mValues.get(position).getSendTime());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // TODO Abrir tela com informações da jogada
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsername;
        public final TextView mResultView;
        public final TextView mSendTime;
        public DiceRoll mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = (TextView) view.findViewById(R.id.username);
            mResultView = (TextView) view.findViewById(R.id.result);
            mSendTime = (TextView) view.findViewById(R.id.dice_send_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mResultView.getText() + "'";
        }
    }
}
