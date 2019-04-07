package com.example.semoto.todo

import io.realm.RealmObject

open class TodoModel: RealmObject() {

    // タイトル
    var title = ""
    // 期日(yyyy/mm/dd)
    var deadline = ""
    // タスク内容
    var taskDetail = ""
    // タスク完了フラグ
    var isCompleted = false
}