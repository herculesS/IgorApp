package com.devapps.igor.Screens.HomeJogosFrontend;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devapps.igor.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by danielbarboni on 20/10/17.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<AdventureList> task;
    protected Context context;


    public RecyclerViewAdapter(Context context, List<AdventureList> task) {
        this.task = task;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_jogos_frontend_list_adventures,parent,false);
        return new ViewHolder(layoutView);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, final int position) {

        final AdventureList listItem = task.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.Titulo_Aventura.setText(listItem.getTitle());
        holder.Proxima_Sessao.setText(listItem.getNextsession());
        holder.Barra_Progresso.setTag(listItem.getProgressbar());

      //  holder.homejogosfrontendconstraintLayout.setOnClickListener(new View.OnClickListener() {
        holder.Layout_Relativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Toast.makeText(context, "You clicked "+task.get(position).getTitle(), Toast.LENGTH_LONG).show();
                Toast.makeText(context, "You clicked "+task.get(position).getProgressbar(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        int arr = 0;

        try{
            if(task.size()==0){

                arr = 0;

            }
            else{

                arr=task.size();
            }



        }catch (Exception e){



        }

        return arr;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Titulo_Aventura;
        public TextView Proxima_Sessao;
        public SeekBar Barra_Progresso;
      //  public ImageView adventureWindow;
       // public ImageView favorite;
  //      public ConstraintLayout homejogosfrontendconstraintLayout;
        public RelativeLayout Layout_Relativo;


        public ViewHolder(View itemView) {
            super(itemView);

            Titulo_Aventura = (TextView) itemView.findViewById(R.id.textViewTitle);
            Proxima_Sessao = (TextView) itemView.findViewById(R.id.textViewNextSession);
            Barra_Progresso = (SeekBar) itemView.findViewById(R.id.progressBar);
           // adventureWindow = (ImageView) itemView.findViewById(R.id.adventureWindow);
          //  favorite = (ImageView) itemView.findViewById(R.id.favorite);
  //          homejogosfrontendconstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.homejogosfrontendconstraintLayout);
            Layout_Relativo = (RelativeLayout) itemView.findViewById(R.id.homejogosfrontendrelativeLayout);
        }



    }

}
