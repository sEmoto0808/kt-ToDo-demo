package com.example.semoto.todo

val number = 3


enum class IntentKey {
    TITLE,
    DEADLINE,
    TASK_DETAIL,
    IS_COMPLETED,
    MODE_IN_EDIT
}

enum class ModeInEdit {
    NEW_ENTRY,
    EDIT
}

enum class FragmentTag {
    MASTER, DETAIL, EDIT, DATE_PICKER
}