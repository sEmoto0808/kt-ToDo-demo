package com.example.semoto.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: OnDateSetListener? = null

    interface OnDateSetListener {
        fun onDateSelected(dateString: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDateSetListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    // Dialogの初期設定
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, day)
    }


    // 日付を選択した時
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {

        val dateString = getDateString(year, month, day)

        listener?.onDateSelected(dateString)

        fragmentManager?.let {
            it.beginTransaction().remove(this).commit()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateString(year: Int, month: Int, day: Int): String {

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")

        return dateFormat.format(calendar.time)
    }
}