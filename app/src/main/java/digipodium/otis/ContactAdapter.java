package digipodium.otis;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    Context context;
    LayoutInflater inflater;
    int layout;
    List<String> contactList;

    public ContactAdapter(Context context, int layout, List<String> contactList) {
        this.context = context;
        this.layout = layout;
        this.contactList = contactList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(inflater.inflate(layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        String contact = contactList.get(i);
        holder.txtName.setText(contact);
        holder.btnPay.setTag(contact);
        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = (String) v.getTag();
                Intent intent = new Intent(context, PayMoneyActivity.class);
                intent.putExtra("personname", contact);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView btnPay;
        TextView txtName;

        public Holder(@NonNull View v) {
            super(v);
            btnPay = v.findViewById(R.id.btnPay);
            txtName = v.findViewById(R.id.txtname);
        }
    }
}
