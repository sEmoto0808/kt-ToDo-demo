package com.example.semoto.todo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_detail.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailFragment : Fragment() {

    private var title = ""
    private var deadline = ""
    private var taskDetail = ""
    private var isCompleted = false

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(IntentKey.TITLE.name)
            deadline = it.getString(IntentKey.DEADLINE.name)
            taskDetail = it.getString(IntentKey.TASK_DETAIL.name)
            isCompleted = it.getBoolean(IntentKey.IS_COMPLETED.name)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        // onCreateOptionMenuを受け取る
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title_detail.text = title
        deadline_detail.text = deadline
        todo_detail.text = taskDetail
    }

    /**
     * メニューアイテムを設定
     * */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        // 各アイテムの表示・非表示
        menu!!.apply {
            findItem(R.id.menu_delete).isVisible = true
            findItem(R.id.menu_edit).isVisible = true
            findItem(R.id.menu_register).isVisible = false
        }
    }

    /**
     * メニューのクリック処理
     * */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_delete -> {
                deleteSelectedTodo(title, deadline, taskDetail)
            }
            R.id.menu_edit -> {
                listener?.onEditSelectedTodo(title, deadline, taskDetail, isCompleted, ModeInEdit.EDIT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("CommitTransaction")
    private fun deleteSelectedTodo(title: String, deadline: String, taskDetail: String) {
        val realm = Realm.getDefaultInstance()
        val selectedTodo = realm.where(TodoModel::class.java)
                .equalTo(TodoModel::title.name, title)
                .equalTo(TodoModel::deadline.name, deadline)
                .equalTo(TodoModel::taskDetail.name, taskDetail)
                .findFirst()

        realm.beginTransaction()
        selectedTodo?.deleteFromRealm()
        realm.commitTransaction()

        listener?.onDetaDeleted()
        fragmentManager?.beginTransaction()?.remove(this)?.commit()

        realm.close()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onDetaDeleted()
        fun onEditSelectedTodo(title: String, deadline: String, taskDetail: String, isCompleted: Boolean, mode: ModeInEdit)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        @JvmStatic
        fun newInstance(title: String,
                        deadline: String,
                        taskDetail: String,
                        isCompleted: Boolean) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(IntentKey.TITLE.name, title)
                        putString(IntentKey.DEADLINE.name, deadline)
                        putString(IntentKey.TASK_DETAIL.name, taskDetail)
                        putBoolean(IntentKey.IS_COMPLETED.name, isCompleted)
                    }
                }
    }
}
