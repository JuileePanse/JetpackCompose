package com.showcase.app.ui.navigation

object NavRoutes {
    const val TASK_LIST = "task_list"
    const val TASK_DETAIL = "task_detail/{taskId}"
    const val ADD_TASK = "add_task"

    fun taskDetail(taskId: Long): String = "task_detail/$taskId"
}
