import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waterdelivery.R
import com.waterdelivery.notification.NotificationResponse
import kotlinx.android.synthetic.main.adpter_notification.view.*


class NotificationAdapter(
    var context: Context,
    var notificationlist: ArrayList<NotificationResponse.Data>
) : RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {
    class NotificationHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(
            LayoutInflater.from(context).inflate(
                R.layout.adpter_notification,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.itemView.notifcationname.text = notificationlist[position].title
        holder.itemView.category.text = notificationlist[position].message
        holder.itemView.time.text = notificationlist[position].created_date
    }

    override fun getItemCount(): Int {
        return notificationlist.size
    }
}