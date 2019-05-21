package com.example.mariuspop.catalog3.adapters;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mariuspop.catalog3.AbsenteManager;
import com.example.mariuspop.catalog3.FirebaseDb;
import com.example.mariuspop.catalog3.R;
import com.example.mariuspop.catalog3.db.DBHelper;
import com.example.mariuspop.catalog3.interfaces.FirebaseCallbackClasaById;
import com.example.mariuspop.catalog3.models.Absenta;
import com.example.mariuspop.catalog3.models.Clasa;
import com.example.mariuspop.catalog3.models.Elev;
import com.example.mariuspop.catalog3.wizard.AddManager;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by mariuspop on 24/05/18.
 */

public class AbsenteAdapter extends RecyclerView.Adapter<AbsenteAdapter.ViewHolder> implements FirebaseCallbackClasaById {

    private ArrayList<Absenta> absente;
    private Context context;
    private Elev elev;
    private FirebaseCallbackClasaById firebaseCallbackClasaById;
    private Absenta absentaToDelete;

    public AbsenteAdapter(Context context, ArrayList<Absenta> absente, Elev elev) {
        this.absente = absente;
        this.context = context;
        this.elev = elev;
    }

    public void setData(ArrayList<Absenta> absente) {
        this.absente = absente;

    }

    @Override
    public int getItemCount() {
        return absente.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_absenta, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        firebaseCallbackClasaById = this;
        if (absente.get(position).isPending()) {
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.yellow_warm));
            viewHolder.dataAbsenta.setTextColor(Color.BLACK);
            viewHolder.inAsteptare.setTextColor(Color.BLACK);
            viewHolder.inAsteptare.setText(context.getResources().getString(R.string.in_asteptare));
        } else if (!absente.get(position).isMotivata()) {
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.red_warm));
            viewHolder.dataAbsenta.setTextColor(Color.WHITE);
            viewHolder.inAsteptare.setTextColor(Color.BLACK);
            viewHolder.inAsteptare.setText(context.getResources().getString(R.string.abs_trimisa));
        } else {
            viewHolder.dataAbsenta.setTextColor(context.getResources().getColor(R.color.input_login_hint));
            viewHolder.cv.setCardBackgroundColor(context.getResources().getColor(R.color.green_warm));
            viewHolder.inAsteptare.setTextColor(Color.BLACK);
            viewHolder.inAsteptare.setText(context.getResources().getString(R.string.abs_trimisa));
        }
        viewHolder.absentaText.setText(String.valueOf(absente.get(position).isMotivata() ? context.getResources().getString(R.string.absenta_motivata) :
                context.getResources().getString(R.string.absenta_nemotivata)));
        viewHolder.dataAbsenta.setText(String.valueOf(absente.get(position).getData()));
        viewHolder.cv.setTag(absente.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClasaReceived(Clasa clasa) {
        if (absentaToDelete != null) {
            for (Elev elev1 : clasa.getElevi()) {
                if (elev1.getElevId() == elev.getElevId()) {
                    absente.remove(absentaToDelete);
                    elev.getAbsente().remove(absentaToDelete);
                    elev1.setAbsente(elev.getAbsente());
                    AbsenteManager.getInstance().getAlarmManager().cancel(AbsenteManager.getInstance().getPendingIntents().get(absentaToDelete.getAbsentaId()));
                    break;
                }
            }
            FirebaseDb.saveClasa(clasa);
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView absentaText;
        TextView dataAbsenta;
        TextView inAsteptare;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.abs_cv);
            absentaText = itemView.findViewById(R.id.absentaText);
            dataAbsenta = itemView.findViewById(R.id.dataAbsenta);
            inAsteptare = itemView.findViewById(R.id.inAsteptare);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StateListAnimator stateListAnimator = AnimatorInflater
                        .loadStateListAnimator(context, R.anim.animation);
                cv.setStateListAnimator(stateListAnimator);
            }
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Absenta absenta = (Absenta) v.getTag();
                    if (absenta != null) {
                        if (absenta.isPending()) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.cancel_absenta_dialog);
                            dialog.setTitle(context.getResources().getString(R.string.anuleaza_absenta));
                            FancyButton dialogButtonOk = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (elev != null) {
                                        absentaToDelete = absenta;
                                        FirebaseDb.getClasaById(firebaseCallbackClasaById, elev.getClasaId());
                                    }
                                    //cancel job, remove absenta
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            FancyButton dialogButtonCancel = dialog.findViewById(R.id.dialogButtonCancel);
                            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        } else if (!absenta.isMotivata()) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.motiveaza_absenta_dialog);
                            dialog.setTitle(context.getResources().getString(R.string.motiveaza_absenta));
                            FancyButton dialogButtonOk = dialog.findViewById(R.id.dialogButtonOK);
                            dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (elev != null) {
                                        for (Absenta absenta1 : elev.getAbsente()) {
                                            if (absenta.getAbsentaId() == absenta1.getAbsentaId()) {
                                                absenta1.setMotivata(true);
                                            }
                                        }
                                        new DBHelper(context).insertOrUpdateAbsenta(absenta, AddManager.getInstance().getClasa());
                                        notifyDataSetChanged();
                                    }
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            FancyButton dialogButtonCancel = dialog.findViewById(R.id.dialogButtonCancel);
                            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}
