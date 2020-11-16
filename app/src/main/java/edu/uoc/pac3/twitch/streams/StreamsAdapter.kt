package edu.uoc.pac3.twitch.streams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uoc.pac3.R
import edu.uoc.pac3.data.streams.Stream

class StreamsAdapter(private var streams: List<Stream>): RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {

  override fun getItemCount(): Int = streams.count()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.stream_list_content, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.name.text = streams[position].userName
    holder.title.text = streams[position].title

    if (streams[position].imageUri != null) {
      val url = streams[position].imageUri!!
        .replace("{width}","100")
        .replace("{height}","100")

      Glide.with(holder.itemView.context)
        .load(url)
        .centerCrop()
        .into(holder.image)
    }
  }

  fun setStreams(streams: List<Stream>?) {
    this.streams = streams ?: listOf()
    notifyDataSetChanged()
  }

  inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val image: ImageView = view.findViewById(R.id.stream_image)
    val name: TextView = view.findViewById(R.id.stream_name)
    val title: TextView = view.findViewById(R.id.stream_title)
  }
}