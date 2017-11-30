package com.devapps.igor.Screens.HomeJogosFrontend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.AdventureRequestManager;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.Edit.EditAdventureFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.devapps.igor.Screens.HomeJogosFrontend.FragmentAdventure.batata;
import static com.devapps.igor.Screens.HomeJogosFrontend.FragmentAdventure.sessoes;


/**
 * Created by danielbarboni on 20/10/17.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private List<Adventure> task;
    protected FragmentActivity context;
    private boolean mIsInEditMode;


    public RecyclerViewAdapter(FragmentActivity context, List<Adventure> task) {
        this.task = task;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_jogos_frontend_list_adventures, parent, false);
        return new ViewHolder(layoutView);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {
        final Adventure listItem = task.get(position);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.mDeleteAdventure.setTag(listItem.getId());
        holder.mDeleteAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onDeleteAdventure(listItem, position);
            }
        });
        holder.mEditAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = EditAdventureFragment.newInstance(listItem.getId());
                context.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();

            }
        });
        if (mIsInEditMode) {
            holder.Layout_Relativo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            if (listItem.getDMChar() != null &&
                    listItem.getDMChar().getPlayerId().equals(userId)) {
                holder.mEditModeLayout.setVisibility(View.VISIBLE);
                holder.itemView.setAlpha(1f);

            } else {
                holder.mEditModeLayout.setVisibility(View.GONE);
                holder.itemView.setAlpha(0.5f);
            }
        } else {
            holder.Layout_Relativo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("LayoutRel", task.get(position).getId());
                    Fragment fragment = AdventureProgressFragment.newInstance(task.get(position).getId());
                    context.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();

                }
            });
            holder.mEditModeLayout.setVisibility(View.GONE);
            holder.itemView.setAlpha(1f);
        }

        holder.Titulo_Aventura.setText(listItem.getName());
        holder.Proxima_Sessao.setText(listItem.getSummary());
        holder.Proxima_Sessao.setMaxLines(1);

        if (listItem.getBackground() == 1) {
            holder.adventureWindow.setImageResource(R.drawable.miniatura_imagem_automatica);
        }
        if (listItem.getBackground() == 2) {
            holder.adventureWindow.setImageResource(R.drawable.miniatura_krevast);
        }
        if (listItem.getBackground() == 3) {
            holder.adventureWindow.setImageResource(R.drawable.miniatura_coast);
        }
        if (listItem.getBackground() == 4) {
            holder.adventureWindow.setImageResource(R.drawable.miniatura_corvali);
        }
        if (listItem.getBackground() == 5) {
            holder.adventureWindow.setImageResource(R.drawable.miniatura_heartlands);
        }

        holder.Barra_Progresso.setMax(listItem.getSessions().size() - 1);
        holder.text_view.setText("Covered : " + 0 + " / " + holder.Barra_Progresso.getMax());
        ;


        holder.Barra_Progresso.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        holder.text_view.setText("Covered : " + progress + " / " + holder.Barra_Progresso.getMax());
                        Toast.makeText(context, "SeekBar in progress" + task.get(position).getSessions().size(), Toast.LENGTH_LONG).show();
                        if (task.get(position).getSessions().size() != 0) {
                            holder.Proxima_Sessao.setText(task.get(position).getSessions().get(progress).getTitle());
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        holder.text_view.setText("Covered : " + progress_value + " / " + holder.Barra_Progresso.getMax());

                    }
                }
        );
    }

    private void onDeleteAdventure(final Adventure listItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Deseja deletar a aventura "
                + listItem.getName() + "?");
        builder.setPositiveButton("Sim", new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AdventureRequestManager.deleteAdventure(listItem.getId());
                notifyDataSetChanged();
                dialog.cancel();
            }

        });

        builder.setNegativeButton("Não", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        builder.show();
    }


    @Override
    public int getItemCount() {
        int arr = 0;

        try {
            if (task.size() == 0) {

                arr = 0;

            } else {

                arr = task.size();
            }


        } catch (Exception e) {


        }

        return arr;

    }

    public void setEditMode(boolean mode) {
        mIsInEditMode = mode;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mEditModeLayout;
        private ImageView mEditAdventure;
        private ImageView mDeleteAdventure;
        public TextView Titulo_Aventura;
        public TextView Proxima_Sessao;
        public SeekBar Barra_Progresso;
        public TextView text_view;
        public ImageView adventureWindow;

        public RelativeLayout Layout_Relativo;


        public ViewHolder(View itemView) {
            super(itemView);

            Titulo_Aventura = (TextView) itemView.findViewById(R.id.home_jogos_textViewTitle);
            Proxima_Sessao = (TextView) itemView.findViewById(R.id.textViewNextSession);
            Barra_Progresso = (SeekBar) itemView.findViewById(R.id.progressBar);
            text_view = (TextView) itemView.findViewById(R.id.textView);
            adventureWindow = (ImageView) itemView.findViewById(R.id.adventureWindow);
            Layout_Relativo = (RelativeLayout) itemView.findViewById(R.id.homejogosfrontendrelativeLayout);
            mDeleteAdventure = (ImageView) itemView.findViewById(R.id.adventure_item_trash_can);
            mEditAdventure = (ImageView) itemView.findViewById(R.id.adventure_item_edit);
            mEditModeLayout = (LinearLayout) itemView.findViewById(R.id.adventure_edit_layout);
        }


    }

}
