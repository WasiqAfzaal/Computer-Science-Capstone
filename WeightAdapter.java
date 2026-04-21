public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {

    private final List<WeightEntry> data;
    private final OnWeightActionListener listener;

    public WeightAdapter(List<WeightEntry> data, OnWeightActionListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder holder, int position) {
        WeightEntry entry = data.get(position);

        holder.textRowDate.setText(entry.date);
        holder.textRowWeight.setText(String.valueOf(entry.weight));

        holder.buttonRowEdit.setOnClickListener(v -> listener.onEdit(entry));
        holder.buttonRowDelete.setOnClickListener(v -> listener.onDelete(entry));
    }
}