package com.example.mdp_project

import android.app.Activity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast


fun Toast.showCustomToast(message: String, activity: Activity, mode: String)
{
    var layout = activity.layoutInflater.inflate (
        R.layout.custom_toast_success,
        activity.findViewById(R.id.toast_container)
    )

    if(mode == "success") {
        layout = activity.layoutInflater.inflate (
            R.layout.custom_toast_success,
            activity.findViewById(R.id.toast_container)
        )
    }
    else if(mode == "error"){
        layout = activity.layoutInflater.inflate (
            R.layout.custom_toast_error,
            activity.findViewById(R.id.toast_container)
        )
    }


    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    // use the application extension function
    this.apply {
        setGravity(Gravity.BOTTOM, 0, 40)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}