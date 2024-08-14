<script setup lang="ts">
import { ref } from "vue";
import { useDetailPage } from "./hooks";
import "plus-pro-components/es/components/form/style/css";
import EpisodeRenameTaskDetailPageTaskForm from "./EpisodeRenameTaskDetailPageTaskForm.vue";
import EpisodeRenameTaskDetailPageEpisodesTable from "./EpisodeRenameTaskDetailPageEpisodesTable.vue";

defineOptions({
  name: "EpisodeRenameTaskDetailPage"
});

const { initToDetailPage, getParameter, router } = useDetailPage();
initToDetailPage();

const taskId = ref(Number(getParameter["id"]));

const itemsTablekey = ref(new Date().getTime());
const taskFormkey = ref(new Date().getTime());

function refreshAll() {
  refreshTaskItemsTable();
  refreshTaskDetailForm();
}

function refreshTaskItemsTable() {
  itemsTablekey.value = new Date().getTime();
}

function refreshTaskDetailForm() {
  taskFormkey.value = new Date().getTime();
}
</script>
<template>
  <div>
    <el-container>
      <el-header height="300">
        <el-card>
          <episode-rename-task-detail-page-task-form
            :key="taskFormkey"
            :task-id="taskId"
            @reparse-success="refreshAll"
            @submit-success="refreshAll"
          />
        </el-card>
      </el-header>
      <el-main
        ><el-card>
          <episode-rename-task-detail-page-episodes-table
            :key="itemsTablekey"
            :task-id="taskId"
            height="400"
            @ignore-success="refreshTaskItemsTable"
            @manual-parse-success="refreshTaskItemsTable" /></el-card
      ></el-main>
    </el-container>
  </div>
</template>
