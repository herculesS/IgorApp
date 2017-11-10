package com.devapps.igor.Screens.Books;

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

public class BooksMyAdapter extends RecyclerView.Adapter<BooksMyAdapter.ViewHolder>{

    private List<BooksListItem> booksListItems;
    private Context bookscontext;

    public BooksMyAdapter(List<BooksListItem> booksListItems, Context bookscontext) {
        this.booksListItems = booksListItems;
        this.bookscontext = bookscontext;
    }

    @Override
    public BooksMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_books_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BooksMyAdapter.ViewHolder holder, int position) {
        final BooksListItem listItem = booksListItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
        holder.textViewTeam.setText(listItem.getTeam());
        holder.textViewFirstAppearance.setText(listItem.getFirstappearance());
        holder.textViewCreatedBy.setText(listItem.getCreatedby());
        holder.textViewPublisher.setText(listItem.getPublisher());
        Picasso.with(bookscontext)
                .load(listItem.getImageUrl())
                .into(holder.imageView);
        holder.textViewBio.setText(listItem.getBio());

        holder.bookslinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(bookscontext, "You clicked "+listItem.getHead(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
            return booksListItems.size();
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
        public LinearLayout bookslinearLayout;

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
            bookslinearLayout = (LinearLayout) itemView.findViewById(R.id.bookslinearLayout);
        }
    }
}
