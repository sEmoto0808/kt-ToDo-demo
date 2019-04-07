package com.example.semoto.todo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * Basic Activity
 * */
class MainActivity : AppCompatActivity() {

    var isTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            goEditScreen("", "", "", false, ModeInEdit.NEW_ENTRY)
        }

        // スマホorタブレットの判定
        isTwoPane = container_detail != null
    }

    private fun goEditScreen(title: String,
                             deadline: String,
                             taskDetail: String,
                             isCompleted: Boolean,
                             mode: ModeInEdit) {

        if (isTwoPane) {
            // タブレットの場合
//            val fragmentManager = supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.add(R.id.container_detail, EditFragment.newInstance("1","1"))
//            fragmentTransaction.commit()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container_detail,
                            EditFragment.newInstance(title,deadline, taskDetail, isCompleted, mode),
                            FragmentTag.EDIT.toString())
                    .commit()
            return
        }

        // スマホの場合
        val intent = Intent(this@MainActivity, EditActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEADLINE.name, deadline)
            putExtra(IntentKey.TASK_DETAIL.name, taskDetail)
            putExtra(IntentKey.IS_COMPLETED.name, isCompleted)
            putExtra(IntentKey.MODE_IN_EDIT.name, mode)
        }
        startActivity(intent)
    }

    /**
     * メニューアイテムを設定
     * */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        // 各アイテムの表示・非表示
        menu.apply {
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = false
        }
        // メニューを表示する時はtrue
        return true
    }

    /**
     * メニューのクリック処理
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
