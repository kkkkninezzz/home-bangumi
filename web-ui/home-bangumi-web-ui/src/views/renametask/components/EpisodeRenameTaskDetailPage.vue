<script setup lang="ts">
import { ref } from "vue";
import { useDetailPage } from "./hooks";
import "plus-pro-components/es/components/form/style/css";
import RssBangumiCardDetailPageBangumiForm from "./RssBangumiCardDetailPageBangumiForm.vue";
import RssBangumiCardDetailPageEpisodesTable from "./RssBangumiCardDetailPageEpisodesTable.vue";

defineOptions({
  name: "RssBangumiCardDetailPage"
});

const { initToDetailPage, getParameter, router } = useDetailPage();
initToDetailPage();

const rssBangumiId = ref(Number(getParameter["id"]));

const episodesTablekey = ref(new Date().getTime());
const bangumiFormkey = ref(new Date().getTime());
// 当预览成功时重新刷新剧集列表
function refreshAll() {
  refreshEpisodesTable();
  refreshBangumiForm();
}

function refreshEpisodesTable() {
  episodesTablekey.value = new Date().getTime();
}

function refreshBangumiForm() {
  bangumiFormkey.value = new Date().getTime();
}
</script>
<template>
  <div>
    <el-container>
      <el-header height="300">
        <el-card>
          <RssBangumiCardDetailPageBangumiForm
            :key="bangumiFormkey"
            :rss-bangumi-id="rssBangumiId"
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
            :key="episodesTablekey"
            :rss-bangumi-id="rssBangumiId"
            height="400"
            @ignore-success="refreshEpisodesTable"
            @delete-success="refreshEpisodesTable"
            @manual-parse-success="refreshEpisodesTable"
            @rename-success="refreshEpisodesTable" /></el-card
      ></el-main>
    </el-container>
  </div>
</template>
