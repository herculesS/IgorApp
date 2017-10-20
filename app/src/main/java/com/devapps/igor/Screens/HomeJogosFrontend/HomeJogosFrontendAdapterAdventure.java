package com.devapps.igor.Screens.HomeJogosFrontend;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by danielbarboni on 20/10/17.
 */

//public class HomeJogosFrontendAdapterAdventure extends ArrayAdapter<HomeJogosFrontEndListAdventure> {
public class HomeJogosFrontendAdapterAdventure extends RecyclerView.Adapter<HomeJogosFrontendAdapterAdventure.ViewHolder>{

    private List<HomeJogosFrontEndListAdventure> homeJogosFrontEndListAdventure;
    private Context homejogosfrontendcontext;

    private Activity context;
    private List<HomeJogosFrontEndListAdventure> adventureList;

    public HomeJogosFrontendAdapterAdventure(Activity context, List<HomeJogosFrontEndListAdventure> adventureList){
       // super(context, R.layout.fragment_home_jogos_frontend_list_adventures, adventureList);
        this.context = context;
        this.adventureList = adventureList;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.fragment_home_jogos_frontend_list_adventures,null, true);

        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewNextSession = (TextView) listViewItem.findViewById(R.id.textViewNextSession);

        HomeJogosFrontEndListAdventure adventure = adventureList.get(position);

        textViewTitle.setText(adventure.getTitle());
        textViewNextSession.setText(adventure.getNextsession());

        return listViewItem;

    }

    public HomeJogosFrontendAdapterAdventure(List<HomeJogosFrontEndListAdventure> homeJogosFrontEndListAdventure, Context homejogosfrontendcontext) {
        this.homeJogosFrontEndListAdventure = homeJogosFrontEndListAdventure;
        this.homejogosfrontendcontext = homejogosfrontendcontext;
    }

    @Override
    public HomeJogosFrontendAdapterAdventure.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_jogos_frontend_list_adventures,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HomeJogosFrontEndListAdventure listItem = homeJogosFrontEndListAdventure.get(position);


        holder.textViewTitle.setText(listItem.getTitle());
        holder.textViewNextSession.setText(listItem.getNextsession());
        holder.progressBar.setTag(position);
       // Picasso.with(homejogosfrontendcontext)
        //        .load(listItem.getImageUrl())
         //       .into(holder.adventureWindow);

        holder.homejogosfrontendconstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(homejogosfrontendcontext, "You clicked "+listItem.getTitle(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeJogosFrontEndListAdventure.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTitle;
        public TextView textViewNextSession;
        public SeekBar progressBar;
        public ImageView adventureWindow;
        public ConstraintLayout homejogosfrontendconstraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewNextSession = (TextView) itemView.findViewById(R.id.textViewNextSession);
            progressBar = (SeekBar) itemView.findViewById(R.id.progressBar);
            adventureWindow = (ImageView) itemView.findViewById(R.id.adventureWindow);
            homejogosfrontendconstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.homejogosfrontendconstraintLayout);
        }
    }
}
