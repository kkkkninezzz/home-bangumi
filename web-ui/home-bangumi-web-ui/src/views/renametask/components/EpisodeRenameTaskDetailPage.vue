<script setup lang="ts">
import { ref } from "vue";
import { useDetailPage } from "./hooks";
import "plus-pro-components/es/components/form/style/css";
import EpisodeRenameTaskDetailPageTaskForm from "./EpisodeRenameTaskDetailPageTaskForm.vue";
import RssBangumiCardDetailPageEpisodesTable from "./RssBangumiCardDetailPageEpisodesTable.vue";

defineOptions({
  name: "EpisodeRenameTaskDetailPage"
});

const { initToDetailPage, getParameter, router } = useDetailPage();
initToDetailPage();

const taskId = ref(Number(getParameter["id"]));

const itemsTablekey = ref(new Date().getTime());
const taskFormkey = ref(new Date().getTime());
// 当预览成功时重新刷新剧集列表
function refreshAll() {
  refreshEpisodesTable();
  refreshBangumiForm();
}

function refreshEpisodesTable() {
  itemsTablekey.value = new Date().getTime();
}

function refreshBangumiForm() {
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
            :taskId="taskId"
            @reparse-success="refreshAll"
            @archive-success="refreshAll"
            @incremental-parse-success="refreshAll"
            @collect-success="refreshEpisodesTable"
            @active-success="refreshAll"
            @inactive-success="refreshAll"
          />
        </el-card>
      </el-header>
      <el-main
        ><el-card>
          <RssBangumiCardDetailPageEpisodesTable
            :key="itemsTablekey"
            :rss-bangumi-id="taskId"
            height="400"
            @ignore-success="refreshEpisodesTable"
            @delete-success="refreshEpisodesTable"
            @manual-parse-success="refreshEpisodesTable"
            @rename-success="refreshEpisodesTable" /></el-card
      ></el-main>
    </el-container>
  </div>
</template>
