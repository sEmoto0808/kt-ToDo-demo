package com.example.semoto.todo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_edit.*


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
    // TODO: Rename and change types of parameters
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
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
        // TODO DBへの登録処理
        return super.onOptionsItemSelected(item)
    }

    /**
     * Viewの更新はonActivityCreatedでやる方が安全
     * */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateUi(mode)
    }

    private fun updateUi(mode: ModeInEdit) {

        when (mode) {
            ModeInEdit.NEW_ENTRY -> {
                checkBox.visibility = View.INVISIBLE
            }
            ModeInEdit.EDIT -> {

            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
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
