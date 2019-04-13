package com.example.semoto.todo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_edit.*
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EditFragment : Fragment() {

    private var title = ""
    private var deadline = ""
    private var taskDetail = ""
    private var isCompleted = false
    private var mode = ModeInEdit.NEW_ENTRY

    private var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(IntentKey.TITLE.name)
            deadline = it.getString(IntentKey.DEADLINE.name)
            taskDetail = it.getString(IntentKey.TITLE.name)
            isCompleted = it.getBoolean(IntentKey.IS_COMPLETED.name)
            mode = it.getSerializable(IntentKey.MODE_IN_EDIT.name) as ModeInEdit
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        // onCreateOptionMenuを受け取る
        setHasOptionsMenu(true)
        return view
    }

    /**
     * メニューアイテムを設定
     * */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        // 各アイテムの表示・非表示
        menu!!.apply {
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = true
        }
    }

    /**
     * メニューのクリック処理
     * */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.menu_register) recordToRealmDB(mode)
        return super.onOptionsItemSelected(item)
    }

    private fun recordToRealmDB(mode: ModeInEdit) {
        
        val isRequiredItemFills = isRequiredItemFilledCheck()
        if (!isRequiredItemFills) return

        when (mode) {
            ModeInEdit.NEW_ENTRY -> addNewTodo()
            ModeInEdit.EDIT -> editExistingTodo()
        }

        listener?.onDataEdited()
        fragmentManager?.let {
            it.beginTransaction().remove(this).commit()
        }
    }

    private fun isRequiredItemFilledCheck(): Boolean {

        if (inputTitleText.text.toString() == "") {
            inputTitle.error = getString(R.string.error)
            return false
        }

        if (!inputDateCheck(inputDateText.text.toString())) {
            inputDate.error = getString(R.string.error)
            return false
        }

        return true
    }

    @SuppressLint("SimpleDateFormat")
    private fun inputDateCheck(inputDate: String): Boolean {
        if (inputDate == "") return false

        try {
            val format = SimpleDateFormat("yyyy/MM/dd")
            format.isLenient = false
            format.parse(inputDate)
        } catch (e: ParseException) {
            return false
        }
        return true
    }

    private fun addNewTodo() {

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val newTodo = realm.createObject(TodoModel::class.java)
        newTodo.apply {
            title = inputTitleText.text.toString()
            deadline = inputDateText.text.toString()
            taskDetail = inputDetailText.text.toString()
            isCompleted = checkBox.isChecked
        }
        realm.commitTransaction()
        realm.close()
    }

    private fun editExistingTodo() {

        val realm = Realm.getDefaultInstance()
        val selectedTodo = realm.where(TodoModel::class.java)
                .equalTo(TodoModel::title.name, title)
                .equalTo(TodoModel::deadline.name, deadline)
                .equalTo(TodoModel::taskDetail.name, taskDetail)
                .findFirst()

        selectedTodo?.let {
            realm.beginTransaction()

            it.title = inputTitleText.text.toString()
            it.deadline = inputDateText.text.toString()
            it.taskDetail = inputDetailText.text.toString()
            it.isCompleted = checkBox.isChecked

            realm.commitTransaction()
        }
//        selectedTodo.apply {
//            title = inputTitleText.text.toString()
//            deadline = inputDateText.text.toString()
//            taskDetail = inputDetailText.text.toString()
//            isCompleted = checkBox.isChecked
//        }
//

        realm.close()
    }


    /**
     * Viewの更新はonActivityCreatedでやる方が安全
     * */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateUi(mode)

        imageButtonDateSet.setOnClickListener {
            listener?.onDatePickerLaunched()
        }
    }

    private fun updateUi(mode: ModeInEdit) {

        when (mode) {
            ModeInEdit.NEW_ENTRY -> {
                checkBox.visibility = View.INVISIBLE
            }
            ModeInEdit.EDIT -> {
                inputTitleText.setText(title)
                inputDateText.setText(deadline)
                inputDetailText.setText(taskDetail)
                checkBox.isChecked = isCompleted

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {

        fun onDatePickerLaunched()
        fun onDataEdited()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment EditFragment.
         */
        @JvmStatic
        fun newInstance(title: String,
                        deadline: String,
                        taskDetail: String,
                        isCompleted: Boolean,
                        mode: ModeInEdit) =
                EditFragment().apply {
                    arguments = Bundle().apply {
                        putString(IntentKey.TITLE.name, title)
                        putString(IntentKey.DEADLINE.name, deadline)
                        putString(IntentKey.TASK_DETAIL.name, taskDetail)
                        putBoolean(IntentKey.IS_COMPLETED.name, isCompleted)
                        putSerializable(IntentKey.MODE_IN_EDIT.name, mode)
                    }
                }
    }
}
