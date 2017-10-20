package com.devapps.igor.Screens.HomeJogosFrontend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devapps.igor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by danielbarboni on 07/10/17.
 */

public class HomeJogosFrontendMyAdapter extends RecyclerView.Adapter<HomeJogosFrontendMyAdapter.ViewHolder> {

    private List<HomeJogosFrontendListItem> homejogosfrontendListItems;
    private Context homejogosfrontendcontext;

    public HomeJogosFrontendMyAdapter(List<HomeJogosFrontendListItem> homejogosfrontendListItems, Context homejogosfrontendcontext) {
        this.homejogosfrontendListItems = homejogosfrontendListItems;
        this.homejogosfrontendcontext = homejogosfrontendcontext;
    }

    @Override
    public HomeJogosFrontendMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_jogos_frontend_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeJogosFrontendMyAdapter.ViewHolder holder, int position) {
        final HomeJogosFrontendListItem listItem = homejogosfrontendListItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
        holder.textViewTeam.setText(listItem.getTeam());
        holder.textViewFirstAppearance.setText(listItem.getFirstappearance());
        holder.textViewCreatedBy.setText(listItem.getCreatedby());
        holder.textViewPublisher.setText(listItem.getPublisher());
        Picasso.with(homejogosfrontendcontext)
                .load(listItem.getImageUrl())
                .into(holder.imageView);
        holder.textViewBio.setText(listItem.getBio());

        holder.homejogosfrontendlinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(homejogosfrontendcontext, "You clicked "+listItem.getHead(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return homejogosfrontendListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewTeam;
        public TextView textViewFirstAppearance;
        public TextView textViewCreatedBy;
        public TextView textViewPublisher;
        public ImageView imageView;
        public TextView textViewBio;
        public LinearLayout homejogosfrontendlinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewTeam = (TextView) itemView.findViewById(R.id.textViewTeam);
            textViewFirstAppearance = (TextView) itemView.findViewById(R.id.textViewFirstAppearance);
            textViewCreatedBy = (TextView) itemView.findViewById(R.id.textViewCreatedBy);
            textViewPublisher = (TextView) itemView.findViewById(R.id.textViewPublisher);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewBio = (TextView) itemView.findViewById(R.id.textViewBio);
            homejogosfrontendlinearLayout = (LinearLayout) itemView.findViewById(R.id.homejogosfrontendlinearLayout);
        }
    }
}
